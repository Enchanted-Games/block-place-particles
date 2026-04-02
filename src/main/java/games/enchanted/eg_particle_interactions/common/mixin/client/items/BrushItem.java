package games.enchanted.eg_particle_interactions.common.mixin.client.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.eg_particle_interactions.common.config.categories.ItemInteractionOptions;
import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.override_system.OverridePreset;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.options.DefaultParticles;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BrushItem.DustParticlesDelta;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = net.minecraft.world.item.BrushItem.class, priority = 1010)
public abstract class BrushItem {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "spawnDustParticles"
    )
    private void eg_particle_interactions$replaceBrushDustParticlesConditionally(
        Level instance,
        ParticleOptions particle,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed,
        Operation<Void> original,
        Level level,
        BlockHitResult hitResult,
        BlockState state,
        @Local(ordinal = 0) DustParticlesDelta particlesDelta,
        @Local(ordinal = 0) int armDirection
    ) {
        if(!(instance instanceof ClientLevel clientLevel)) {
            original.call(instance, particle, x, y, z, xSpeed, ySpeed, zSpeed);
            return;
        }
        if(ItemInteractionOptions.BRUSH_PARTICLE_BEHAVIOUR.getValue() == BrushParticleBehaviour.NONE) return;

        final double outwardVelocity = 0.05;
        BlockPos blockPos = hitResult.getBlockPos();
        Direction brushDirection = hitResult.getDirection();
        Vec3 particlePos = hitResult.getLocation();
        double baseDeltaX = particlesDelta.xd();
        double baseDeltaY = particlesDelta.yd();
        double baseDeltaZ = particlesDelta.zd();

        ParticleOrigin origin = ParticleOrigin.BLOCK_BRUSHED;
        OverridePreset preset = BlockOverrideManager.getForBlock(state, origin);
        ParticleOverride override = preset.getRandom();
        Identifier id = ParticleOverrides.getIdOrThrow(override);

        boolean isVanillaOrEmptyOverride = id.equals(ParticleOverrides.VANILLA_OVERRIDE_ID) || id.equals(ParticleOverrides.EMPTY_OVERRIDE_ID);

        if (
            ItemInteractionOptions.BRUSH_PARTICLE_BEHAVIOUR.getValue() == BrushParticleBehaviour.VANILLA_LIKE ||
            (ItemInteractionOptions.BRUSH_PARTICLE_BEHAVIOUR.getValue() == BrushParticleBehaviour.DUST && !isVanillaOrEmptyOverride)
        ) {
            override.spawnParticle(
                origin,
                ParticleContext.block(clientLevel, state, blockPos),
                particlePos.x + (brushDirection.getStepX() * outwardVelocity),
                particlePos.y + (brushDirection.getStepY() * outwardVelocity),
                particlePos.z + (brushDirection.getStepZ() * outwardVelocity),
                (baseDeltaX * (double) armDirection * level.getRandom().nextDouble()) + (brushDirection.getStepX() * outwardVelocity),
                (baseDeltaY + 1) * level.getRandom().nextDouble() * brushDirection.getStepY(),
                (baseDeltaZ * (double) armDirection * level.getRandom().nextDouble()) + (brushDirection.getStepZ() * outwardVelocity)
            );
            return;
        } else if(ItemInteractionOptions.BRUSH_PARTICLE_BEHAVIOUR.getValue() == BrushParticleBehaviour.DUST) {
            double velocityMultiplier = 0.1f;
            ParticleSpawner.spawn(
                DefaultParticles.BRUSH_DUST.get(),
                ParticleContext.plain(clientLevel),
                particlePos.x + (brushDirection.getStepX() * outwardVelocity),
                particlePos.y + (brushDirection.getStepY() * outwardVelocity),
                particlePos.z + (brushDirection.getStepZ() * outwardVelocity),
                (baseDeltaX * (double) armDirection * level.getRandom().nextDouble() * velocityMultiplier) + (brushDirection.getStepX() * outwardVelocity),
                (baseDeltaY + 1) * level.getRandom().nextDouble() * velocityMultiplier * brushDirection.getStepY(),
                (baseDeltaZ * (double) armDirection * level.getRandom().nextDouble() * velocityMultiplier) + (brushDirection.getStepZ() * outwardVelocity)
            );
            return;
        }

        original.call(instance, particle, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}
