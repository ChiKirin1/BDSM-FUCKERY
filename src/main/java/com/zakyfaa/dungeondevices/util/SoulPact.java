package com.zakyfaa.dungeondevices.util;

import com.zakyfaa.dungeondevices.DungeonDevices;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The cursed whip's "Soul Pact": -10 max health, +10 absorption and permanent fire resistance.
 * (Was a Forge capability `E`/`F`/`aq`/`ar`. The state was reset on every join/leave anyway,
 * so it is just an in-memory set here, no save data needed.)
 */
public final class SoulPact {
	private static final Identifier MODIFIER_ID = DungeonDevices.id("max_health_modifier");
	private static final Set<UUID> ENABLED = ConcurrentHashMap.newKeySet();

	private SoulPact() {}

	public static boolean isEnabled(PlayerEntity player) {
		return ENABLED.contains(player.getUuid());
	}

	public static void toggle(ServerPlayerEntity player) {
		set(player, !isEnabled(player));
	}

	public static void set(ServerPlayerEntity player, boolean enable) {
		EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
		if (maxHealth == null) return;

		if (enable) {
			if (isEnabled(player)) return;
			if (player.getHealth() != player.getMaxHealth()) {
				player.sendMessage(Text.translatable("text.cursed_whip_effect.need_max_health").formatted(Formatting.RED), false);
				return;
			}
			if (!maxHealth.hasModifier(MODIFIER_ID)) {
				maxHealth.addTemporaryModifier(new EntityAttributeModifier(MODIFIER_ID, -10.0, EntityAttributeModifier.Operation.ADD_VALUE));
				player.setAbsorptionAmount(10.0F);
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, StatusEffectInstance.INFINITE, 0, true, false));
				player.sendMessage(Text.translatable("text.cursed_whip_effect.on").formatted(Formatting.YELLOW), false);
				ENABLED.add(player.getUuid());
			}
		} else if (maxHealth.hasModifier(MODIFIER_ID)) {
			maxHealth.removeModifier(MODIFIER_ID);
			player.setAbsorptionAmount(0.0F);
			player.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
			player.sendMessage(Text.translatable("text.cursed_whip_effect.off").formatted(Formatting.YELLOW), false);
			ENABLED.remove(player.getUuid());
		}
	}

	/** Silent disable used on join/leave (the pact never survives a relog). */
	public static void reset(ServerPlayerEntity player) {
		if (isEnabled(player)) {
			EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
			if (maxHealth != null) maxHealth.removeModifier(MODIFIER_ID);
			player.setAbsorptionAmount(0.0F);
			player.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
		}
		ENABLED.remove(player.getUuid());
	}
}
