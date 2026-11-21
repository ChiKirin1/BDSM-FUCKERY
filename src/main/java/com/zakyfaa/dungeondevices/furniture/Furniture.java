package com.zakyfaa.dungeondevices.furniture;

import com.zakyfaa.dungeondevices.entity.SeatEntity;
import com.zakyfaa.dungeondevices.registry.ModEntities;
import net.minecraft.entity.EntityType;

/** Static per-furniture data: seat offsets, footprint, pose (was `af`). */
public enum Furniture {
	WOODEN_HORSE(FurnitureAnimation.WOODEN_HORSE, FurnitureType.NORMAL, "wooden_horse", 0.5, 0.75, 0.5, 0.0, false),
	WEIGHTED_WOODEN_HORSE(FurnitureAnimation.WEIGHTED_WOODEN_HORSE, FurnitureType.NORMAL, "weighted_wooden_horse", 0.5, 0.75, 0.5, 0.0, false),
	PILLORY(FurnitureAnimation.PILLORY, FurnitureType.NORMAL, "pillory", 0.5, 0.35, 0.5, 0.7, false),
	STRETCH_BENCH(FurnitureAnimation.STRETCH_BENCH, FurnitureType.HORIZONTAL_TRIPLE, "stretch_bench", 0.5, 0.5, 0.5, -0.6, true),
	BONDAGE_BENCH(FurnitureAnimation.BONDAGE_BENCH, FurnitureType.NORMAL, "bondage_bench", 0.5, 0.65, 0.5, 0.25, false),
	CROSS(FurnitureAnimation.CROSS, FurnitureType.VERTICAL_DOUBLE, "cross", 0.5, 0.4, 0.5, 0.25, false),
	CAGE(FurnitureAnimation.CAGE, FurnitureType.VERTICAL_DOUBLE, "cage", 0.5, 0.4, 0.5, 0.0, false),
	CUFF_BED(FurnitureAnimation.CUFF_BED, FurnitureType.HORIZONTAL_DOUBLE, "cuff_bed", 0.5, 0.35, 0.5, 0.3, true),
	BONDAGE_POLE(FurnitureAnimation.BONDAGE_POLE, FurnitureType.VERTICAL_DOUBLE, "bondage_pole", 0.5, 0.35, 0.5, -0.25, false),
	WALL_CUFF(FurnitureAnimation.WALL_CUFF, FurnitureType.NORMAL, "wall_cuff", 0.5, -0.65, 0.5, 0.25, false);

	private final FurnitureAnimation animation;
	private final FurnitureType type;
	private final String blockName;
	private final double offsetX, offsetY, offsetZ;
	private final double generalOffset;
	private final boolean oppositeDirection;

	Furniture(FurnitureAnimation animation, FurnitureType type, String blockName,
	          double offsetX, double offsetY, double offsetZ, double generalOffset, boolean oppositeDirection) {
		this.animation = animation;
		this.type = type;
		this.blockName = blockName;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.generalOffset = generalOffset;
		this.oppositeDirection = oppositeDirection;
	}

	public FurnitureAnimation animation() { return animation; }
	public FurnitureType type() { return type; }
	public String blockName() { return blockName; }
	public String seatName() { return blockName + "_seat"; }
	public double seatX(double blockX) { return offsetX + blockX; }
	/**
	 * 1.18 placed a rider 0.35 blocks below the seat; 1.21 uses the player's vehicle attachment point
	 * (about 0.6 below). The original offsets were tuned for 1.18, so add the difference back.
	 * Tune this one number if players still sit too high/low.
	 */
	public static final double RIDER_HEIGHT_CORRECTION = 0.25;

	public double seatY(double blockY) { return offsetY + blockY + RIDER_HEIGHT_CORRECTION; }
	public double seatZ(double blockZ) { return offsetZ + blockZ; }
	public double generalOffset() { return generalOffset; }
	public boolean oppositeDirection() { return oppositeDirection; }

	public EntityType<SeatEntity> seatEntityType() {
		return switch (this) {
			case WOODEN_HORSE -> ModEntities.WOODEN_HORSE_SEAT;
			case WEIGHTED_WOODEN_HORSE -> ModEntities.WEIGHTED_WOODEN_HORSE_SEAT;
			case PILLORY -> ModEntities.PILLORY_SEAT;
			case STRETCH_BENCH -> ModEntities.STRETCH_BENCH_SEAT;
			case BONDAGE_BENCH -> ModEntities.BONDAGE_BENCH_SEAT;
			case WALL_CUFF -> ModEntities.WALL_CUFF_SEAT;
			case CROSS -> ModEntities.CROSS_SEAT;
			case CAGE -> ModEntities.CAGE_SEAT;
			case CUFF_BED -> ModEntities.CUFF_BED_SEAT;
			case BONDAGE_POLE -> ModEntities.BONDAGE_POLE_SEAT;
		};
	}
}
