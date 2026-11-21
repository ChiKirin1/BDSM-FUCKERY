package com.zakyfaa.dungeondevices.client;

import com.zakyfaa.dungeondevices.network.VibratorPayload;
import com.zakyfaa.dungeondevices.network.VibratorSoundPayload;
import com.zakyfaa.dungeondevices.registry.ModItems;
import com.zakyfaa.dungeondevices.registry.ModSounds;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** The cursed chastity belt buzzes while its wearer sneaks (was `az.onArmorTick` + `aS`/`aW`). */
public final class VibratorClient {
	private static final Map<UUID, BuzzSound> ACTIVE = new HashMap<>();
	private static boolean lastSent = false;

	private VibratorClient() {}

	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(VibratorSoundPayload.ID,
			(payload, context) -> context.client().execute(() -> handle(payload)));
		ClientTickEvents.END_CLIENT_TICK.register(VibratorClient::tick);
	}

	/** Tell the server when our sneak state (while wearing the belt) changes. */
	private static void tick(MinecraftClient client) {
		ClientPlayerEntity player = client.player;
		if (player == null) {
			lastSent = false;
			return;
		}
		boolean shouldBuzz = player.isSneaking()
			&& player.getEquippedStack(EquipmentSlot.LEGS).isOf(ModItems.CURSED_CHASTITY_BELT);
		if (shouldBuzz != lastSent) {
			ClientPlayNetworking.send(new VibratorPayload(shouldBuzz));
			lastSent = shouldBuzz;
		}
	}

	private static void handle(VibratorSoundPayload payload) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null) return;
		BuzzSound existing = ACTIVE.remove(payload.player());
		if (existing != null) existing.stop();
		if (payload.on()) {
			PlayerEntity player = client.world.getPlayerByUuid(payload.player());
			if (player != null) {
				BuzzSound sound = new BuzzSound(player);
				ACTIVE.put(payload.player(), sound);
				client.getSoundManager().play(sound);
			}
		}
	}

	/** Looping sound that follows the player until stopped. */
	private static final class BuzzSound extends MovingSoundInstance {
		private final PlayerEntity player;
		private boolean stopped = false;

		BuzzSound(PlayerEntity player) {
			super(ModSounds.VIBRATOR, SoundCategory.PLAYERS, SoundInstance.createRandom());
			this.player = player;
			this.repeat = true;
			this.repeatDelay = 0;
			this.volume = 1.0F;
			this.pitch = 1.0F;
			this.x = player.getX();
			this.y = player.getY();
			this.z = player.getZ();
		}

		void stop() { this.stopped = true; }

		@Override
		public void tick() {
			if (stopped || player.isRemoved()) {
				setDone();
				return;
			}
			this.x = player.getX();
			this.y = player.getY();
			this.z = player.getZ();
		}
	}
}
