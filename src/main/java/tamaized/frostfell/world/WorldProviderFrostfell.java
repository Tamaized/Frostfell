package tamaized.frostfell.world;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderFrostfell extends WorldProvider {

	public static DimensionType dimType;

	@Override
	protected void init() {
		super.init();
		biomeProvider = new BiomeProviderModded(world, new GenLayerFrostfell());
	}

	@Override
	public DimensionType getDimensionType() {
		return dimType;
	}

	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkGeneratorFrostfell(world, world.getSeed());
	}
}
