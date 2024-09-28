package games.enchanted.blockplaceparticles.mixin.entity;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity {
    @Shadow public abstract boolean isOnRails();
    @Shadow @Nullable public abstract Vec3 getPos(double $$0, double $$1, double $$2);
    @Shadow @Nullable public abstract Vec3 getPosOffs(double $$0, double $$1, double $$2, double $$3);
    @Shadow public abstract @NotNull Direction getMotionDirection();
    @Shadow protected abstract double getMaxSpeed();
    @Shadow public abstract BlockState getDisplayBlockState();

    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private boolean block_place_particle$shouldSpawnSparks() {
        BlockPos blockPos = BlockPos.containing(this.getX(), this.getY(),this.getZ());
        BlockState blockState = this.level().getBlockState(blockPos);
        return !blockState.getFluidState().is(FluidTags.WATER);
    }

    @Inject(
        at = @At("HEAD"),
        method = "tick"
    )
    protected void spawnSparksWhileMovingOnRails(CallbackInfo ci) {
        if (block_place_particle$shouldSpawnSparks() && this.level().isClientSide) {
            float horizontalRot = 0;
            float verticalRot = 0;
            Vec3 pos = this.getPos(this.getX(), this.getY(), this.getZ());
            if(pos != null) {
                Vec3 posOffset = this.getPosOffs(this.getX(), this.getY(), this.getZ(), 0.3f);
                Vec3 posOffset2 = this.getPosOffs(this.getX(), this.getY(), this.getZ(), -0.3f);
                if (posOffset == null) {
                    posOffset = pos;
                }
                if (posOffset2 == null) {
                    posOffset2 = pos;
                }
                Vec3 finalPos = posOffset2.add(-posOffset.x, -posOffset.y, -posOffset.z);
                if (finalPos.length() != 0.0) {
                    finalPos = finalPos.normalize();
                    horizontalRot = (float)(Math.atan2(finalPos.z, finalPos.x) * 180.0 / Math.PI);
                    verticalRot = (float)(Math.atan(finalPos.y) * 73.0);
                }
            }

            BlockPos blockPos = BlockPos.containing(this.getX(), this.getY(),this.getZ());
            BlockState blockState = this.level().getBlockState(blockPos);
            boolean hasBlock = !this.getDisplayBlockState().is(BlockTags.AIR);

            SpawnParticles.spawnSparksAtMinecartWheels(this.getX(), this.getY(),this.getZ(), horizontalRot, verticalRot, BaseRailBlock.isRail(blockState), !this.getPassengers().isEmpty(), hasBlock, this.getDeltaMovement(), this.getMaxSpeed(), this.level());
        }
    }
}
