package tamaized.frostfell;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.frostfell.common.command.FrostfellCommands;
import tamaized.frostfell.world.WorldProviderFrostfell;

@Mod(Frostfell.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Frostfell {

	public static final String MODID = "frostfell";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public Frostfell() {
		MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
	}

	@SubscribeEvent
	public void init(FMLCommonSetupEvent event) {
		LOGGER.info("Brrrr, it's chilly!");

		DimensionManager.registerDimension(ConfigHandler.dimID, WorldProviderFrostfell.dimType = DimensionType.register("Frostfell", "_frostfell", ConfigHandler.dimID, WorldProviderFrostfell.class, false));

	}

	private void serverStarting(FMLServerStartingEvent evt) {
		evt.getCommandDispatcher().register(

				LiteralArgumentBuilder.<CommandSource>literal("frostfell").
						then(FrostfellCommands.Weather.register())

		);
	}

}
