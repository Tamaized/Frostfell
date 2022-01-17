package tamaized.frostfell.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import tamaized.frostfell.asm.ASMHooks;
import tamaized.frostfell.world.genlayer.GenLayerBiomeStabilize;
import tamaized.frostfell.world.genlayer.GenLayerBiomes;
import tamaized.frostfell.world.genlayer.legacy.Area;
import tamaized.frostfell.world.genlayer.legacy.AreaFactory;
import tamaized.frostfell.world.genlayer.legacy.BigContext;
import tamaized.frostfell.world.genlayer.legacy.Layer;
import tamaized.frostfell.world.genlayer.legacy.LazyArea;
import tamaized.frostfell.world.genlayer.legacy.LazyAreaContext;
import tamaized.frostfell.world.genlayer.legacy.ZoomLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.LongFunction;

public class FrostfellSeededBiomeProvider extends BiomeSource {

	public static final List<ResourceKey<Biome>> BIOMES = ImmutableList.of(

			Biomes.SNOWY_TAIGA,

			Biomes.SNOWY_PLAINS

	);
	public static final Codec<FrostfellSeededBiomeProvider> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.LONG.
			fieldOf("seed").stable().orElseGet(() -> ASMHooks.SEED).forGetter((obj) -> obj.seed), RegistryLookupCodec.
			create(Registry.BIOME_REGISTRY).forGetter(provider -> provider.registry)).apply(instance, instance.stable(FrostfellSeededBiomeProvider::new)));
	private final Map<ResourceKey<Biome>, Integer> idCache = new HashMap<>();
	private final Map<Integer, Biome> biomeCache = new HashMap<>();
	private final Registry<Biome> registry;
	private final Layer gen;
	private final long seed;

	public FrostfellSeededBiomeProvider(long seed, Registry<Biome> registryIn) {
		super(BIOMES.stream().map(ResourceKey::location).map(registryIn::getOptional).filter(Optional::isPresent).map(opt -> opt::get));
		this.seed = seed;
		registry = registryIn;
		gen = makeLayers(seed);
	}

	public int getBiomeId(ResourceKey<Biome> biome) {
		Integer id = idCache.get(biome);
		if (id != null)
			return id;
		id = registry.getId(registry.get(biome));
		idCache.put(biome, id);
		return id;
	}

	public Biome getBiome(int id) {
		Biome biome = biomeCache.get(id);
		if (biome != null)
			return biome;
		biome = registry.byId(id);
		if (biome == null)
			throw new IllegalStateException("Unknown biome id emitted by layers: " + id);
		biomeCache.put(id, biome);
		return biome;
	}

	private <T extends Area, C extends BigContext<T>> AreaFactory<T> makeLayers(LongFunction<C> seed) {
		AreaFactory<T> biomes = GenLayerBiomes.INSTANCE.setup(this, BIOMES).run(seed.apply(1L));

		biomes = ZoomLayer.NORMAL.run(seed.apply(1000L), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1001L), biomes);

		biomes = GenLayerBiomeStabilize.INSTANCE.run(seed.apply(700L), biomes);

		biomes = ZoomLayer.NORMAL.run(seed.apply(1002), biomes);

		return biomes;
	}

	public Layer makeLayers(long seed) {
		AreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaContext(25, seed, context));
		return new Layer(areaFactory) {
			@Override
			public Biome get(Registry<Biome> p_242936_1_, int x, int y) {
				return getBiome(area.get(x, y));
			}
		};
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long l) {
		return new FrostfellSeededBiomeProvider(l, registry);
	}

	@Override
	public Biome getNoiseBiome(int x, int cy, int z, Climate.Sampler p_186738_) {
		return getRealNoiseBiome(x, cy << 2, z);
	}

	public static final int[] LAYERS;

	static {
		final int antiSpireY = 32;
		final int thunderSpireY = 192 - antiSpireY;
		final int split = (thunderSpireY - (antiSpireY * 2)) / 3;
		final int sliceBottom = antiSpireY + split;
		final int sliceTop = antiSpireY + split * 2;
		LAYERS = new int[]{antiSpireY, sliceBottom, sliceTop, thunderSpireY};
	}

	public Biome getRealNoiseBiome(int x, int y, int z) {
		// Debug code to render an image of the biome layout within the ide
		/*final java.util.Map<Integer, Integer> remapColors = new java.util.HashMap<>();
		remapColors.put(getBiomeId(Biomes.SNOWY_TAIGA), 0x00FFFF);
		remapColors.put(getBiomeId(Biomes.SNOWY_PLAINS), 0x0077FF);
		final int size = 2048;
		final int rad = size / 2;
		final int ox = 0;
		final int oz = 0;
		java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_RGB);
		java.awt.Graphics2D display = image.createGraphics();
		LazyArea area = gen.area;
		java.util.function.BiPredicate<Integer, Integer> line = (i, mod) -> {
			for (int j = -5; j < 5; j++) {
				if ((i + j) % mod == 0)
					return true;
			}
			return false;
		};
		for (int fx = -rad; fx < rad - 1; fx++) {
			for (int fz = -rad; fz < rad - 1; fz++) {
				int xx = fx + (ox * 64);
				int zz = fz + (oz * 64);
				int c = area.get(fx, fz);
				display.setColor(line.test(xx, 512) || line.test(zz, 512) ? new java.awt.Color(0xFF0000) : new java.awt.Color(remapColors.getOrDefault(c, c)));
				display.drawRect(fx + rad, fz + rad, 1, 1);
			}
		}
 		System.out.println("breakpoint");*/
		return getBiome(gen.area.get(x, z));
	}
}