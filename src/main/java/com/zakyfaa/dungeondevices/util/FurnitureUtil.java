package com.zakyfaa.dungeondevices.util;

import com.zakyfaa.dungeondevices.blockentity.SeatBlockEntity;
import com.zakyfaa.dungeondevices.entity.SeatEntity;
import com.zakyfaa.dungeondevices.furniture.Furniture;
import com.zakyfaa.dungeondevices.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/** Misc helpers (was `bb`). */
public final class FurnitureUtil {
	private FurnitureUtil() {}

	public static Optional<Entity> entityById(World world, int id) {
		return Optional.ofNullable(world.getEntityById(id));
	}

	@Nullable
	public static SeatBlockEntity seatBlockEntity(World world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		return be instanceof SeatBlockEntity seat ? seat : null;
	}

	/** Furniture whose occupant can be turned around by right-clicking (the "flip"). */
	public static boolean isFlippable(Block block) {
		return List.of(ModBlocks.WALL_CUFF, ModBlocks.CROSS, ModBlocks.WEIGHTED_WOODEN_HORSE, ModBlocks.WOODEN_HORSE).contains(block);
	}

	public static void setProperty(World world, BooleanProperty property, boolean value, BlockPos... positions) {
		for (BlockPos p : positions) {
			BlockState state = world.getBlockState(p);
			if (state.contains(property)) {
				world.setBlockState(p, state.with(property, value), Block.NOTIFY_ALL);
			}
		}
	}

	@Nullable
	public static Furniture furnitureOf(Block block) {
		String path = Registries.BLOCK.getId(block).getPath();
		for (Furniture f : Furniture.values()) {
			if (f.blockName().equals(path)) return f;
		}
		return null;
	}

	/** Frees a player that is riding one of our seat entities. */
	public static void release(PlayerEntity player) {
		if (player.hasVehicle() && player.getVehicle() instanceof SeatEntity seat) {
			seat.releaseOccupant(player);
		}
	}
}
