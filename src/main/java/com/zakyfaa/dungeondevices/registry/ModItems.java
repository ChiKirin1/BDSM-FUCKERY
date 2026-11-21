package com.zakyfaa.dungeondevices.registry;

import com.zakyfaa.dungeondevices.DungeonDevices;
import com.zakyfaa.dungeondevices.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.type.AttributeModifierSlot;
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
