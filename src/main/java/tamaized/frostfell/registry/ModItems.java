package tamaized.frostfell.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

public class ModItems implements RegistryClass {

	static final DeferredRegister<Item> REGISTRY = RegUtil.create(ForgeRegistries.ITEMS);

	@Override
	public void init(IEventBus bus) {

	}

}
