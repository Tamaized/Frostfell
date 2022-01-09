package tamaized.frostfell.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

public class BlockTransparent extends Block {

	public BlockTransparent(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
		BlockState iblockstate = world.getBlockState(pos.offset(face));
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
