package tamaized.frostfell.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class BlockIcicle extends Block {

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 3.0D, 2.0D, 14.0D, 16.0D, 14.0D);

	public BlockIcicle(Block.Properties properties) {
		super(properties.sound(SoundType.GLASS).tickRandomly());
	}

	public static boolean canBlockStay(IWorldReader worldIn, BlockPos pos) {
		BlockState state = worldIn.getBlockState(pos.up());
		return !worldIn.isAirBlock(pos.up()) && (state.func_215702_a(worldIn, pos.up(), Direction.DOWN) == VoxelShapes.fullCube() || state.getMaterial() == Material.LEAVES);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
		super.neighborChanged(p_220069_1_, p_220069_2_, p_220069_3_, p_220069_4_, p_220069_5_, p_220069_6_);
	}

	/*@Override
	public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, world, pos, neighbor);
		this.checkAndDropBlock(world, pos, state);
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		this.checkAndDropBlock(worldIn, pos, state);
	}*/

	/*@Override
	public boolean isValidPosition(BlockState state, IWorldReaderBase worldIn, BlockPos pos) {
		return super.isValidPosition(state, worldIn, pos) && canBlockStay(worldIn, pos);
	}

	protected void checkAndDropBlock(IWorldReader worldIn, BlockPos pos, BlockState state) {
		if (!canBlockStay(worldIn, pos)) {
			spawnAsEntity(worldIn, pos, new ItemStack(asItem()));
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}*/

	/*@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}*/

	/*@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}*/

	/*@Override
	public boolean causesSuffocation(BlockState state) {
		return false;
	}*/

	@Override
	public boolean canSpawnInBlock() {
		return true;
	}

	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}
}
