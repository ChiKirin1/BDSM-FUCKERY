package com.zakyfaa.dungeondevices.item;

import com.zakyfaa.dungeondevices.event.ModEvents;
import com.zakyfaa.dungeondevices.registry.ModEnchantments;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Chastity items and shackles (was `az`).
 * PHASE 2b-2b: GeckoLib model + animation rendering (the item is wearable but not drawn yet).
 */
public class RestraintItem extends Item {
	private final RestraintType type;

	public RestraintItem(Settings settings, RestraintType type) {
		super(settings);
		this.type = type;
	}

	public RestraintType getRestraintType() { return type; }

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent display,
	                          Consumer<Text> tooltip, TooltipType tooltipType) {
		super.appendTooltip(stack, context, display, tooltip, tooltipType);
		for (String line : type.lore()) {
			tooltip.accept(Text.literal(line));
		}
	}

	/** Ankle shackles keep slowing the wearer; Extra Tight makes it Slowness III instead of I. */
	@Override
	public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
		super.inventoryTick(stack, world, entity, slot);
		if (type.isAnkleShackles() && slot == EquipmentSlot.FEET && entity instanceof PlayerEntity player) {
			int amplifier = ModEvents.hasEnchantment(stack, ModEnchantments.EXTRA_TIGHT) ? 2 : 0;
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, amplifier, false, false));
		}
	}
}
