package tamaized.frostfell;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tamaized.frostfell.client.ClientInitiator;
import tamaized.frostfell.registry.ModBlocks;
import tamaized.frostfell.registry.ModFeatures;
import tamaized.frostfell.registry.ModItems;
import tamaized.regutil.RegUtil;

@Mod(Frostfell.MODID)
public class Frostfell {

	public static final String MODID = "frostfell";

	public Frostfell() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInitiator::call);
		RegUtil.setup(MODID, null, modBus,
				ModBlocks::new,
				ModItems::new,
				ModFeatures::new
		);

	}

}
