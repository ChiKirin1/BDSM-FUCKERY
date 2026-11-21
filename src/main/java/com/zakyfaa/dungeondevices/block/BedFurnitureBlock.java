package com.zakyfaa.dungeondevices.block;

import com.zakyfaa.dungeondevices.blockentity.SeatBlockEntity;
import com.zakyfaa.dungeondevices.furniture.DoublePart;
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

/** Two blocks long furniture, e.g. the cuff bed (was `w`). The block entity lives in the FOOT part. */
public abstract class BedFurnitureBlock extends FurnitureBlock {
	public static final EnumProperty<DoublePart> PART = EnumProperty.of("part", DoublePart.class);

	protected Map<DoublePart, Map<Direction, VoxelShape>> shapesByPart;

	protected BedFurnitureBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState()
			.with(FACING, Direction.NORTH)
			.with(OCCUPIED, false)
			.with(FLIPPED, false)
			.with(PART, DoublePart.FOOT));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return state.get(PART) == DoublePart.FOOT ? new SeatBlockEntity(pos, state) : null;
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
		BlockPos headPos = ctx.getBlockPos().offset(facing);
		World world = ctx.getWorld();
		if (!world.isInBuildLimit(headPos)) return null;
		if (!world.getBlockState(headPos).canReplace(ctx)) return null;
		return getDefaultState()
			.with(FACING, facing)
			.with(PART, DoublePart.FOOT)
			.with(OCCUPIED, false)
			.with(FLIPPED, false);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		world.setBlockState(pos.offset(state.get(FACING)), state.with(PART, DoublePart.HEAD), Block.NOTIFY_ALL);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		BlockPos bePos = state.get(PART) == DoublePart.HEAD ? pos.offset(state.get(FACING).getOpposite()) : pos;
		return super.onUse(world.getBlockState(bePos), world, bePos, player, hit);
	}

	@Override
	protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		Direction facing = state.get(FACING);
		BlockPos other = state.get(PART) == DoublePart.FOOT ? pos.offset(facing) : pos.offset(facing.getOpposite());
		BlockState otherState = world.getBlockState(other);
		if (otherState.getBlock() == this && otherState.get(PART) != state.get(PART)) {
			world.removeBlock(other, false);
		}
		super.onStateReplaced(state, world, pos, moved);
	}
}
