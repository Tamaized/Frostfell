package tamaized.frostfell;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Frostfell.MODID)
@Mod.EventBusSubscriber(modid = Frostfell.MODID)
public class ConfigHandler {

	@Config.Name("Dimension ID")
	public static int dimID = 8;

	@SubscribeEvent
	public static void reload(ConfigChangedEvent event) {
		if (event.getModID().equals(Frostfell.MODID))
			ConfigManager.sync(Frostfell.MODID, Config.Type.INSTANCE);
	}

}
