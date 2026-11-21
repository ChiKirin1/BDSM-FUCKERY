package com.zakyfaa.dungeondevices.network;

import com.zakyfaa.dungeondevices.DungeonDevices;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/** Client -> server: "I started/stopped sneaking in the cursed chastity belt" (was `aU`). */
public record VibratorPayload(boolean on) implements CustomPayload {
	public static final CustomPayload.Id<VibratorPayload> ID = new CustomPayload.Id<>(DungeonDevices.id("vibrator"));
	public static final PacketCodec<RegistryByteBuf, VibratorPayload> CODEC =
		PacketCodec.tuple(PacketCodecs.BOOLEAN, VibratorPayload::on, VibratorPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
}
