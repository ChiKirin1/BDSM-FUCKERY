package com.zakyfaa.dungeondevices.entity;

import com.zakyfaa.dungeondevices.blockentity.SeatBlockEntity;
import com.zakyfaa.dungeondevices.registry.ModEntities;
import com.zakyfaa.dungeondevices.util.FurnitureUtil;
import com.zakyfaa.dungeondevices.util.SeatSessions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/** Invisible entity the restrained player rides (was `ad`). */
public class SeatEntity extends Entity {
	@Nullable
	private BlockPos blockPos;
	private boolean released = false;

	public SeatEntity(EntityType<? extends SeatEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		if (!getEntityWorld().isClient()) updateFurnitureNeighbors();
	}

	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		// Replaces the Forge EntityMountEvent "dismounting" hook: leaving the seat frees the occupant.
		if (!getEntityWorld().isClient() && passenger instanceof PlayerEntity player) {
			releaseOccupant(player);
		}
		if (!getEntityWorld().isClient() && getPassengerList().isEmpty()) {
			updateFurnitureNeighbors();
			if (passenger instanceof LivingEntity living) {
				Vec3d target = updatePassengerForDismount(living);
				passenger.requestTeleport(target.x, target.y, target.z);
			}
		}
	}

	@Override
	protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
		super.updatePassengerPosition(passenger, positionUpdater);
		// Everything except the cage locks the rider's facing to the furniture.
		if (getType() != ModEntities.CAGE_SEAT && passenger instanceof LivingEntity living) {
			living.setBodyYaw(getYaw());
			float diff = MathHelper.wrapDegrees(living.getYaw() - getYaw());
			float clamped = MathHelper.clamp(diff, -90.0F, 90.0F);
			living.lastYaw += clamped - diff;
			living.setYaw(living.getYaw() + clamped - diff);
			living.setHeadYaw(living.getYaw());
		}
	}

	// Forge-only hook `shouldRiderSit()` (-> false = no sitting pose) has no Fabric equivalent.
	// PHASE 2: the riding pose is overridden client-side by the Player Animation Library pose / a small mixin.

	@Override
	public boolean isPushable() { return false; }

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) { return false; }

	private void updateFurnitureNeighbors() {
		if (blockPos != null) {
			getEntityWorld().updateNeighborsAlways(blockPos, getEntityWorld().getBlockState(blockPos).getBlock(), null);
		}
	}

	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		BlockPos above = getBlockPos().up();
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 && dz == 0) continue;
				BlockPos candidate = above.add(dx, 0, dz);
				if (getEntityWorld().isAir(candidate) && getEntityWorld().isAir(candidate.up())) {
					return new Vec3d(candidate.getX() + 0.5, candidate.getY(), candidate.getZ() + 0.5);
				}
			}
		}
		return getEntityPos();
	}

	// Seat entities are rebuilt from the block entity, never saved on their own.
	@Override
	public boolean shouldSave() { return false; }

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {}

	@Override
	protected void writeCustomData(WriteView view) {
		if (blockPos != null) {
			view.putInt("BlockX", blockPos.getX());
			view.putInt("BlockY", blockPos.getY());
			view.putInt("BlockZ", blockPos.getZ());
		}
	}

	@Override
	protected void readCustomData(ReadView view) {
		blockPos = new BlockPos(view.getInt("BlockX", 0), view.getInt("BlockY", 0), view.getInt("BlockZ", 0));
	}

	/** Position of the furniture block this seat belongs to (NOT the entity's own position). */
	@Nullable
	public BlockPos furniturePos() { return blockPos; }

	public void setFurniturePos(BlockPos pos) { this.blockPos = pos; }

	/** True until the occupant has been released. */
	public boolean isActive() { return !released; }

	public void markReleased() { this.released = true; }

	/** Frees the occupant: clears OCCUPIED/FLIPPED on the furniture, drops the session, removes the seat. */
	public void releaseOccupant(PlayerEntity player) {
		if (released) return;
		released = true;
		World world = getEntityWorld();
		if (blockPos != null) {
			SeatBlockEntity be = FurnitureUtil.seatBlockEntity(world, blockPos);
			if (be != null) be.clearOccupant(world, blockPos);
		}
		SeatSessions.all().remove(getUuid());
		if (!SeatSessions.isPlayerSeated(player.getUuid())) {
			SeatSessions.reset(player);
		}
		discard();
	}
}
