package tamaized.frostfell.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTransparent extends BlockSound {

	public BlockTransparent(Material blockMaterialIn, MapColor blockMapColorIn, SoundType type) {
		super(blockMaterialIn, blockMapColorIn, type);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (blockState != iblockstate) {
			return true;
		}

		if (block == this) {
			return false;
		}

		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
