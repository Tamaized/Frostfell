package tamaized.frostfell.world;

import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
import tamaized.frostfell.registry.ModDimensions;

public class DimensionFrostfell extends OverworldDimension {

	@Override
	public IChunkGenerator<? extends IChunkGenSettings> createChunkGenerator() {
		OverworldGenSettings genSettings = ModDimensions.CHUNKGENTYPE_FROSTFELL.createSettings();
		OverworldBiomeProviderSettings providerSettings = ModDimensions.BIOMEPROVIDER_FROSTFELL.createSettings().setWorldInfo(this.world.getWorldInfo()).setGeneratorSettings(genSettings);
		return ModDimensions.CHUNKGENTYPE_FROSTFELL.create(this.world,  ModDimensions.BIOMEPROVIDER_FROSTFELL.create(providerSettings), genSettings);
	}

}
