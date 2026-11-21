package com.zakyfaa.dungeondevices.util;

import com.zakyfaa.dungeondevices.furniture.DoublePart;
import com.zakyfaa.dungeondevices.furniture.TriplePart;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.EnumMap;
import java.util.Map;

/** Hitbox helpers (was `bc` = ShapeBuilder and `be` = rotation helper). */
public final class ShapeUtil {
	private ShapeUtil() {}

	/** Collects pixel-space (0-16) cuboids into one shape. */
	public static final class Builder {
		private VoxelShape shape = VoxelShapes.empty();

		public Builder box(double x1, double y1, double z1, double x2, double y2, double z2) {
			shape = VoxelShapes.union(shape, Block.createCuboidShape(x1, y1, z1, x2, y2, z2));
			return this;
		}

		public VoxelShape build() { return shape; }
	}

	public static Map<Direction, VoxelShape> rotations(VoxelShape north) {
		Map<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
		map.put(Direction.NORTH, north);
		map.put(Direction.EAST, rotate(north, 90));
		map.put(Direction.SOUTH, rotate(north, 180));
		map.put(Direction.WEST, rotate(north, 270));
		return map;
	}

	public static Map<DoublePart, Map<Direction, VoxelShape>> rotations(VoxelShape head, VoxelShape foot) {
		Map<DoublePart, Map<Direction, VoxelShape>> map = new EnumMap<>(DoublePart.class);
		map.put(DoublePart.HEAD, rotations(head));
		map.put(DoublePart.FOOT, rotations(foot));
		return map;
	}

	public static Map<TriplePart, Map<Direction, VoxelShape>> rotations(VoxelShape head, VoxelShape center, VoxelShape foot) {
		Map<TriplePart, Map<Direction, VoxelShape>> map = new EnumMap<>(TriplePart.class);
		map.put(TriplePart.HEAD, rotations(head));
		map.put(TriplePart.CENTER, rotations(center));
		map.put(TriplePart.FOOT, rotations(foot));
		return map;
	}

	public static Map<DoubleBlockHalf, Map<Direction, VoxelShape>> rotationsByHalf(VoxelShape lower, VoxelShape upper) {
		Map<DoubleBlockHalf, Map<Direction, VoxelShape>> map = new EnumMap<>(DoubleBlockHalf.class);
		map.put(DoubleBlockHalf.LOWER, rotations(lower));
		map.put(DoubleBlockHalf.UPPER, rotations(upper));
		return map;
	}

	/** Rotates a shape clockwise around the block centre in 90 degree steps. */
	public static VoxelShape rotate(VoxelShape shape, int degrees) {
		int times = degrees / 90 % 4;
		VoxelShape[] buffer = new VoxelShape[] { shape, VoxelShapes.empty() };
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
				buffer[1] = VoxelShapes.union(buffer[1],
					VoxelShapes.cuboid(1.0 - maxZ, minY, minX, 1.0 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}
		return buffer[0];
	}
}
