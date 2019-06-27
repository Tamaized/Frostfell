package tamaized.frostfell.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import tamaized.frostfell.registry.ModItems;

import java.util.Random;

public class BlockIcyOre extends Block {

	public BlockIcyOre(Block.Properties properties) {
		super(properties);
	}

	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
		return ModItems.permafrost;
	}

	@Override
	public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
		return 1 + random.nextInt(5 + fortune);
	}

	@Override
	public int getExpDrop(IBlockState state, IWorldReader world, BlockPos pos, int fortune) {
		return (world instanceof World ? ((World) world).rand : new Random()).nextInt(3 + fortune);
	}
}
