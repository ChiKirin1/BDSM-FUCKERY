package com.zakyfaa.dungeondevices.client;

import com.zakyfaa.dungeondevices.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;

/**
 * Client entrypoint. The seat entities are invisible, but the game still needs a renderer
 * registered for every entity type (the Forge original used an empty renderer `aX`).
 * PHASE 2b: player pose animations, vibrator sound instance, GeckoLib armor renderers.
 */
public class DungeonDevicesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SeatAnimationClient.register();
		VibratorClient.register();
		EntityRendererRegistry.register(ModEntities.WOODEN_HORSE_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.WEIGHTED_WOODEN_HORSE_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.STRETCH_BENCH_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.BONDAGE_BENCH_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.PILLORY_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.CROSS_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.CAGE_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.CUFF_BED_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.BONDAGE_POLE_SEAT, EmptyEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.WALL_CUFF_SEAT, EmptyEntityRenderer::new);
	}
}
