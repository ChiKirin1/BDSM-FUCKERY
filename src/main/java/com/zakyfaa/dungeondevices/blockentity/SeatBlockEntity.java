package com.zakyfaa.dungeondevices.blockentity;

import com.zakyfaa.dungeondevices.block.FurnitureBlock;
import com.zakyfaa.dungeondevices.entity.SeatEntity;
import com.zakyfaa.dungeondevices.furniture.Furniture;
import com.zakyfaa.dungeondevices.registry.ModBlockEntities;
import com.zakyfaa.dungeondevices.util.FurnitureUtil;
import com.zakyfaa.dungeondevices.util.SeatSessions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/** Stores who is restrained on a piece of furniture and spawns/removes the seat (was `ab`). */
public class SeatBlockEntity extends BlockEntity {
	private int seatEntityId;
	@Nullable
	private UUID playerUuid;

	public SeatBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SEAT, pos, state);
	}

	@Nullable
	public UUID getPlayerUuid() { return playerUuid; }

	public void setPlayerUuid(@Nullable UUID uuid) {
		this.playerUuid = uuid;
		markDirty();
	}

	public int getSeatEntityId() { return seatEntityId; }

	public void setSeatEntityId(int id) {
		this.seatEntityId = id;
		markDirty();
	}

	/** Seats the given player on this furniture. */
	public void seatPlayer(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		Furniture furniture = FurnitureUtil.furnitureOf(state.getBlock());
		if (furniture == null) return;

		Direction facing = state.get(FurnitureBlock.FACING);
		if (furniture.oppositeDirection()) facing = facing.getOpposite();

		SeatEntity seat = new SeatEntity(furniture.seatEntityType(), world);
		double[] p = seatPosition(pos, facing, furniture);
		seat.setPosition(p[0], p[1], p[2]);
		float yaw = yawFor(facing);
		seat.setYaw(yaw);
		player.setYaw(yaw);
		world.spawnEntity(seat);
		seat.setFurniturePos(pos);

		setSeatEntityId(seat.getId());
		setPlayerUuid(player.getUuid());
		setOccupied(world, pos, state, FurnitureBlock.OCCUPIED, true);
		setOccupied(world, pos, state, FurnitureBlock.FLIPPED, false);
		player.startRiding(seat, true, true);

		if (player instanceof ServerPlayerEntity serverPlayer) {
			SeatSessions.start(serverPlayer, seat.getUuid(), furniture.animation());
		}
	}

	/** Turns the restrained player around (right-click on an occupied, flippable piece). */
	public void flip(World world, BlockPos pos, BlockState state, Entity player) {
		Direction facing = state.get(FurnitureBlock.FACING);
		boolean flipped = state.get(FurnitureBlock.FLIPPED);
		float yaw = yawFor(flipped ? facing : facing.getOpposite());
		FurnitureUtil.entityById(world, getSeatEntityId()).ifPresent(e -> {
			if (e instanceof SeatEntity seat) seat.setYaw(yaw);
		});
		setOccupied(world, pos, state, FurnitureBlock.FLIPPED, !flipped);
	}

	/** Called when the occupant leaves: clears OCCUPIED/FLIPPED. */
	public void clearOccupant(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		setOccupied(world, pos, state, FurnitureBlock.OCCUPIED, false);
		setOccupied(world, pos, state, FurnitureBlock.FLIPPED, false);
	}

	/** Frees the player when the furniture is removed (replaces the old Forge onRemove logic in the block). */
	@Override
	public void onBlockReplaced(BlockPos pos, BlockState oldState) {
		super.onBlockReplaced(pos, oldState);
		if (world != null && !world.isClient() && playerUuid != null) {
			PlayerEntity player = world.getPlayerByUuid(playerUuid);
			if (player != null) FurnitureUtil.release(player);
		}
	}

	private static float yawFor(Direction facing) {
		return switch (facing) {
			case EAST -> 270.0F;
			case WEST -> 90.0F;
			case NORTH -> 180.0F;
			default -> 0.0F; // SOUTH, UP, DOWN
		};
	}

	private void setOccupied(World world, BlockPos pos, BlockState state, BooleanProperty property, boolean value) {
		Furniture furniture = FurnitureUtil.furnitureOf(state.getBlock());
		if (furniture == null) return;
		Direction facing = state.get(FurnitureBlock.FACING);
		switch (furniture.type()) {
			case NORMAL -> FurnitureUtil.setProperty(world, property, value, pos);
			case HORIZONTAL_DOUBLE -> FurnitureUtil.setProperty(world, property, value, pos, pos.offset(facing));
			case HORIZONTAL_TRIPLE -> FurnitureUtil.setProperty(world, property, value, pos, pos.offset(facing), pos.offset(facing.getOpposite()));
			case VERTICAL_DOUBLE -> FurnitureUtil.setProperty(world, property, value, pos, pos.up());
		}
	}

	private double[] seatPosition(BlockPos pos, Direction facing, Furniture furniture) {
		double x = furniture.seatX(pos.getX());
		double y = furniture.seatY(pos.getY());
		double z = furniture.seatZ(pos.getZ());
		double off = furniture.generalOffset();
		switch (facing) {
			case EAST -> x -= off;
			case WEST -> x += off;
			case SOUTH -> z -= off;
			case NORTH -> z += off;
			default -> {}
		}
		return new double[] { x, y, z };
	}

	@Override
	protected void writeData(WriteView view) {
		super.writeData(view);
		if (playerUuid != null) {
			view.putInt("SeatEntityID", seatEntityId);
			view.put("PlayerUUID", Uuids.INT_STREAM_CODEC, playerUuid);
		}
	}

	@Override
	protected void readData(ReadView view) {
		super.readData(view);
		// NOTE: the Forge original checked containsUuid("SeatEntityID"), which never matched (bug),
		// so the seat id was never restored. Fixed here.
		seatEntityId = view.getInt("SeatEntityID", 0);
		playerUuid = view.read("PlayerUUID", Uuids.INT_STREAM_CODEC).orElse(null);
	}
}
