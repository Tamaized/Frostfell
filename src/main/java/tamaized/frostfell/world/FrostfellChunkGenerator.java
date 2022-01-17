package tamaized.frostfell.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSamplingSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.NoiseSlider;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import tamaized.frostfell.asm.ASMHooks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FrostfellChunkGenerator extends NoiseBasedChunkGenerator {

	public static final Codec<FrostfellChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.
			group(RegistryLookupCodec.create(Registry.NOISE_REGISTRY).
							forGetter((p_188716_) -> p_188716_.noises),

					BiomeSource.CODEC.
							fieldOf("biome_source").
							forGetter(ChunkGenerator::getBiomeSource),

					Codec.LONG.
							fieldOf("seed").orElseGet(() -> ASMHooks.SEED).
							forGetter(gen -> gen.seed),

					NoiseGeneratorSettings.CODEC.
							fieldOf("settings").
							forGetter(FrostfellChunkGenerator::getDimensionSettings)).
			apply(p_236091_0_, p_236091_0_.stable(FrostfellChunkGenerator::new)));

	private final Registry<NormalNoise.NoiseParameters> noises;
	private long seed;

	private FrostfellChunkGenerator(Registry<NormalNoise.NoiseParameters> noiseRegistry, BiomeSource biomeProvider1, long seed, Supplier<NoiseGeneratorSettings> dimensionSettings) {
		super(noiseRegistry, biomeProvider1, seed, fixSettings(dimensionSettings));
		this.noises = noiseRegistry;
		this.seed = seed;
	}

	/**
	 * Lazy load the ASM changes
	 */
	private static Supplier<NoiseGeneratorSettings> fixSettings(Supplier<NoiseGeneratorSettings> settings) {
		return () -> fixSettings(settings.get());
	}

	/**
	 * This is altered via ASM to use {@link CorrectedNoiseSettings} instead of {@link NoiseSettings}
	 */
	private static NoiseGeneratorSettings fixSettings(NoiseGeneratorSettings settings) {
		NoiseSettings s = settings.noiseSettings();
		settings.noiseSettings = new NoiseSettings(s.minY(), s.height(), s.noiseSamplingSettings(), s.topSlideSettings(), s.bottomSlideSettings(), s.noiseSizeHorizontal(), s.noiseSizeVertical(), s.islandNoiseOverride(), s.isAmplified(), s.largeBiomes(), s.terrainShaper());
		return settings;
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return codec;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new FrostfellChunkGenerator(noises, biomeSource.withSeed(seed), seed, getDimensionSettings());
	}

	private Supplier<NoiseGeneratorSettings> getDimensionSettings() {
		return settings;
	}

	/**
	 * Extends {@link NoiseSettings)} via asm
	 */
	@SuppressWarnings("unused")
	private static class CorrectedNoiseSettings {

		private final int noiseSizeHorizontal;

		private CorrectedNoiseSettings(int minY, int height, NoiseSamplingSettings noiseSamplingSettings, NoiseSlider topSlideSettings, NoiseSlider bottomSlideSettings, int noiseSizeHorizontal, int noiseSizeVertical, boolean islandNoiseOverride, boolean isAmplified, boolean largeBiomes, TerrainShaper terrainShaper) {
			this.noiseSizeHorizontal = noiseSizeHorizontal;
		}

		public int getCellWidth() {
			return noiseSizeHorizontal << 1;
		}

	}

}
