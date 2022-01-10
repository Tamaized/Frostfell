package tamaized.frostfell;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tamaized.frostfell.registry.ModBlocks;

@Mod(Frostfell.MODID)
public class Frostfell {

	public static final String MODID = "frostfell";

	public Frostfell() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		ModBlocks.init(modBus);

	}

}
