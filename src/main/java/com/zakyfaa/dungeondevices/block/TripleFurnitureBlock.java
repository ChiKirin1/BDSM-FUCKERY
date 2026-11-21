package com.zakyfaa.dungeondevices.block;

import com.zakyfaa.dungeondevices.blockentity.SeatBlockEntity;
import com.zakyfaa.dungeondevices.furniture.TriplePart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/** Three blocks long furniture, e.g. the stretch bench (was `x`). The block entity lives in the CENTER part. */
public abstract class TripleFurnitureBlock extends FurnitureBlock {
	public static final EnumProperty<TriplePart> PART = EnumProperty.of("part", TriplePart.class);

	protected Map<TriplePart, Map<Direction, VoxelShape>> shapesByPart;

	protected TripleFurnitureBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState()
			.with(FACING, Direction.NORTH)
			.with(OCCUPIED, false)
			.with(FLIPPED, false)
			.with(PART, TriplePart.HEAD));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return state.get(PART) == TriplePart.CENTER ? new SeatBlockEntity(pos, state) : null;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shapesByPart.get(state.get(PART)).get(state.get(FACING));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, PART, OCCUPIED, FLIPPED);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = ctx.getHorizontalPlayerFacing();
		BlockPos origin = ctx.getBlockPos();
		BlockPos centerPos = origin.offset(facing);
		BlockPos headPos = origin.offset(facing, 2);
		World world = ctx.getWorld();
		if (!world.isInBuildLimit(centerPos) || !world.isInBuildLimit(headPos)) return null;
		if (!world.getBlockState(centerPos).canReplace(ctx)) return null;
		if (!world.getBlockState(headPos).canReplace(ctx)) return null;
		return getDefaultState()
			.with(FACING, facing)
			.with(PART, TriplePart.FOOT)
			.with(OCCUPIED, false)
			.with(FLIPPED, false);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		Direction facing = state.get(FACING);
		world.setBlockState(pos.offset(facing), state.with(PART, TriplePart.CENTER), Block.NOTIFY_ALL);
		world.setBlockState(pos.offset(facing, 2), state.with(PART, TriplePart.HEAD), Block.NOTIFY_ALL);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		Direction facing = state.get(FACING);
		BlockPos bePos = switch (state.get(PART)) {
			case HEAD -> pos.offset(facing.getOpposite());
			case CENTER -> pos;
			case FOOT -> pos.offset(facing);
		};
		return super.onUse(world.getBlockState(bePos), world, bePos, player, hit);
	}

	@Override
	protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		Direction facing = state.get(FACING);
		BlockPos foot = switch (state.get(PART)) {
			case HEAD -> pos.offset(facing.getOpposite(), 2);
			case CENTER -> pos.offset(facing.getOpposite());
			case FOOT -> pos;
		};
		for (BlockPos p : new BlockPos[] { foot, foot.offset(facing), foot.offset(facing, 2) }) {
			if (!p.equals(pos) && world.getBlockState(p).getBlock() == this) {
				world.removeBlock(p, false);
			}
		}
		super.onStateReplaced(state, world, pos, moved);
	}
}
