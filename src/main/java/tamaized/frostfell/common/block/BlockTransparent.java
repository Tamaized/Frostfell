package tamaized.frostfell.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class BlockTransparent extends Block {

	public BlockTransparent(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}


	@Override
	public boolean doesSideBlockRendering(IBlockState state, IWorldReader world, BlockPos pos, EnumFacing face) {
		IBlockState iblockstate = world.getBlockState(pos.offset(face));
		Block block = iblockstate.getBlock();

		if (state != iblockstate) {
			return true;
		}

		if (block == this) {
			return false;
		}

		return super.doesSideBlockRendering(state, world, pos, face);
	}

}
