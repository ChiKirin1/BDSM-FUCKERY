package com.zakyfaa.dungeondevices.item;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/** Paddle, cane, riding crop, branding iron: plays a sound on hit (was `aD`). */
public class ImplementItem extends Item {
	private final SoundEvent hitSound;
	@Nullable
	private final String tooltipLine;

	public ImplementItem(Settings settings, SoundEvent hitSound, @Nullable String tooltipLine) {
		super(settings);
		this.hitSound = hitSound;
		this.tooltipLine = tooltipLine;
	}

	@Override
	public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.postHit(stack, target, attacker);
		onHit(stack, target, attacker);
		playSound(target, hitSound);
	}

	/** Extra effect on hit. Comforting Hit etc. are datapack enchantment effects now. */
	protected void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {}

	public static void playSound(LivingEntity at, SoundEvent sound) {
		at.getEntityWorld().playSound(null, at.getBlockPos(), sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent display,
	                          Consumer<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, display, tooltip, type);
		if (tooltipLine != null) {
			tooltip.accept(Text.literal(tooltipLine).formatted(Formatting.LIGHT_PURPLE));
		}
	}
}
