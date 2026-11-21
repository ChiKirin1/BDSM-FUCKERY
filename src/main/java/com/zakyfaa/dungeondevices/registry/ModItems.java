package com.zakyfaa.dungeondevices.registry;

import com.zakyfaa.dungeondevices.DungeonDevices;
import com.zakyfaa.dungeondevices.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Function;

/**
 * Items and creative tabs (was `au`, `av`, `aw`, `ay`, `aC` and the weapon classes).
 * Damage = (modifier + tier damage), exactly as the 1.18 SwordItem constructor computed it;
 * attack speed is the 1.18 modifier (-3.0 = 1.0 attacks/second).
 * PHASE 2b: shackles and chastity items (GeckoLib armor).
 */
public final class ModItems {
	// ---------- whips ----------            damage speed  durability enchant
	public static final Item LEATHER_WHIP = whip("leather_whip", 7.0, -3.2, 132, 15, Items.LEATHER);
	public static final Item IRON_WHIP = whip("iron_whip", 9.0, -3.1, 256, 15, Items.IRON_INGOT);
	public static final Item DIAMOND_WHIP = whip("diamond_whip", 9.0, -3.0, 1600, 10, Items.DIAMOND);
	public static final Item NETHERITE_WHIP = whip("netherite_whip", 10.0, -3.0, 2031, 15, Items.NETHERITE_INGOT);
	public static final Item CURSED_WHIP = register("cursed_whip",
		key -> weapon(key, 10.0, -3.0, 2031, 15, Items.NETHERITE_INGOT), CursedWhipItem::new);
	public static final Item GOLDEN_WHIP = register("golden_whip",
		key -> weapon(key, 0.0, -2.5, 0, 22, null),
		s -> new WhipItem(s, "Demonstrate some dominance!"));

	// ---------- implements ----------
	public static final Item WOODEN_PADDLE = register("wooden_paddle",
		key -> weapon(key, 0.0, -3.0, 0, 0, null),
		s -> new ImplementItem(s, ModSounds.PADDLE_SLAP, "Demonstrate some less scary dominance"));
	public static final Item RIDING_CROP = register("riding_crop",
		key -> weapon(key, 0.0, -3.0, 0, 0, null),
		s -> new ImplementItem(s, ModSounds.PADDLE_SLAP, null));
	public static final Item CANE = register("cane",
		key -> weapon(key, 0.0, -3.0, 0, 0, null),
		s -> new ImplementItem(s, ModSounds.PADDLE_SLAP, null));
	public static final Item BRANDING_IRON = register("branding_iron",
		key -> weapon(key, 0.0, -3.0, 0, 0, null), BrandingIronItem::new);

	// ---------- restraints (wearable) ----------
	public static final Item CHASTITY_BRA = restraint(RestraintType.CHASTITY_BRA);
	public static final Item CURSED_CHASTITY_BRA = restraint(RestraintType.CURSED_CHASTITY_BRA);
	public static final Item CHASTITY_BELT = restraint(RestraintType.CHASTITY_BELT);
	public static final Item CURSED_CHASTITY_BELT = restraint(RestraintType.CURSED_CHASTITY_BELT);
	public static final Item HAND_SHACKLES = restraint(RestraintType.HAND_SHACKLES);
	public static final Item CURSED_HAND_SHACKLES = restraint(RestraintType.CURSED_HAND_SHACKLES);
	public static final Item ANKLE_SHACKLES = restraint(RestraintType.ANKLE_SHACKLES);
	public static final Item CURSED_ANKLE_SHACKLES = restraint(RestraintType.CURSED_ANKLE_SHACKLES);

	// ---------- plain items ----------
	public static final Item HANDLE = simple("handle");
	public static final Item DUNGEON_PLANK = simple("dungeon_plank");
	public static final Item DUNGEON_BRICK = simple("dungeon_brick");

	/** Items that count as "implements": no knockback, thorns-proof (was `au.a()`). */
	public static final List<Item> IMPLEMENTS = List.of(GOLDEN_WHIP, WOODEN_PADDLE, CANE, BRANDING_IRON, RIDING_CROP);

	public static boolean isImplement(ItemStack stack) {
		return IMPLEMENTS.contains(stack.getItem());
	}

	// ---------- creative tabs ----------
	public static final ItemGroup FURNITURE_GROUP = Registry.register(Registries.ITEM_GROUP,
		DungeonDevices.id("dungeon_devices_tab"),
		FabricItemGroup.builder()
			.icon(() -> new ItemStack(LEATHER_WHIP))
			.displayName(Text.translatable("itemGroup.dungeon_devices_tab"))
			.entries((context, entries) -> {
				ModBlocks.FURNITURE_TAB.forEach(entries::add);
				entries.add(LEATHER_WHIP);
				entries.add(IRON_WHIP);
				entries.add(DIAMOND_WHIP);
				entries.add(NETHERITE_WHIP);
				entries.add(CURSED_WHIP);
				entries.add(GOLDEN_WHIP);
				entries.add(WOODEN_PADDLE);
				entries.add(RIDING_CROP);
				entries.add(CANE);
				entries.add(BRANDING_IRON);
				entries.add(CHASTITY_BRA);
				entries.add(CURSED_CHASTITY_BRA);
				entries.add(CHASTITY_BELT);
				entries.add(CURSED_CHASTITY_BELT);
				entries.add(HAND_SHACKLES);
				entries.add(CURSED_HAND_SHACKLES);
				entries.add(ANKLE_SHACKLES);
				entries.add(CURSED_ANKLE_SHACKLES);
				entries.add(HANDLE);
				entries.add(DUNGEON_PLANK);
				entries.add(DUNGEON_BRICK);
			})
			.build());

	public static final ItemGroup BLOCK_GROUP = Registry.register(Registries.ITEM_GROUP,
		DungeonDevices.id("dungeon_devices_block_tab"),
		FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModBlocks.DUNGEON_BRICKS.get(ModBlocks.Variant.BASE)))
			.displayName(Text.translatable("itemGroup.dungeon_devices_block_tab"))
			.entries((context, entries) -> ModBlocks.BLOCK_TAB.forEach(entries::add))
			.build());

	// ---------- helpers ----------
	private static Item whip(String name, double damage, double speed, int durability, int enchantability, Item repairItem) {
		return register(name, key -> weapon(key, damage, speed, durability, enchantability, repairItem),
			s -> new WhipItem(s, null));
	}

	private static Item.Settings weapon(RegistryKey<Item> key, double damage, double speed, int durability,
	                                    int enchantability, Item repairItem) {
		Item.Settings settings = new Item.Settings()
			.registryKey(key)
			.attributeModifiers(AttributeModifiersComponent.builder()
				.add(EntityAttributes.ATTACK_DAMAGE,
					new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, damage, EntityAttributeModifier.Operation.ADD_VALUE),
					AttributeModifierSlot.MAINHAND)
				.add(EntityAttributes.ATTACK_SPEED,
					new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE),
					AttributeModifierSlot.MAINHAND)
				.build());
		if (durability > 0) settings.maxDamage(durability);
		if (enchantability > 0) settings.enchantable(enchantability);
		if (repairItem != null) settings.repairable(repairItem);
		return settings;
	}

	/**
	 * Wearable restraint. The original was a GeckoLib armor item with a custom armor material:
	 * durability 600/400, protection 3/6/8/3 per slot, enchantability 10, repaired with iron ingots.
	 * Built by hand (no ArmorMaterial) so the per-slot numbers stay exactly as they were.
	 * No equippable "asset" is set, so nothing is drawn until the GeckoLib renderer (phase 2b-2b).
	 */
	private static Item restraint(RestraintType type) {
		return register(type.itemName(), key -> {
			AttributeModifierSlot slot = switch (type.slot()) {
				case HEAD -> AttributeModifierSlot.HEAD;
				case CHEST -> AttributeModifierSlot.CHEST;
				case LEGS -> AttributeModifierSlot.LEGS;
				default -> AttributeModifierSlot.FEET;
			};
			AttributeModifiersComponent.Builder attributes = AttributeModifiersComponent.builder()
				.add(EntityAttributes.ARMOR,
					new EntityAttributeModifier(DungeonDevices.id("restraint_armor_" + type.slot().getName()),
						type.protection(), EntityAttributeModifier.Operation.ADD_VALUE),
					slot);
			if (type.isHandShackles()) {
				// was am.java: -1.5 reach while worn in the chest slot (Extra Tight adds -0.2, see enchantment json)
				attributes.add(EntityAttributes.BLOCK_INTERACTION_RANGE,
					new EntityAttributeModifier(DungeonDevices.id("reach_distance"), -1.5,
						EntityAttributeModifier.Operation.ADD_VALUE),
					slot);
			}
			return new Item.Settings().registryKey(key)
				.maxDamage(type.durability())
				.attributeModifiers(attributes.build())
				.enchantable(10)
				.repairable(Items.IRON_INGOT)
				.component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(type.slot())
					.equipSound(Registries.SOUND_EVENT.getEntry(DungeonDevices.id("lock")).orElseThrow())
					.build());
		}, s -> new RestraintItem(s, type));
	}

	private static <T extends Item> T register(String name, Function<RegistryKey<Item>, Item.Settings> settings,
	                                           Function<Item.Settings, T> factory) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, DungeonDevices.id(name));
		return Registry.register(Registries.ITEM, key, factory.apply(settings.apply(key)));
	}

	private static Item simple(String name) {
		return register(name, key -> new Item.Settings().registryKey(key), Item::new);
	}

	public static void init() {}
}
