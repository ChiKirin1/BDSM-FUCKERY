package com.zakyfaa.dungeondevices.network;

import com.zakyfaa.dungeondevices.DungeonDevices;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

/** Server -> clients: start/stop the looping buzzing sound on a player (was `aS`). */
public record VibratorSoundPayload(UUID player, boolean on) implements CustomPayload {
	public static final CustomPayload.Id<VibratorSoundPayload> ID = new CustomPayload.Id<>(DungeonDevices.id("vibrator_sound"));
	public static final PacketCodec<RegistryByteBuf, VibratorSoundPayload> CODEC = PacketCodec.tuple(
		Uuids.PACKET_CODEC, VibratorSoundPayload::player,
		PacketCodecs.BOOLEAN, VibratorSoundPayload::on,
		VibratorSoundPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
}
