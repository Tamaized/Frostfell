package tamaized.frostfell.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockSound extends Block {

	public BlockSound(Material blockMaterialIn, MapColor blockMapColorIn, SoundType type) {
		super(blockMaterialIn, blockMapColorIn);
		setSoundType(type);
	}
}
