package com.zakyfaa.dungeondevices.event;

import com.zakyfaa.dungeondevices.DungeonDevices;
import com.zakyfaa.dungeondevices.registry.ModEnchantments;
import com.zakyfaa.dungeondevices.registry.ModItems;
import com.zakyfaa.dungeondevices.util.FurnitureUtil;
import com.zakyfaa.dungeondevices.util.SoulPact;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/** Gameplay events (was `aj`, `ak`, `al`, `an`; `am` is now an item attribute, see phase 2b). */
public final class ModEvents {
	private static final Identifier KNOCKBACK_ID = DungeonDevices.id("knockback_resistance");

	private ModEvents() {}

	public static void register() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(ModEvents::allowDamage);
		ServerLivingEntityEvents.AFTER_DEATH.register(ModEvents::afterDeath);

		// A relog / respawn / dimension change always starts with the Soul Pact off.
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof ServerPlayerEntity player) SoulPact.reset(player);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			SoulPact.reset(player);
			FurnitureUtil.release(player);
		});
	}

	private static boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
		// Whips / implements and Entanglement remove knockback from the victim (was `al`).
		EntityAttributeInstance knockback = entity.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE);
		if (knockback != null) {
			knockback.removeModifier(KNOCKBACK_ID);
			if (source.getAttacker() instanceof PlayerEntity attacker) {
				ItemStack held = attacker.getMainHandStack();
				if (ModItems.isImplement(held) || hasEnchantment(held, ModEnchantments.ENTANGLEMENT)) {
					knockback.addTemporaryModifier(new EntityAttributeModifier(KNOCKBACK_ID, 1.0, EntityAttributeModifier.Operation.ADD_VALUE));
				}
			}
		}

		// Holding a harmless implement protects the player from thorns (was `an`).
		if (source.isOf(DamageTypes.THORNS) && entity instanceof PlayerEntity player
			&& ModItems.isImplement(player.getMainHandStack())) {
			return false;
		}
		return true;
	}

	private static void afterDeath(LivingEntity entity, DamageSource source) {
		// Soul Pact: killing a (non-MISC) mob refills the absorption.
		if (source.getAttacker() instanceof PlayerEntity killer && SoulPact.isEnabled(killer)
			&& !entity.getType().getSpawnGroup().equals(SpawnGroup.MISC)) {
			killer.setAbsorptionAmount(10.0F);
		}
		// Easter egg: killing the cat "Zakyotsu" with the netherite whip drops a random cursed restraint (was aj).
		if (entity instanceof CatEntity cat && source.getAttacker() instanceof PlayerEntity attacker
			&& attacker.getMainHandStack().isOf(ModItems.NETHERITE_WHIP)
			&& "Zakyotsu".equals(cat.getName().getString())
			&& cat.getEntityWorld() instanceof ServerWorld serverWorld) {
			ItemStack drop = switch (serverWorld.getRandom().nextInt(4)) {
				case 0 -> new ItemStack(ModItems.CURSED_CHASTITY_BELT);
				case 1 -> new ItemStack(ModItems.CURSED_CHASTITY_BRA);
				case 2 -> new ItemStack(ModItems.CURSED_HAND_SHACKLES);
				default -> new ItemStack(ModItems.CURSED_ANKLE_SHACKLES);
			};
			cat.dropStack(serverWorld, drop);
		}
		// Dying frees the player from any furniture.
		if (entity instanceof PlayerEntity dead) {
			FurnitureUtil.release(dead);
		}
	}

	public static boolean hasEnchantment(ItemStack stack, RegistryKey<Enchantment> key) {
		for (RegistryEntry<Enchantment> entry : stack.getEnchantments().getEnchantments()) {
			if (entry.matchesKey(key)) return true;
		}
		return false;
	}
}
