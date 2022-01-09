package tamaized.frostfell.world;

import net.minecraft.world.World;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.OverworldGenSettings;
import tamaized.frostfell.registry.ModDimensions;

import java.util.Objects;

public class DimensionFrostfell extends OverworldDimension {

	public DimensionFrostfell(World worldIn, DimensionType typeIn) {
		super(worldIn, typeIn);
	}

	@Override
	public ChunkGenerator<? extends GenerationSettings> createChunkGenerator() {
		OverworldGenSettings genSettings = Objects.requireNonNull(ModDimensions.CHUNKGENTYPE_FROSTFELL).createSettings();
		OverworldBiomeProviderSettings providerSettings = Objects.requireNonNull(ModDimensions.BIOMEPROVIDER_FROSTFELL).createSettings().setWorldInfo(this.world.getWorldInfo()).setGeneratorSettings(genSettings);
		return ModDimensions.CHUNKGENTYPE_FROSTFELL.create(this.world, ModDimensions.BIOMEPROVIDER_FROSTFELL.create(providerSettings), genSettings);
	}

}
