package tamaized.frostfell.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import tamaized.frostfell.client.world.FrostfellWeatherRenderer;

import java.lang.reflect.Field;

public class WorldProviderFrostfell extends WorldProvider {

	public static DimensionType dimType;

	private static Field FIELD_worldInfo;

	@Override
	protected void init() {
		super.init();
		if (!world.isRemote) {
			if (FIELD_worldInfo == null) {
				FIELD_worldInfo = ObfuscationReflectionHelper.findField(World.class, "field_72986_A");
				FIELD_worldInfo.setAccessible(true);
			}
			try {
				FIELD_worldInfo.set(world, new WorldInfoFrostfell(world.getWorldInfo()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		biomeProvider = new BiomeProviderModded(world, new GenLayerFrostfell());
		if (world.isRemote)
			setWeatherRenderer(new FrostfellWeatherRenderer());
	}

	@Override
	public DimensionType getDimensionType() {
		return dimType;
	}

	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkGeneratorFrostfell(world, world.getSeed());
	}

	@Override
	public boolean canBlockFreeze(BlockPos pos, boolean byWater) {
		if (pos.getY() >= 0 && pos.getY() < 256) {
			IBlockState iblockstate1 = world.getBlockState(pos);
			Block block = iblockstate1.getBlock();

			if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
				if (!byWater) {
					return true;
				}

				boolean flag = isWater(pos.west()) && isWater(pos.east()) && isWater(pos.north()) && isWater(pos.south());

				return !flag;
			}
		}

		return false;
	}

	private boolean isWater(BlockPos pos) {
		return world.getBlockState(pos).getMaterial() == Material.WATER;
	}

	@Override
	public boolean canSnowAt(BlockPos pos, boolean checkLight) {
		if (pos.getY() >= 0 && pos.getY() < 256) {
			IBlockState iblockstate1 = world.getBlockState(pos);
			return iblockstate1.getBlock().isAir(iblockstate1, world, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos);
		}

		return false;
	}

}
