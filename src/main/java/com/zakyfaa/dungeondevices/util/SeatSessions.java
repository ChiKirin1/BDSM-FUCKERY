package com.zakyfaa.dungeondevices.util;

import com.zakyfaa.dungeondevices.furniture.FurnitureAnimation;
import com.zakyfaa.dungeondevices.network.SeatAnimationPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Server-side record of who sits on which seat entity, and the pose they should show (was `b` + `e`). */
public final class SeatSessions {
	public record Session(UUID playerId, String animation) {}

	/** seat entity UUID -> session */
	private static final Map<UUID, Session> SESSIONS = new HashMap<>();

	private SeatSessions() {}

	public static void start(ServerPlayerEntity player, UUID seatEntityId, FurnitureAnimation animation) {
		// The cage has no pose, so it just clears any previous one.
		String name = animation.getId() == null ? SeatAnimationPayload.RESET : animation.getName();
		SESSIONS.put(seatEntityId, new Session(player.getUuid(), name));
		broadcast(player, name);
	}

	public static void reset(PlayerEntity player) {
		if (player instanceof ServerPlayerEntity serverPlayer) {
			broadcast(serverPlayer, SeatAnimationPayload.RESET);
		}
	}

	/** Sends to the player themself and everybody who currently tracks them. */
	private static void broadcast(ServerPlayerEntity player, String animation) {
		SeatAnimationPayload payload = new SeatAnimationPayload(player.getUuid(), animation);
		ServerPlayNetworking.send(player, payload);
		for (ServerPlayerEntity other : PlayerLookup.tracking(player)) {
			ServerPlayNetworking.send(other, payload);
		}
	}

	/** Sends one session to a single player (login / start tracking). */
	public static void sendTo(ServerPlayerEntity receiver, Session session) {
		ServerPlayNetworking.send(receiver, new SeatAnimationPayload(session.playerId(), session.animation()));
	}

	public static Map<UUID, Session> all() { return SESSIONS; }

	public static boolean isPlayerSeated(UUID playerId) {
		return SESSIONS.values().stream().anyMatch(s -> s.playerId().equals(playerId));
	}
}
