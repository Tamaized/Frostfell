package tamaized.frostfell.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import tamaized.frostfell.Frostfell;

import java.util.function.Supplier;

/**
 * Avoid adding to this class, this is strictly for {@link ClientListener} initiliazation from {@link Frostfell#Frostfell()} via {@link DistExecutor#safeRunWhenOn(Dist, Supplier)}
 */
public class ClientInitiator {

	public static void call() {
		ClientListener.init();
	}

}
