package com.zakyfaa.dungeondevices.network;

import com.zakyfaa.dungeondevices.DungeonDevices;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

/**
 * Server -> client: "play pose `animation` on player `player`" (animation "reset" clears it).
 * Replaces the Forge SimpleChannel packet `aT`.
 */
public record SeatAnimationPayload(UUID player, String animation) implements CustomPayload {
	public static final CustomPayload.Id<SeatAnimationPayload> ID = new CustomPayload.Id<>(DungeonDevices.id("seat_animation"));

	public static final PacketCodec<RegistryByteBuf, SeatAnimationPayload> CODEC = PacketCodec.tuple(
		Uuids.PACKET_CODEC, SeatAnimationPayload::player,
		PacketCodecs.STRING, SeatAnimationPayload::animation,
		SeatAnimationPayload::new);

	public static final String RESET = "reset";

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}
}
