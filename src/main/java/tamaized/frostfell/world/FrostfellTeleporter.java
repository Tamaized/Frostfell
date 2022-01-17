package tamaized.frostfell.world;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import tamaized.frostfell.registry.ModBlocks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class FrostfellTeleporter implements ITeleporter {

	public static final FrostfellTeleporter INSTANCE = new FrostfellTeleporter();

	private static final Map<ResourceLocation, Map<ColumnPos, PortalPosition>> destinationCoordinateCache = new HashMap<>();
	private static final Object2LongMap<ColumnPos> columnMap = new Object2LongOpenHashMap<>();

	private FrostfellTeleporter() {

	}

	static class PortalPosition {
		public final BlockPos pos;
		long lastUpdateTime;

		PortalPosition(BlockPos pos, long time) {
			this.pos = pos;
			this.lastUpdateTime = time;
		}
	}

	@Override
	public Entity placeEntity(Entity oldEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		oldEntity.fallDistance = 0;
		return repositionEntity.apply(false);
	}

	@Override
	public PortalInfo getPortalInfo(Entity entity, ServerLevel dest, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		PortalInfo pos;
		if ((pos = placeInExistingPortal(dest, entity, entity.blockPosition(), entity instanceof Player)) == null) {
			pos = moveToSafeCoords(dest, entity);
			makePortal(entity, dest, pos.pos);
			pos = placeInExistingPortal(dest, entity, new BlockPos(pos.pos), entity instanceof Player);
		}
		return pos;
	}

	private void makePortal(Entity entity, ServerLevel world, Vec3 pos) {
		loadSurroundingArea(world, pos);
		BlockPos spot = findPortalCoords(world, pos, blockPos -> isPortalAt(world, blockPos));
		if (spot != null) {
			cachePortalCoords(world, pos, spot);
			return;
		}
		spot = findPortalCoords(world, pos, blockpos -> isIdealForPortal(world, blockpos));
		if (spot != null) {
			cachePortalCoords(world, pos, makePortalAt(world, spot));
			return;
		}
		cachePortalCoords(world, pos, makePortalAt(world, new BlockPos(entity.getX(), (entity.getY()) - 1.0, entity.getZ())));
	}

	private static void cachePortalCoords(ServerLevel world, Vec3 loc, BlockPos pos) {
		int x = Mth.floor(loc.x), z = Mth.floor(loc.z);
		destinationCoordinateCache.putIfAbsent(world.dimension().location(), Maps.newHashMapWithExpectedSize(4096));
		destinationCoordinateCache.get(world.dimension().location()).put(new ColumnPos(x, z), new PortalPosition(pos, world.getGameTime()));
	}

	private static boolean isIdealForPortal(ServerLevel world, BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = 0; potentialY < 4; potentialY++) {
					BlockPos tPos = pos.offset(potentialX - 1, potentialY, potentialZ - 1);
					Material material = world.getBlockState(tPos).getMaterial();
					if (potentialY == 0 && !material.isSolid() && !material.isLiquid() || potentialY >= 1 && !material.isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected BlockPos makePortalAt(Level world, BlockPos pos) {
		BlockState ice = Blocks.BLUE_ICE.defaultBlockState();

		world.setBlockAndUpdate(pos.west().north(), ice);
		world.setBlockAndUpdate(pos.north(), ice);
		world.setBlockAndUpdate(pos.east().north(), ice);
		world.setBlockAndUpdate(pos.east(2).north(), ice);

		world.setBlockAndUpdate(pos.west(), ice);
		world.setBlockAndUpdate(pos.east(2), ice);

		world.setBlockAndUpdate(pos.west().south(), ice);
		world.setBlockAndUpdate(pos.east(2).south(), ice);

		world.setBlockAndUpdate(pos.west().south(2), ice);
		world.setBlockAndUpdate(pos.south(2), ice);
		world.setBlockAndUpdate(pos.east().south(2), ice);
		world.setBlockAndUpdate(pos.east(2).south(2), ice);

		world.setBlockAndUpdate(pos.below(), ice);
		world.setBlockAndUpdate(pos.east().below(), ice);
		world.setBlockAndUpdate(pos.south().below(), ice);
		world.setBlockAndUpdate(pos.east().south().below(), ice);

		BlockState portal = ModBlocks.PORTAL.get().defaultBlockState();

		world.setBlock(pos, portal, 2);
		world.setBlock(pos.east(), portal, 2);
		world.setBlock(pos.south(), portal, 2);
		world.setBlock(pos.east().south(), portal, 2);

		for (int dx = -1; dx <= 2; dx++) {
			for (int dz = -1; dz <= 2; dz++) {
				for (int dy = 1; dy <= 5; dy++) {
					world.removeBlock(pos.offset(dx, dy, dz), false);
				}
			}
		}

		return pos;
	}

	@Nullable
	private static BlockPos findPortalCoords(ServerLevel world, Vec3 loc, Predicate<BlockPos> predicate) {
		int entityX = Mth.floor(loc.x);
		int entityZ = Mth.floor(loc.z);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		double spotWeight = -1D;
		BlockPos spot = null;
		int range = 16;
		for (int rx = entityX - range; rx <= entityX + range; rx++) {
			double xWeight = (rx + 0.5D) - loc.x;
			for (int rz = entityZ - range; rz <= entityZ + range; rz++) {
				double zWeight = (rz + 0.5D) - loc.z;
				for (int ry = getScanHeight(world, rx, rz); ry >= world.getMinBuildHeight(); ry--) {
					if (!world.isEmptyBlock(pos.set(rx, ry, rz)))
						continue;
					while (ry > world.getMinBuildHeight() && world.isEmptyBlock(pos.set(rx, ry - 1, rz)))
						ry--;
					double yWeight = (ry + 0.5D) - loc.y;
					double rPosWeight = xWeight * xWeight + yWeight * yWeight + zWeight * zWeight;
					if (spotWeight < 0.0D || rPosWeight < spotWeight) {
						if (predicate.test(pos)) {
							spotWeight = rPosWeight;
							spot = pos.immutable();
						}
					}
				}
			}
		}
		return spot;
	}

	private static void loadSurroundingArea(ServerLevel world, Vec3 pos) {

		int x = Mth.floor(pos.x) >> 4;
		int z = Mth.floor(pos.y) >> 4;

		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				world.getChunk(x + dx, z + dz);
			}
		}
	}

	private static PortalInfo moveToSafeCoords(ServerLevel world, Entity entity) {
		BlockPos pos = entity.blockPosition();
		if (isSafeAround(world, pos, entity))
			return makePortalInfo(entity, entity.position());
		BlockPos safeCoords = findSafeCoords(world, 200, pos, entity);
		if (safeCoords != null)
			return makePortalInfo(entity, safeCoords.getX(), entity.getY(), safeCoords.getZ());
		safeCoords = findSafeCoords(world, 400, pos, entity);
		if (safeCoords != null)
			return makePortalInfo(entity, safeCoords.getX(), entity.getY(), safeCoords.getZ());
		return makePortalInfo(entity, entity.position());
	}

	@Nullable
	private static BlockPos findSafeCoords(ServerLevel world, int range, BlockPos pos, Entity entity) {
		int attempts = range / 8;
		for (int x = 0; x < attempts; x++) {
			for (int z = 0; z < attempts; z++) {
				BlockPos dPos = new BlockPos(pos.getX() + (x * attempts) - (range / 2), 100, pos.getZ() + (z * attempts) - (range / 2));

				if (isSafeAround(world, dPos, entity)) {
					return dPos;
				}
			}
		}
		return null;
	}

	public static boolean isSafeAround(Level world, BlockPos pos, Entity entity) {
		if (isUnsafe(world, pos, entity)) {
			return false;
		}
		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (isUnsafe(world, pos.relative(facing, 16), entity)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isUnsafe(Level world, BlockPos pos, Entity entity) {
		return !checkPos(world, pos);
	}

	private static boolean checkPos(Level world, BlockPos pos) {
		return world.getWorldBorder().isWithinBounds(pos);
	}

	private static int getScanHeight(ServerLevel world, BlockPos pos) {
		return getScanHeight(world, pos.getX(), pos.getZ());
	}

	private static int getScanHeight(ServerLevel world, int x, int z) {
		int worldHeight = world.getMaxBuildHeight() - 1;
		int chunkHeight = world.getChunk(x >> 4, z >> 4).getHighestSectionPosition() + 15;
		return Math.min(worldHeight, chunkHeight);
	}

	private static boolean isPortal(BlockState state) {
		return state.getBlock() == ModBlocks.PORTAL.get();
	}

	@Nullable
	private static PortalInfo placeInExistingPortal(ServerLevel world, Entity entity, BlockPos pos, boolean isPlayer) {
		int scan = 32;
		boolean flag = true;
		BlockPos blockpos = BlockPos.ZERO;
		ColumnPos columnPos = new ColumnPos(pos);

		if (!isPlayer && columnMap.containsKey(columnPos)) {
			return null;
		} else {
			PortalPosition portalPosition = destinationCoordinateCache.containsKey(world.dimension().location()) ? destinationCoordinateCache.get(world.dimension().location()).get(columnPos) : null;
			if (portalPosition != null) {
				blockpos = portalPosition.pos;
				portalPosition.lastUpdateTime = world.getGameTime();
				flag = false;
			} else {
				double d0 = Double.MAX_VALUE;
				for (int i1 = -scan; i1 <= scan; ++i1) {
					BlockPos blockpos2;
					for (int j1 = -scan; j1 <= scan; ++j1) {
						if (!world.getWorldBorder().isWithinBounds(pos.offset(i1, 0, j1))) {
							continue;
						}
						ChunkPos chunkPos = new ChunkPos(pos.offset(i1, 0, j1));
						if (!world.getChunkSource().chunkMap.isExistingChunkFull(chunkPos)) {
							continue;
						}
						LevelChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
						for (BlockPos blockpos1 = pos.offset(i1, getScanHeight(world, pos) - pos.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
							blockpos2 = blockpos1.below();
							if (d0 >= 0.0D && blockpos1.distSqr(pos) >= d0) {
								continue;
							}
							if (isPortal(chunk.getBlockState(blockpos1))) {
								for (blockpos2 = blockpos1.below(); isPortal(chunk.getBlockState(blockpos2)); blockpos2 = blockpos2.below()) {
									blockpos1 = blockpos2;
								}
								float d1 = (float) blockpos1.distSqr(pos);
								if (d0 < 0.0D || d1 < d0) {
									d0 = d1;
									blockpos = blockpos1;
									// restrict search radius to new distance
									scan = Mth.ceil(Mth.sqrt(d1));
								}
							}
						}
					}
				}
			}
		}
		if (blockpos.equals(BlockPos.ZERO)) {
			long factor = world.getGameTime() + 300L;
			columnMap.put(columnPos, factor);
			return null;
		} else {
			if (flag) {
				destinationCoordinateCache.putIfAbsent(world.dimension().location(), Maps.newHashMapWithExpectedSize(4096));
				destinationCoordinateCache.get(world.dimension().location()).put(columnPos, new PortalPosition(blockpos, world.getGameTime()));
				world.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, new BlockPos(columnPos.x, blockpos.getY(), columnPos.z));
			}
			BlockPos[] portalBorder = getBoundaryPositions(world, blockpos).toArray(new BlockPos[0]);
			BlockPos borderPos = portalBorder[world.random.nextInt(portalBorder.length)];

			double portalX = borderPos.getX() + 0.5;
			double portalY = borderPos.getY() + 1.0;
			double portalZ = borderPos.getZ() + 0.5;

			return makePortalInfo(entity, portalX, portalY, portalZ);
		}
	}

	private static Set<BlockPos> getBoundaryPositions(ServerLevel world, BlockPos start) {
		Set<BlockPos> result = new HashSet<>(), checked = new HashSet<>();
		checked.add(start);
		checkAdjacent(world, start, checked, result);
		return result;
	}

	private static void checkAdjacent(ServerLevel world, BlockPos pos, Set<BlockPos> checked, Set<BlockPos> result) {
		for (Direction facing : Direction.Plane.HORIZONTAL) {
			BlockPos offset = pos.relative(facing);
			if (!checked.add(offset))
				continue;
			if (isPortalAt(world, offset))
				checkAdjacent(world, offset, checked, result);
			else
				result.add(offset);
		}
	}

	private static boolean isPortalAt(ServerLevel world, BlockPos pos) {
		return isPortal(world.getBlockState(pos));
	}

	private static PortalInfo makePortalInfo(Entity entity, double x, double y, double z) {
		return makePortalInfo(entity, new Vec3(x, y, z));
	}

	private static PortalInfo makePortalInfo(Entity entity, Vec3 pos) {
		return new PortalInfo(pos, Vec3.ZERO, entity.getYRot(), entity.getXRot());
	}

}
