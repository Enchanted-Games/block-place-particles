package games.enchanted.blockplaceparticles.mixin.entity;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartBehavior;
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
    @Shadow public abstract @NotNull Direction getMotionDirection();
    @Shadow public abstract BlockState getDisplayBlockState();
    @Shadow public abstract float lerpTargetXRot();
    @Shadow public abstract float lerpTargetYRot();

    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private boolean block_place_particle$shouldSpawnSparks() {
        BlockPos blockPos = BlockPos.containing(this.getX(), this.getY(),this.getZ());
        BlockState blockState = this.level().getBlockState(blockPos);
        return !blockState.getFluidState().is(FluidTags.WATER);
    }

    @Unique
    private double block_place_particle$maxSpeed() {
        return this.isInWater() ? 0.2 : 0.4;
    }

    @Inject(
        at = @At("HEAD"),
        method = "tick"
    )
    protected void spawnSparksWhileMovingOnRails(CallbackInfo ci) {
        if (block_place_particle$shouldSpawnSparks() && this.level().isClientSide) {
            float horizontalRot = this.lerpTargetYRot();
            float verticalRot = this.lerpTargetXRot();

            BlockPos blockPos = BlockPos.containing(this.getX(), this.getY(),this.getZ());
            BlockState blockState = this.level().getBlockState(blockPos);
            boolean hasBlock = !this.getDisplayBlockState().is(BlockTags.AIR);

            SpawnParticles.spawnSparksAtMinecartWheels(this.getX(), this.getY(),this.getZ(), horizontalRot, verticalRot, BaseRailBlock.isRail(blockState), !this.getPassengers().isEmpty(), hasBlock, this.getDeltaMovement(), block_place_particle$maxSpeed(), this.level());
        }
    }
}
