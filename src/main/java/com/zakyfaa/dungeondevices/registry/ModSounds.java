package com.zakyfaa.dungeondevices.registry;

import com.zakyfaa.dungeondevices.DungeonDevices;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/** Sound events (was `ax`). The files/definitions live in assets/dungeon_devices/sounds.json. */
public final class ModSounds {
	public static final SoundEvent WHIP_HIT = register("whip_hit");
	public static final SoundEvent PADDLE_SLAP = register("paddle_slap");
	public static final SoundEvent BRANDING_IRON_HIT = register("branding_iron_hit");
	public static final SoundEvent VIBRATOR = register("vibrator_sound");
	public static final SoundEvent LOCK = register("lock");

	private static SoundEvent register(String name) {
		Identifier id = DungeonDevices.id(name);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

	public static void init() {}
}
