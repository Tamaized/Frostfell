package tamaized.frostfell.registry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.common.block.BlockIcicle;
import tamaized.frostfell.common.block.BlockIcyOre;
import tamaized.frostfell.common.block.BlockSound;
import tamaized.frostfell.common.block.BlockTransparent;

import static tamaized.frostfell.registry.ModCreativeTabs.TAB;

@GameRegistry.ObjectHolder(Frostfell.MODID)
@Mod.EventBusSubscriber(modid = Frostfell.MODID)
public class ModBlocks {

	public static final Block icystone = Blocks.AIR;
	public static final Block icyore = Blocks.AIR;
	public static final Block icicle = Blocks.AIR;
	public static final Block icystonebrick = Blocks.AIR;
	public static final Block icebrick = Blocks.AIR;
	public static final Block snowbrick = Blocks.AIR;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(

				assign(new Block(Material.ROCK, MapColor.STONE), "icystone").setHardness(1.5F).setResistance(10.0F),

				assign(new BlockIcyOre(Material.ROCK, MapColor.ICE), "icyore").setHardness(3.0F).setResistance(5.0F),

				assign(new BlockIcicle(Material.ICE, MapColor.ICE), "icicle").setHardness(0.0F).setLightOpacity(3),

				assign(new Block(Material.ROCK, MapColor.STONE), "icystonebrick").setHardness(1.5F).setResistance(10.0F),

				assign(new BlockTransparent(Material.ICE, MapColor.ICE, SoundType.GLASS), "icebrick").setHardness(0.5F),

				assign(new BlockSound(Material.ICE, MapColor.SNOW, SoundType.SNOW), "snowbrick").setHardness(0.5F)

		);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(

				assign(new ItemBlock(icystone), "icystone"),

				assign(new ItemBlock(icyore), "icyore"),

				assign(new ItemBlock(icicle), "icicle"),

				assign(new ItemBlock(icystonebrick), "icystonebrick"),

				assign(new ItemBlock(icebrick), "icebrick"),

				assign(new ItemBlock(snowbrick), "snowbrick")

		);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerModel(icystone, 0);
		registerModel(icyore, 0);
		registerModel(icicle, 0, "inventory");
		registerModel(icystonebrick, 0);
		registerModel(icebrick, 0);
		registerModel(snowbrick, 0);
	}

	private static Block assign(Block block, String name) {
		return block

				.setRegistryName(Frostfell.MODID, name)

				.setTranslationKey(Frostfell.MODID + "." + name)

				.setCreativeTab(TAB);
	}

	private static Item assign(Item item, String name) {
		return item

				.setRegistryName(Frostfell.MODID, name)

				.setTranslationKey(Frostfell.MODID + "." + name)

				.setCreativeTab(TAB);
	}

	@SuppressWarnings("SameParameterValue")
	private static void registerModel(Block block, int meta, String... variant) {
		if (block.getRegistryName() == null)
			return;
		ModelLoader.setCustomModelResourceLocation(

				Item.getItemFromBlock(block),

				meta,

				new ModelResourceLocation(

						new ResourceLocation(block.getRegistryName().getNamespace(), block.getRegistryName().getPath()),

						variant.length > 0 ? variant[0] : "normal"

				)

		);
	}

}
