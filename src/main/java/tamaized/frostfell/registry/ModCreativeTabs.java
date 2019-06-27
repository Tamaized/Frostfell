package tamaized.frostfell.registry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import tamaized.frostfell.Frostfell;

public class ModCreativeTabs {

	public static final ItemGroup TAB = new ItemGroup(Frostfell.MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.icystone);
		}
	};

}
