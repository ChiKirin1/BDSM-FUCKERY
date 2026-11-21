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
			ModBlocks.BONDAGE_POLE
			// The Forge original did not list WEIGHTED_WOODEN_HORSE or WALL_CUFF here. Kept identical.
		).build());

	public static void init() {}
}
