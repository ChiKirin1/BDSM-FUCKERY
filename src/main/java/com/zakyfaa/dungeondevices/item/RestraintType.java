package com.zakyfaa.dungeondevices.item;

import net.minecraft.entity.EquipmentSlot;

import java.util.List;

/** The eight wearable restraints (was enum `ah`): item name, worn slot, tooltip lines. */
public enum RestraintType {
	CHASTITY_BRA("chastity_bra", EquipmentSlot.HEAD, false, "§dA special push-up bra :3"),
	CURSED_CHASTITY_BRA("cursed_chastity_bra", EquipmentSlot.HEAD, true, "§dA special push-up bra :3"),
	CHASTITY_BELT("chastity_belt", EquipmentSlot.LEGS, false, "§cNo touchie!"),
	CURSED_CHASTITY_BELT("cursed_chastity_belt", EquipmentSlot.LEGS, true,
		"§dIt is said that this belt was owned by a very submissive cat...", "W-what is that buzzing sound??!"),
	HAND_SHACKLES("hand_shackles", EquipmentSlot.CHEST, false, "§dDecreases the victim's range!"),
	CURSED_HAND_SHACKLES("cursed_hand_shackles", EquipmentSlot.CHEST, true, "§dDecreases the victim's range!"),
	ANKLE_SHACKLES("ankle_shackles", EquipmentSlot.FEET, false, "§dSlow the victim's movement speed"),
	CURSED_ANKLE_SHACKLES("cursed_ankle_shackles", EquipmentSlot.FEET, true, "§dSlow the victim's movement speed");

	private final String itemName;
	private final EquipmentSlot slot;
	private final boolean cursed;
	private final List<String> lore;

	RestraintType(String itemName, EquipmentSlot slot, boolean cursed, String... lore) {
		this.itemName = itemName;
		this.slot = slot;
		this.cursed = cursed;
		this.lore = List.of(lore);
	}

	public String itemName() { return itemName; }
	public EquipmentSlot slot() { return slot; }
	public boolean cursed() { return cursed; }
	public List<String> lore() { return lore; }

	public boolean isAnkleShackles() { return this == ANKLE_SHACKLES || this == CURSED_ANKLE_SHACKLES; }
	public boolean isHandShackles() { return this == HAND_SHACKLES || this == CURSED_HAND_SHACKLES; }

	/** Durability per slot from the original armor material (`aA`). */
	public int durability() {
		return switch (slot) {
			case HEAD, LEGS -> 600;
			default -> 400;
		};
	}

	/** Armor points per slot from the original armor material (`aA`). */
	public int protection() {
		return switch (slot) {
			case HEAD, FEET -> 3;
			case LEGS -> 6;
			default -> 8;
		};
	}
}
