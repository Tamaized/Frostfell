package tamaized.frostfell.registry;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.world.BiomeFill;

@GameRegistry.ObjectHolder(Frostfell.MODID)
@Mod.EventBusSubscriber(modid = Frostfell.MODID)
public class ModBiomes {

	public static final Biome frozenwasteland = makeNull();

	private static Biome.BiomeProperties makeProperties(String name, String id) {
		return new Biome.BiomeProperties(name).setBaseBiome(Frostfell.MODID + ":" + id);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Biome> event) {
		register(event.getRegistry(), new BiomeFill(makeProperties("Frozen Wasteland", "frozenwasteland").setBaseHeight(1F).setHeightVariation(1.0F)) {
			{
				fillerBlock = Blocks.SNOW.getDefaultState();
				topBlock = Blocks.SNOW.getDefaultState();
			}
		}, "frozenwasteland", BiomeDictionary.Type.COLD);
	}

	private static void register(IForgeRegistry<Biome> registry, Biome biome, String id, BiomeDictionary.Type type) {
		registry.register(biome.setRegistryName(Frostfell.MODID, id));
		BiomeDictionary.addTypes(biome, type);
	}

	private static Biome makeNull() {
		return null;
	}

}
