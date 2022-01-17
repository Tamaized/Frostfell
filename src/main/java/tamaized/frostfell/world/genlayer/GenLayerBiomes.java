package tamaized.frostfell.world.genlayer;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import tamaized.frostfell.world.FrostfellSeededBiomeProvider;
import tamaized.frostfell.world.genlayer.legacy.AreaTransformer0;
import tamaized.frostfell.world.genlayer.legacy.Context;

import java.util.ArrayList;
import java.util.List;

public enum GenLayerBiomes implements AreaTransformer0 {
	INSTANCE;

	private FrostfellSeededBiomeProvider provider;

	private List<ResourceKey<Biome>> biomes = new ArrayList<>();

	GenLayerBiomes() {

	}

	public GenLayerBiomes setup(FrostfellSeededBiomeProvider provider, List<ResourceKey<Biome>> biomes) {
		this.provider = provider;
		this.biomes = biomes;
		return this;
	}

	@Override
	public int applyPixel(Context iNoiseRandom, int x, int y) {
		return getRandomBiome(iNoiseRandom);
	}

	private int getRandomBiome(Context random) {
		return provider.getBiomeId(biomes.get(random.nextRandom(biomes.size())));
	}
}