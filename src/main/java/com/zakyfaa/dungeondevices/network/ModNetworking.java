package com.zakyfaa.dungeondevices.network;

import com.zakyfaa.dungeondevices.entity.SeatEntity;
import com.zakyfaa.dungeondevices.util.SeatSessions;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.zakyfaa.dungeondevices.registry.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.server.network.ServerPlayerEntity;

/** Registers the packet and re-sends active poses to players who log in or start tracking a seat (was `ak`). */
public final class ModNetworking {
	private ModNetworking() {}

	public static void register() {
		PayloadTypeRegistry.playS2C().register(SeatAnimationPayload.ID, SeatAnimationPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(VibratorPayload.ID, VibratorPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(VibratorSoundPayload.ID, VibratorSoundPayload.CODEC);

		// Relay the buzzing sound to everybody nearby. The server re-checks that the sender really wears the belt.
		ServerPlayNetworking.registerGlobalReceiver(VibratorPayload.ID, (payload, context) -> {
			ServerPlayerEntity sender = context.player();
			boolean on = payload.on() && sender.getEquippedStack(EquipmentSlot.LEGS).isOf(ModItems.CURSED_CHASTITY_BELT);
			VibratorSoundPayload out = new VibratorSoundPayload(sender.getUuid(), on);
			ServerPlayNetworking.send(sender, out);
			for (ServerPlayerEntity other : PlayerLookup.tracking(sender)) {
				ServerPlayNetworking.send(other, out);
			}
		});

		// A player joining must learn about everybody who is already restrained.
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
			SeatSessions.all().values().forEach(session -> SeatSessions.sendTo(handler.getPlayer(), session)));

		// A player walking up to a restrained player must see the pose too.
		EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
			if (trackedEntity instanceof SeatEntity seat) {
				SeatSessions.Session session = SeatSessions.all().get(seat.getUuid());
				if (session != null) SeatSessions.sendTo(player, session);
			}
		});
	}
}
