package tamaized.frostfell.registry;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tamaized.frostfell.Frostfell;

import static tamaized.frostfell.registry.ModCreativeTabs.TAB;

@SuppressWarnings({"unused", "SameParameterValue"})
@GameRegistry.ObjectHolder(Frostfell.MODID)
@Mod.EventBusSubscriber(modid = Frostfell.MODID)
public class ModItems {

	public static final Item permafrost = Items.AIR;
	public static final Item iceshard = Items.AIR;

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(

				assign(new Item(), "permafrost"),

				assign(new Item(), "iceshard")

		);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerModel(permafrost, 0, "");
		registerModel(iceshard, 0, "");
	}

	private static Item assign(Item item, String name) {
		return item

				.setRegistryName(Frostfell.MODID, name)

				.setTranslationKey(Frostfell.MODID + "." + name)

				.setCreativeTab(TAB);
	}

	private static void registerModel(Item item, int meta, String path) {
		if (item.getRegistryName() == null)
			return;
		ModelLoader.setCustomModelResourceLocation(

				item,

				meta,

				new ModelResourceLocation(

						new ResourceLocation(item.getRegistryName().getNamespace(), path + item.getRegistryName().getPath()),

						"inventory"

				)

		);
	}
}
