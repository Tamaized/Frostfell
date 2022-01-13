package tamaized.frostfell.registry;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.features.TemplateFeature;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModFeatures implements RegistryClass {

	private static final DeferredRegister<Feature<?>> REGISTRY = RegUtil.create(ForgeRegistries.FEATURES);

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> PORTAL = REGISTRY.register("portal", () -> new TemplateFeature<>(NoneFeatureConfiguration.CODEC) {
		private static final ResourceLocation LOCATION = new ResourceLocation(Frostfell.MODID, "portal");
		@Override
		protected StructureTemplate getTemplate(StructureManager templateManager, Random random) {
			return templateManager.getOrCreate(LOCATION);
		}

		@Override
		protected int yLevelOffset() {
			return -6;
		}
	});

	public static final Supplier<PlacedFeature> PLACED_PORTAL = Suppliers.memoize(() -> PORTAL.get().
			configured(NoneFeatureConfiguration.NONE).
			placed(RarityFilter.onAverageOnceEvery(30)));

	private static <T extends PlacementModifier> PlacementModifierType<T> registerPlacementMod(String name, Codec<T> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIERS, new ResourceLocation(Frostfell.MODID, name), () -> codec);
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> ConfiguredFeature<FC, F> registerConfiguredFeature(String loc, ConfiguredFeature<FC, F> feature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Frostfell.MODID, loc), feature);
	}

	@Override
	public void init(IEventBus bus) {
		MinecraftForge.EVENT_BUS.addListener((Consumer<BiomeLoadingEvent>) event -> {
			if (event.getClimate().precipitation == Biome.Precipitation.SNOW)
				event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PLACED_PORTAL.get());
		});
	}

}
