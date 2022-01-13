package tamaized.frostfell.registry;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

public class ModItems implements RegistryClass {

	static class ItemProps {
		static final RegUtil.ItemProps DEFAULT = new RegUtil.ItemProps(() -> new Item.Properties().tab(RegUtil.creativeTab()));
		static final RegUtil.ItemProps PERMAFROST = new RegUtil.ItemProps(() -> DEFAULT.properties().get().fireResistant());
	}

	static final DeferredRegister<Item> REGISTRY = RegUtil.create(ForgeRegistries.ITEMS);

	public static final RegistryObject<Item> PERMAFROST = REGISTRY.register("permafrost", () -> new Item(ItemProps.PERMAFROST.properties().get()));

	@Override
	public void init(IEventBus bus) {

	}

}
