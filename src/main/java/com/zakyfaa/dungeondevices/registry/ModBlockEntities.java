package com.zakyfaa.dungeondevices.registry;

import com.zakyfaa.dungeondevices.DungeonDevices;
import com.zakyfaa.dungeondevices.blockentity.SeatBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/** Block entity types (was `ao`). */
public final class ModBlockEntities {
	public static final BlockEntityType<SeatBlockEntity> SEAT = Registry.register(
		Registries.BLOCK_ENTITY_TYPE,
		DungeonDevices.id("seat_block_entity"),
		FabricBlockEntityTypeBuilder.create(SeatBlockEntity::new,
			ModBlocks.WOODEN_HORSE,
			ModBlocks.BONDAGE_BENCH,
			ModBlocks.STRETCH_BENCH,
			ModBlocks.PILLORY,
			ModBlocks.CAGE,
			ModBlocks.CROSS,
			ModBlocks.CUFF_BED,
			ModBlocks.BONDAGE_POLE,
			// The Forge 1.18 original forgot these two, which Forge tolerated. 1.21 validates it
			// (crash: "Invalid block entity ... got weighted_wooden_horse"), so they are listed now.
			ModBlocks.WEIGHTED_WOODEN_HORSE,
			ModBlocks.WALL_CUFF
		).build());

	public static void init() {}
}
