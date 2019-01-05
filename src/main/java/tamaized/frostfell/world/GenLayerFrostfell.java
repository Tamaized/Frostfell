package tamaized.frostfell.world;

import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class GenLayerFrostfell implements IGenLayer {

	@Override
	public GenLayer[] initializeAllBiomeGenerators(long seed, WorldType worldType) {
		GenLayer genlayer = new GenLayerModded(1L, getAllowedBiomes());
		genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
		GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
		GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
		GenLayerAddIsland genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
		genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
		genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
		GenLayerAddIsland genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddisland1);
		GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
		genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
		genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
		GenLayerZoom genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
		genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
		GenLayerAddIsland genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
		GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayeraddisland3, 0);
		int i = 4;
		int j = i;

		GenLayer lvt_7_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
		GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, lvt_7_1_);
		GenLayer lvt_9_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		GenLayer genlayer5 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		genlayer5 = GenLayerZoom.magnify(1000L, genlayer5, j);
		GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayer5);

		for (int k = 0; k < i; ++k) {
			genlayer4 = new GenLayerZoom((long) (1000 + k), genlayer4);

			if (k == 0) {
				genlayer4 = new GenLayerAddIsland(3L, genlayer4);
			}

			if (k == 1 || i == 1) {
				genlayer4 = new GenLayerAddIsland(3L, genlayer4);
			}
		}

		GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, genlayer4);
		GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayersmooth1);
		genlayersmooth1.initWorldGenSeed(seed);
		genlayer3.initWorldGenSeed(seed);
		genlayersmooth.initWorldGenSeed(seed);
		return new GenLayer[]{genlayersmooth1, genlayer3, genlayersmooth};
	}

	@Override
	public Biome[] getAllowedBiomes() {
		return new Biome[]{

				Biomes.COLD_BEACH,

				Biomes.COLD_TAIGA,

				Biomes.COLD_TAIGA_HILLS,

				Biomes.MUTATED_TAIGA_COLD,

				Biomes.FROZEN_OCEAN,

				Biomes.FROZEN_RIVER,

				Biomes.ICE_MOUNTAINS,

				Biomes.ICE_PLAINS,

				Biomes.MUTATED_ICE_FLATS,

		};
	}

}
