package tamaized.frostfell.common.block;

import net.minecraft.block.Block;

public class BlockIcyOre extends Block {

	public BlockIcyOre(Block.Properties properties) {
		super(properties);
	}

	/*@Override
	public IItemProvider getItemDropped(BlockState state, World worldIn, BlockPos pos, int fortune) {
		return ModItems.permafrost;
	}

	@Override
	public int getItemsToDropCount(BlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
		return 1 + random.nextInt(5 + fortune);
	}

	@Override
	public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune) {
		return (world instanceof World ? ((World) world).rand : new Random()).nextInt(3 + fortune);
	}*/
}
