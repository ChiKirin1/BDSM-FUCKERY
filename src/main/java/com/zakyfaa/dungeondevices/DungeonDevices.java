package com.zakyfaa.dungeondevices;

import com.zakyfaa.dungeondevices.event.ModEvents;
import com.zakyfaa.dungeondevices.network.ModNetworking;
import com.zakyfaa.dungeondevices.registry.ModBlockEntities;
import com.zakyfaa.dungeondevices.registry.ModBlocks;
import com.zakyfaa.dungeondevices.registry.ModEntities;
import com.zakyfaa.dungeondevices.registry.ModItems;
import com.zakyfaa.dungeondevices.registry.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

/**
 * Dungeon Devices - Fabric port.
 * Original mod (Forge 1.18.2) by Faa and Zaky. Ported to Fabric 1.21.11 by Claude (Anthropic).
 */
public class DungeonDevices implements ModInitializer {
	public static final String MOD_ID = "dungeon_devices";

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		// Order matters: blocks -> items (block items) -> block entities -> entities
		ModSounds.init();
		ModBlocks.init();
		ModItems.init();
		ModBlockEntities.init();
		ModEntities.init();
		ModEvents.register();
		ModNetworking.register();
	}
}
