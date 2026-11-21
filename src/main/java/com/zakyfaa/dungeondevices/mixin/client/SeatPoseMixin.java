package com.zakyfaa.dungeondevices.mixin.client;

import com.zakyfaa.dungeondevices.entity.SeatEntity;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The Forge original overrode Entity#shouldRiderSit (-> false) on the seat so riders did not use the
 * vanilla sitting pose. Fabric has no such hook, so clear the render flag for our seats instead.
 */
@Mixin(BipedEntityRenderer.class)
public abstract class SeatPoseMixin {
	@Inject(method = "updateBipedRenderState", at = @At("TAIL"))
	private static void dungeon_devices$noSeatedPose(LivingEntity entity, BipedEntityRenderState state,
	                                                 float tickProgress, ItemModelManager itemModelManager,
	                                                 CallbackInfo ci) {
		if (entity.getVehicle() instanceof SeatEntity) {
			state.hasVehicle = false;
		}
	}
}
