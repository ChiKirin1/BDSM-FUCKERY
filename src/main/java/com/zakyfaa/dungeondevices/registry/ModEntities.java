package com.zakyfaa.dungeondevices.registry;

import com.zakyfaa.dungeondevices.DungeonDevices;
import com.zakyfaa.dungeondevices.entity.SeatEntity;
import com.zakyfaa.dungeondevices.furniture.Furniture;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

/** One invisible seat entity type per furniture (was `at`). */
public final class ModEntities {
	public static final EntityType<SeatEntity> WOODEN_HORSE_SEAT = seat(Furniture.WOODEN_HORSE);
	public static final EntityType<SeatEntity> WEIGHTED_WOODEN_HORSE_SEAT = seat(Furniture.WEIGHTED_WOODEN_HORSE);
	public static final EntityType<SeatEntity> STRETCH_BENCH_SEAT = seat(Furniture.STRETCH_BENCH);
	public static final EntityType<SeatEntity> BONDAGE_BENCH_SEAT = seat(Furniture.BONDAGE_BENCH);
	public static final EntityType<SeatEntity> PILLORY_SEAT = seat(Furniture.PILLORY);
	public static final EntityType<SeatEntity> CROSS_SEAT = seat(Furniture.CROSS);
	public static final EntityType<SeatEntity> CAGE_SEAT = seat(Furniture.CAGE);
	public static final EntityType<SeatEntity> CUFF_BED_SEAT = seat(Furniture.CUFF_BED);
	public static final EntityType<SeatEntity> BONDAGE_POLE_SEAT = seat(Furniture.BONDAGE_POLE);
	public static final EntityType<SeatEntity> WALL_CUFF_SEAT = seat(Furniture.WALL_CUFF);

	private static EntityType<SeatEntity> seat(Furniture furniture) {
		RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, DungeonDevices.id(furniture.seatName()));
		EntityType<SeatEntity> type = EntityType.Builder.<SeatEntity>create(SeatEntity::new, SpawnGroup.MISC)
			.dimensions(0.0F, 0.0F)
			.maxTrackingRange(4)
			.build(key);
		return Registry.register(Registries.ENTITY_TYPE, key, type);
	}

	public static void init() {}
}
