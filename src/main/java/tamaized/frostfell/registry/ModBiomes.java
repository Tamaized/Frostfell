package tamaized.frostfell.registry;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import tamaized.frostfell.Frostfell;

@Mod.EventBusSubscriber(modid = Frostfell.MODID)
public class ModBiomes {

	private static Biome.BiomeProperties makeProperties(String name, String id) {
		return new Biome.BiomeProperties(name).setBaseBiome(Frostfell.MODID + ":" + id);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Biome> event) {

	}

	private static void register(IForgeRegistry<Biome> registry, Biome biome, String id, BiomeDictionary.Type type) {
		registry.register(biome.setRegistryName(Frostfell.MODID, id));
		BiomeDictionary.addTypes(biome, type);
	}

}
