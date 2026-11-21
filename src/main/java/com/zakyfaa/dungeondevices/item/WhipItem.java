package com.zakyfaa.dungeondevices.item;

import com.zakyfaa.dungeondevices.registry.ModSounds;
import org.jetbrains.annotations.Nullable;

/**
 * All whips (was `aI`). Entanglement / Motivation / Punishment / Comforting Hit are
 * datapack enchantments (data/dungeon_devices/enchantment/*.json) that apply their
 * potion effects on hit, so no code is needed here beyond the sound.
 */
public class WhipItem extends ImplementItem {
	public WhipItem(Settings settings, @Nullable String tooltipLine) {
		super(settings, ModSounds.WHIP_HIT, tooltipLine);
	}
}
