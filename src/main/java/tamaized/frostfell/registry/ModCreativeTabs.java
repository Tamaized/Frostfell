package tamaized.frostfell.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import tamaized.frostfell.Frostfell;

public class ModCreativeTabs {

	public static final CreativeTabs TAB = new CreativeTabs(Frostfell.MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.icystone);
		}
	};

}
