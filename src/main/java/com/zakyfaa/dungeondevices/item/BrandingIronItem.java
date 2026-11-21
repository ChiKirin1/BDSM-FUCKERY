package com.zakyfaa.dungeondevices.item;

import com.zakyfaa.dungeondevices.registry.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;

/** Applies the effects of a level III Punishment on hit (was `aE`). */
public class BrandingIronItem extends ImplementItem {
	public BrandingIronItem(Settings settings) {
		super(settings, ModSounds.BRANDING_IRON_HIT, null);
	}

	@Override
	protected void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 2, false, false));
		target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2, false, false));
	}
}
