package tamaized.frostfell.client;

import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.frostfell.registry.ModBlocks;

import java.util.function.Consumer;

public final class TintHandler {

	private TintHandler() {

	}

	public static void setup(IEventBus bus) {
		bus.addListener((Consumer<ColorHandlerEvent.Block>) event -> {
			event.getBlockColors().register((state, tintGetter, pos, color) -> 0x44DDFF, ModBlocks.PORTAL.get());
		});
	}

}
