package com.zakyfaa.dungeondevices.registry;

import com.zakyfaa.dungeondevices.DungeonDevices;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

/**
 * Enchantments are datapack-driven since 1.21: the definitions are JSON files in
 * data/dungeon_devices/enchantment/. Only the keys are needed in code.
 */
public final class ModEnchantments {
	public static final RegistryKey<Enchantment> COMFORTING_HIT = key("comforting_hit");
	public static final RegistryKey<Enchantment> ENTANGLEMENT = key("entanglement");
	public static final RegistryKey<Enchantment> MOTIVATION = key("motivation");
	public static final RegistryKey<Enchantment> PUNISHMENT = key("punishment");
	public static final RegistryKey<Enchantment> EXTRA_TIGHT = key("extra_tight");

	private static RegistryKey<Enchantment> key(String name) {
		return RegistryKey.of(RegistryKeys.ENCHANTMENT, DungeonDevices.id(name));
	}
}
