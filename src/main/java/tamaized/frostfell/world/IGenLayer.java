package tamaized.frostfell.world;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public interface IGenLayer {

	GenLayer[] initializeAllBiomeGenerators(long seed, WorldType p_180781_2_);

	Biome[] getAllowedBiomes();

	class GenLayerModded extends GenLayer {

		private final Biome[] allowedBiomes;

		public GenLayerModded(long seed, Biome[] biomes) {
			super(seed);
			allowedBiomes = biomes;
		}

		public GenLayerModded(long seed, Biome[] biomes, GenLayer genlayer) {
			this(seed, biomes);
			this.parent = genlayer;
		}

		@Override
		public int[] getInts(int x, int z, int width, int depth) {
			int[] dest = IntCache.getIntCache(width * depth);
			for (int dz = 0; dz < depth; dz++) {
				for (int dx = 0; dx < width; dx++) {
					this.initChunkSeed(dx + x, dz + z);
					dest[(dx + dz * width)] = Biome.getIdForBiome(this.allowedBiomes[nextInt(this.allowedBiomes.length)]);
				}
			}

			return dest;
		}
	}

}
