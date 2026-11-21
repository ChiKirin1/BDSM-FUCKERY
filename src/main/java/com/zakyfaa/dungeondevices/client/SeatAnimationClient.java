package com.zakyfaa.dungeondevices.client;

import com.zakyfaa.dungeondevices.DungeonDevices;
import com.zakyfaa.dungeondevices.network.SeatAnimationPayload;
import com.zigythebird.playeranim.PlayerAnimLibMod;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Plays/stops the restraint poses through Player Animation Library (was `d`). */
public final class SeatAnimationClient {
	/** Poses that arrived before the player entity existed on this client. */
	private static final Map<UUID, String> PENDING = new HashMap<>();

	private SeatAnimationClient() {}

	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(SeatAnimationPayload.ID,
			(payload, context) -> context.client().execute(() -> apply(payload.player(), payload.animation())));

		// Apply a pending pose as soon as the player entity shows up.
		ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof AbstractClientPlayerEntity player) {
				String pending = PENDING.remove(player.getUuid());
				if (pending != null) play(player, pending);
			}
		});
	}

	private static void apply(UUID playerId, String animation) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null) return;
		PlayerEntity player = client.world.getPlayerByUuid(playerId);
		if (player instanceof AbstractClientPlayerEntity clientPlayer) {
			play(clientPlayer, animation);
		} else {
			PENDING.put(playerId, animation);
		}
	}

	private static void play(PlayerLikeEntity avatar, String animation) {
		var layer = PlayerAnimationAccess.getPlayerAnimationLayer(avatar, PlayerAnimLibMod.ANIMATION_LAYER_ID);
		if (layer instanceof PlayerAnimationController controller) {
			if (SeatAnimationPayload.RESET.equals(animation)) {
				controller.stopTriggeredAnimation();
			} else {
				controller.triggerAnimation(DungeonDevices.id(animation));
			}
		}
	}
}
