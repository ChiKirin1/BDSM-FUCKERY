package com.zakyfaa.dungeondevices.block;

import com.zakyfaa.dungeondevices.blockentity.SeatBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/** Two blocks tall furniture: cross, cage, bondage pole (was `A`). */
public abstract class TallFurnitureBlock extends FurnitureBlock {
	public static final EnumProperty<DoubleBlockHalf> HALF = net.minecraft.state.property.Properties.DOUBLE_BLOCK_HALF;

	protected Map<DoubleBlockHalf, Map<net.minecraft.util.math.Direction, VoxelShape>> shapesByHalf;

	protected TallFurnitureBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState()
			.with(FACING, net.minecraft.util.math.Direction.NORTH)
			.with(OCCUPIED, false)
			.with(FLIPPED, false)
			.with(HALF, DoubleBlockHalf.LOWER));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER ? new SeatBlockEntity(pos, state) : null;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shapesByHalf.get(state.get(HALF)).get(state.get(FACING));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, OCCUPIED, FLIPPED);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos pos = ctx.getBlockPos();
		World world = ctx.getWorld();
		if (pos.getY() < world.getTopYInclusive() && world.getBlockState(pos.up()).canReplace(ctx)) {
			return getDefaultState()
				.with(HALF, DoubleBlockHalf.LOWER)
				.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
				.with(OCCUPIED, false)
				.with(FLIPPED, false);
		}
		return null;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		BlockPos bePos = state.get(HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos;
		// Re-resolve the state at the block-entity position so OCCUPIED reads from the right half.
		return super.onUse(world.getBlockState(bePos), world, bePos, player, hit);
	}

	@Override
	protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		BlockPos other = state.get(HALF) == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		if (world.getBlockState(other).getBlock() == this) {
			world.removeBlock(other, false);
		}
		super.onStateReplaced(state, world, pos, moved);
	}
}
