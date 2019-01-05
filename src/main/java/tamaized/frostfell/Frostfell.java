package tamaized.frostfell;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.frostfell.world.WorldProviderFrostfell;

@Mod(modid = Frostfell.MODID, version = Frostfell.VERSION, acceptedMinecraftVersions = "[1.12,)")
public class Frostfell {

	public static final String MODID = "frostfell";
	public static final String VERSION = "${version}";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER.info("Brrrr, it's chilly!");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		DimensionManager.registerDimension(ConfigHandler.dimID, WorldProviderFrostfell.dimType = DimensionType.register("Frostfell", "_frostfell", ConfigHandler.dimID, WorldProviderFrostfell.class, false));

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}
