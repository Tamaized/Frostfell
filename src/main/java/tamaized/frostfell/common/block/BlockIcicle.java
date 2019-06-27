package tamaized.frostfell.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import tamaized.frostfell.registry.ModItems;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockIcicle extends Block {

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 3.0D, 2.0D, 14.0D, 16.0D, 14.0D);

	public BlockIcicle(Block.Properties properties) {
		super(properties.sound(SoundType.GLASS).tickRandomly());
	}

	public static boolean canBlockStay(IWorldReaderBase worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos.up());
		return !worldIn.isAirBlock(pos.up()) && (state.getBlockFaceShape(worldIn, pos.up(), EnumFacing.DOWN) == BlockFaceShape.SOLID || state.getMaterial() == Material.LEAVES);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
		return random.nextInt(4);
	}

	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
		return ModItems.iceshard;
	}

	@Override
	public boolean isReplaceable(IBlockState state, BlockItemUseContext useContext) {
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void tick(IBlockState state, World worldIn, BlockPos pos, Random random) {
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
		return super.isValidPosition(state, worldIn, pos) && canBlockStay(worldIn, pos);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!canBlockStay(worldIn, pos)) {
			spawnAsEntity(worldIn, pos, new ItemStack(asItem()));
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

	@Override
	public boolean canSpawnInBlock() {
		return true;
	}

	@Override
	public EnumPushReaction getPushReaction(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}
}
