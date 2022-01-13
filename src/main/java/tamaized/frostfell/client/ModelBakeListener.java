package tamaized.frostfell.client;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.registry.ModItems;
import tamaized.regutil.RegUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class ModelBakeListener {

	private static final Map<ResourceLocation, ResourceLocation> REMAPPER = new HashMap<>();

	private static void addBlock(List<ModelResourceLocation> list, RegistryObject<Block> object, String... extra) {
		add(list, object, "", extra);
		add(list, object, "inventory", extra);
	}

	private static void add(List<ModelResourceLocation> list, RegistryObject<Item> object, String... extra) {
		add(list, object, "inventory", extra);
	}

	private static void add(List<ModelResourceLocation> list, RegistryObject<?> object, String loc, String... extra) {
		List<String> extras = new ArrayList<>();
		extras.add("");
		extras.addAll(Arrays.asList(extra));
		String location = REMAPPER.getOrDefault(object.getId(), object.getId()).toString();
		extras.forEach(e -> list.add(new ModelResourceLocation(location.concat(e), loc)));
	}

	public static void init(IEventBus bus) {
		bus.addListener((Consumer<ModelBakeEvent>) event -> {
			List<ModelResourceLocation> fullbrightList = new ArrayList<>();
			List<ModelResourceLocation> overlayList = new ArrayList<>();

			add(fullbrightList, ModItems.PERMAFROST);

			fullbrightList.forEach(mrl -> {
				final BakedModel model = event.getModelRegistry().get(mrl);
				if (model != null)
					event.getModelRegistry().put(mrl, new FullBrightModel(model));
				else
					Frostfell.LOGGER.error("Null Model! " + mrl);
			});
			overlayList.forEach(mrl -> {
				final BakedModel model = event.getModelRegistry().get(mrl);
				if (model != null)
					event.getModelRegistry().put(mrl, new FullBrightModel(model) {
						@Nonnull
						@Override
						public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
							return cachedQuads.computeIfAbsent(side, (face) -> {
								List<BakedQuad> quads = model.getQuads(state, side, rand);
								for (BakedQuad quad : quads)
									if (quads.indexOf(quad) == 1)
										LightUtil.setLightData(quad, 0xF000F0);
								return quads;
							});
						}
					});
				else
					Frostfell.LOGGER.error("Null Model! " + mrl);
			});
		});
	}

	private static void impBroken(Item item) {
		ItemProperties.register(item, new ResourceLocation("broken"), (stack, level, entity, prop) -> RegUtil.ToolAndArmorHelper.isBroken(stack) ? 1F : 0F);
	}

	private static void impBow(Item item) {
		ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, entity, prop) ->

				entity == null ? 0.0F : entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F);

		ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, entity, prop) ->

				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	private static void impXBow(Item item) {
		ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, entity, prop) ->

				entity == null ? 0.0F : CrossbowItem.isCharged(stack) ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack));

		ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, entity, prop) ->

				entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, new ResourceLocation("charged"), (stack, level, entity, prop) ->

				entity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, new ResourceLocation("firework"), (stack, level, entity, prop) ->

				entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
	}

	private static void impShield(Item item) {
		ItemProperties.register(item, new ResourceLocation("blocking"), (stack, level, entity, prop) ->

				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	public static void redirectModels() {

	}

	private static void redirectModelLocation(String subfolder, String remove, Item... items) {
		for (Item item : items) {
			ResourceLocation location = item.getRegistryName();
			if (location == null)
				continue;
			ModelResourceLocation oldMrl = new ModelResourceLocation(location, "inventory");
			ModelBakery bakery = ForgeModelBakery.instance();
			if (bakery == null)
				continue;
			ResourceLocation rl = new ResourceLocation(location.getNamespace(), subfolder.concat("/").concat(location.getPath().replaceFirst(remove, "")));
			ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
			REMAPPER.put(location, rl);
			bakery.loadTopLevel(mrl);
			bakery.unbakedCache.put(oldMrl, bakery.unbakedCache.get(mrl));
			Minecraft.getInstance().getItemRenderer().getItemModelShaper().
					register(item, mrl);
		}
	}

	public static void clearOldModels() {
		ModelBakery bakery = ForgeModelBakery.instance();
		if (bakery == null)
			return;
		REMAPPER.keySet().forEach(location -> {
			ModelResourceLocation oldMrl = new ModelResourceLocation(location, "inventory");
			bakery.unbakedCache.remove(oldMrl);
			bakery.topLevelModels.remove(oldMrl);
		});
	}

	private static class FullBrightModel implements BakedModel {

		private final BakedModel model;
		private final ItemOverrides overrides;
		Map<Direction, List<BakedQuad>> cachedQuads = Maps.newHashMap();

		private FullBrightModel(BakedModel delegate) {
			model = delegate;
			overrides = new FullbrightItemOverrideList(delegate.getOverrides());
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
			return cachedQuads.computeIfAbsent(side, (face) -> {
				List<BakedQuad> quads = model.getQuads(state, side, rand);
				for (BakedQuad quad : quads) {
					LightUtil.setLightData(quad, 0xF000F0);
					quad.shade = false;
				}
				return quads;
			});
		}

		@Override
		public boolean useAmbientOcclusion() {
			return false;//model.useAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return model.isGui3d();
		}

		@Override
		public boolean usesBlockLight() {
			return model.usesBlockLight();
		}

		@Override
		public boolean isCustomRenderer() {
			return model.isCustomRenderer();
		}

		@Nonnull
		@Override
		public TextureAtlasSprite getParticleIcon() {
			return model.getParticleIcon();
		}

		@Nonnull
		@Override
		public ItemOverrides getOverrides() {
			return overrides;
		}

		@Nonnull
		@Override
		@SuppressWarnings("deprecation")
		public net.minecraft.client.renderer.block.model.ItemTransforms getTransforms() {
			return model.getTransforms();
		}

		private static class FullbrightItemOverrideList extends ItemOverrides {

			public FullbrightItemOverrideList(ItemOverrides delegate) {
				properties = delegate.properties;
				List<BakedOverride> overridesList = new ArrayList<>();
				for (BakedOverride override : delegate.overrides) {
					if (override.model != null)
						overridesList.add(new BakedOverride(override.matchers, new FullBrightModel(override.model)));
				}
				overrides = overridesList.toArray(new BakedOverride[0]);
			}

		}


	}
}