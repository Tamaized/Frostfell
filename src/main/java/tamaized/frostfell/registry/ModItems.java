package tamaized.frostfell.registry;


import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tamaized.frostfell.Frostfell;

import static tamaized.frostfell.registry.ModCreativeTabs.TAB;

@SuppressWarnings({"unused", "SameParameterValue"})
@ObjectHolder(Frostfell.MODID)
@Mod.EventBusSubscriber(modid = Frostfell.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

	public static final Item permafrost = Items.AIR;
	public static final Item iceshard = Items.AIR;

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(

				assign(new Item(new Item.Properties().group(TAB)), "permafrost"),

				assign(new Item(new Item.Properties().group(TAB)), "iceshard")

		);
	}

	/*@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerModel(permafrost, 0, "");
		registerModel(iceshard, 0, "");
	}*/

	private static Item assign(Item item, String name) {
		return item

				.setRegistryName(Frostfell.MODID, name);
	}

	/*private static void registerModel(Item item, int meta, String path) {
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
	}*/
}
