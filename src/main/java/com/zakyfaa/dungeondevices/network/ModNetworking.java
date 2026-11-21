package com.zakyfaa.dungeondevices.network;

import com.zakyfaa.dungeondevices.entity.SeatEntity;
import com.zakyfaa.dungeondevices.util.SeatSessions;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

/** Registers the packet and re-sends active poses to players who log in or start tracking a seat (was `ak`). */
public final class ModNetworking {
	private ModNetworking() {}

	public static void register() {
		PayloadTypeRegistry.playS2C().register(SeatAnimationPayload.ID, SeatAnimationPayload.CODEC);

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
