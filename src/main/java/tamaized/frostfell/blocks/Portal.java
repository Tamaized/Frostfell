package tamaized.frostfell.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableInt;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.network.client.ClientPacketPortalFormParticles;
import tamaized.frostfell.registry.ModBlocks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Portal extends HalfTransparentBlock {

	private static final VoxelShape AABB = Shapes.create(new AABB(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F));

	private static final int MIN_PORTAL_SIZE = 4;
	private static final int MAX_PORTAL_SIZE = 64;

	public Portal(Properties properties) {
		super(properties);
	}

	private static boolean recursivelyValidatePortal(Level world, BlockPos pos, Map<BlockPos, Boolean> blocksChecked, MutableInt portalSize, BlockState poolBlock) {
		if (portalSize.incrementAndGet() > MAX_PORTAL_SIZE)
			return false;
		boolean isPoolProbablyEnclosed = true;
		for (int i = 0; i < 4 && portalSize.intValue() <= MAX_PORTAL_SIZE; i++) {
			BlockPos positionCheck = pos.relative(Direction.from2DDataValue(i));
			if (!blocksChecked.containsKey(positionCheck)) {
				BlockState state = world.getBlockState(positionCheck);
				if (state == poolBlock && world.getBlockState(positionCheck.below()).canOcclude()) {
					blocksChecked.put(positionCheck, true);
					if (isPoolProbablyEnclosed) {
						isPoolProbablyEnclosed = recursivelyValidatePortal(world, positionCheck, blocksChecked, portalSize, poolBlock);
					}
				} else if (state.is(Blocks.BLUE_ICE)) {
					blocksChecked.put(positionCheck, false);
				} else
					return false;
			}
		}

		return isPoolProbablyEnclosed;
	}

	private static ResourceKey<Level> getDestination(Entity entity) {
		return entity.getCommandSenderWorld().dimension().location().equals(Frostfell.DIMENSION.location()) ? Level.OVERWORLD : Frostfell.DIMENSION;
	}

	public static void attemptSendEntity(Entity entity, boolean forcedEntry, boolean makeReturnPortal) {
		if (!entity.isAlive() || entity.level.isClientSide)
			return;
		if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions())
			return;
		if (!forcedEntry && entity.portalTime > 0)
			return;

		// set a cooldown before this can run again
		entity.portalTime = 10;

		ResourceKey<Level> destination = getDestination(entity);
		ServerLevel serverWorld = entity.getCommandSenderWorld().getServer().getLevel(destination);

		if (serverWorld == null)
			return;

		//entity.changeDimension(serverWorld, makeReturnPortal ? new Teleporter(forcedEntry) : new NoReturnTeleporter());
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		int random = rand.nextInt(100);
		if (random == 0)
			worldIn.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 2.0F, rand.nextFloat() * 0.4F + 0.8F, false);
		if (random < 50) {
			double xPos = pos.getX() + rand.nextFloat();
			double yPos = pos.getY() + 1D;
			double zPos = pos.getZ() + rand.nextFloat();
			double xSpeed = (rand.nextFloat() * 0.25D) - 0.125D;
			double ySpeed = rand.nextFloat() * 0.35D;
			double zSpeed = (rand.nextFloat() * 0.25D) - 0.125D;
			worldIn.addParticle(ParticleTypes.SNOWFLAKE, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
		}
	}

	public boolean tryToCreatePortal(Level level, BlockPos pos, ItemEntity catalyst, @Nullable Player player) {
		BlockState state = level.getBlockState(pos);
		if (canFormPortal(state) && level.getBlockState(pos.below()).canOcclude()) {
			Map<BlockPos, Boolean> blocksChecked = new HashMap<>();
			blocksChecked.put(pos, true);
			MutableInt size = new MutableInt(0);
			if (recursivelyValidatePortal(level, pos, blocksChecked, size, state) && size.intValue() >= MIN_PORTAL_SIZE) {
				catalyst.getItem().shrink(1);
				if (level instanceof ServerLevel server) {
					Random rand = level.getRandom();
					Frostfell.NETWORK.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), new ClientPacketPortalFormParticles(pos));
					for (int i = 0; i < 5; i++)
						server.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.AMETHYST_CLUSTER_STEP, SoundSource.BLOCKS, 2.0F, rand.nextFloat() * 0.4F + 0.8F);
				}
				for (Map.Entry<BlockPos, Boolean> checkedPos : blocksChecked.entrySet()) {
					if (checkedPos.getValue()) {
						level.setBlock(checkedPos.getKey(), ModBlocks.PORTAL.get().defaultBlockState(), 2);
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean canFormPortal(BlockState state) {
		return state.is(Blocks.WATER);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		boolean good = world.getBlockState(pos.below()).canOcclude();
		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (!good)
				break;
			BlockState neighboringState = world.getBlockState(pos.relative(facing));
			good = neighboringState.is(Blocks.BLUE_ICE) || neighboringState == state;
		}
		if (!good) {
			world.levelEvent(2001, pos, Block.getId(state));
			world.setBlock(pos, Blocks.WATER.defaultBlockState(), 0b11);
		}
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
		if (state == this.defaultBlockState())
			attemptSendEntity(entity, false, true);
	}

}
