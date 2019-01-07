package tamaized.frostfell.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamaized.frostfell.registry.ModItems;

import java.util.Random;

public class BlockIcyOre extends Block {

	public BlockIcyOre(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.permafrost;
	}

	@Override
	public int quantityDropped(Random random) {
		return 1 + random.nextInt(5);
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return super.quantityDroppedWithBonus(fortune, random) + random.nextInt(fortune + 1);
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		return (world instanceof World ? ((World) world).rand : new Random()).nextInt(3);
	}
}
