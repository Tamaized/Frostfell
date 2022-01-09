package tamaized.frostfell.registry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.common.block.BlockIcicle;
import tamaized.frostfell.common.block.BlockIcyOre;
import tamaized.frostfell.common.block.BlockTransparent;

import java.util.Objects;

@ObjectHolder(Frostfell.MODID)
@Mod.EventBusSubscriber(modid = Frostfell.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

				assign(new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 10.0F)), "icystone"),

				assign(new BlockIcyOre(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(3.0F, 5.0F)), "icyore"),

				assign(new BlockIcicle(Block.Properties.create(Material.ICE, MaterialColor.ICE).hardnessAndResistance(0.0F, 3)), "icicle"),

				assign(new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 10.0F)), "icystonebrick"),

				assign(new BlockTransparent(Block.Properties.create(Material.ICE, MaterialColor.ICE).hardnessAndResistance(0.5F).sound(SoundType.GLASS)), "icebrick"),

				assign(new Block(Block.Properties.create(Material.ICE, MaterialColor.SNOW).hardnessAndResistance(0.5F).sound(SoundType.SNOW)), "snowbrick")

		);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(

				assign(icystone),

				assign(icyore),

				assign(icicle),

				assign(icystonebrick),

				assign(icebrick),

				assign(snowbrick)

		);
	}

	/*@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerModel(icystone, 0);
		registerModel(icyore, 0);
		registerModel(icicle, 0, "inventory");
		registerModel(icystonebrick, 0);
		registerModel(icebrick, 0);
		registerModel(snowbrick, 0);
	}*/

	private static BlockItem assign(Block block) {
		return (BlockItem) new BlockItem(block, new Item.Properties().group(ModCreativeTabs.TAB)).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
	}

	private static Block assign(Block block, String name) {
		return block

				.setRegistryName(Frostfell.MODID, name);
	}

	private static Item assign(Item item, String name) {
		return item

				.setRegistryName(Frostfell.MODID, name);
	}

	/*@SuppressWarnings("SameParameterValue")
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
	}*/

}
