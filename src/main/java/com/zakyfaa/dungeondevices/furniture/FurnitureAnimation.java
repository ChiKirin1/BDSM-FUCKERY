package com.zakyfaa.dungeondevices.furniture;

import com.zakyfaa.dungeondevices.DungeonDevices;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * Player pose animation played while seated (was `a`).
 * Playback (Player Animation Library) is client side and is added in phase 2;
 * this enum only carries the ids, which match assets/dungeon_devices/player_animation/*.json.
 */
public enum FurnitureAnimation {
	WOODEN_HORSE("wooden_horse"),
	WEIGHTED_WOODEN_HORSE("weighted_wooden_horse"),
	STRETCH_BENCH("stretch_bench"),
	BONDAGE_BENCH("bondage_bench"),
	PILLORY("pillory"),
	CROSS("cross"),
	CAGE(""),
	WALL_CUFF("wall_cuff"),
	CUFF_BED("cuff_bed"),
	BONDAGE_POLE("bondage_pole"),
	/** Sent to clients to clear the pose. */
	RESET("reset");

	private final String name;

	FurnitureAnimation(String name) { this.name = name; }

	public String getName() { return name; }

	/** @return the animation id, or null for the cage (which has no pose). */
	@Nullable
	public Identifier getId() {
		return name.isEmpty() ? null : DungeonDevices.id(name);
	}
}
