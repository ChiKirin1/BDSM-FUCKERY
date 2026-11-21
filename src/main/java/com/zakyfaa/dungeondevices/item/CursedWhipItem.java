package com.zakyfaa.dungeondevices.item;

import com.zakyfaa.dungeondevices.util.SoulPact;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/** Right click toggles the "Soul Pact" (was `aJ`). */
public class CursedWhipItem extends WhipItem {
	public CursedWhipItem(Settings settings) {
		super(settings, null);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient() && user instanceof ServerPlayerEntity player) {
			SoulPact.toggle(player);
		}
		return super.use(world, user, hand);
	}
}
