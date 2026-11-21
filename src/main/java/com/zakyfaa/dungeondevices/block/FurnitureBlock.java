package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.blockentity.SeatBlockEntity;
import com.zakyfaa.dungeondevices.util.FurnitureUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/** Base for single-block furniture (was `z`). */
public abstract class FurnitureBlock extends Block implements BlockEntityProvider {
	public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty OCCUPIED = BooleanProperty.of("occupied");
	public static final BooleanProperty FLIPPED = BooleanProperty.of("flipped");

	/** Hitboxes per facing. Filled by {@link #initShapes()}. */
	protected Map<Direction, VoxelShape> shapes;

	protected FurnitureBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState()
			.with(FACING, Direction.NORTH)
			.with(OCCUPIED, false)
			.with(FLIPPED, false));
	}

	/** Builds the hitboxes (was the obfuscated abstract method `a()`). */
	public abstract void initShapes();

	@Override
	protected abstract MapCodec<? extends FurnitureBlock> getCodec();

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SeatBlockEntity(pos, state);
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) { return state; }

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) { return state; }

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, net.minecraft.block.ShapeContext context) {
		return shapes.get(state.get(FACING));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, OCCUPIED, FLIPPED);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState()
			.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
			.with(OCCUPIED, false)
			.with(FLIPPED, false);
	}

	// Piston behaviour (DESTROY) is set in the block settings, see ModBlocks.

	@Override
	protected boolean hasComparatorOutput(BlockState state) { return true; }

	@Override
	protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
		return state.get(OCCUPIED) ? 15 : 0;
	}

	@Override
	protected boolean emitsRedstonePower(BlockState state) { return true; }

	@Override
	protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(OCCUPIED) ? 15 : 0;
	}

	/**
	 * Right click: seat the clicking player, or (flippable furniture) turn the occupant around.
	 * Behaviour kept identical to the Forge original.
	 */
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient()) {
			SeatBlockEntity be = FurnitureUtil.seatBlockEntity(world, pos);
			if (be != null) {
				if (state.get(OCCUPIED)) {
					if (FurnitureUtil.isFlippable(state.getBlock())) {
						UUID occupant = be.getPlayerUuid();
						PlayerEntity occupantPlayer = occupant == null ? null : world.getPlayerByUuid(occupant);
						if (occupantPlayer != null) {
							be.flip(world, pos, state, occupantPlayer);
							return ActionResult.SUCCESS;
						}
					}
				} else if (!player.isSneaking()) {
					be.seatPlayer(world, pos, state, player);
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.SUCCESS;
	}
	// Releasing the occupant when the block is removed now lives in SeatBlockEntity#onBlockReplaced.
}
