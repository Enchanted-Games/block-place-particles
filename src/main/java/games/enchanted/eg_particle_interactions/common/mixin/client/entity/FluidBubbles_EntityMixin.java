package games.enchanted.eg_particle_interactions.common.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.override_system.OverridePreset;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.FluidOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Entity.class, priority = 950)
public class FluidBubbles_EntityMixin {
    @WrapOperation(
        method = "doWaterSplashEffect",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0)
    )
    private void eg_particle_interactions$wrapBubbles(Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original) {
        this.eg_particle_interactions$tryParticleReplacement(instance, particle, x, y, z, xd, yd, zd, original, ParticleOrigin.FLUID_WATER_ENTITY_ENTERED_BUBBLES);
    }

    @WrapOperation(
        method = "doWaterSplashEffect",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1)
    )
    private void eg_particle_interactions$wrapSplashes(Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original) {
        this.eg_particle_interactions$tryParticleReplacement(instance, particle, x, y, z, xd, yd, zd, original, ParticleOrigin.FLUID_WATER_ENTITY_ENTERED);
    }

    @Unique
    private void eg_particle_interactions$tryParticleReplacement(
        Level instance,
        ParticleOptions particle,
        double x,
        double y,
        double z,
        double xd,
        double yd,
        double zd,
        Operation<Void> original,
        ParticleOrigin origin
    ) {
        if(!(instance instanceof ClientLevel clientLevel)) {
            original.call(instance, particle, x, y, z, xd, yd, zd);
            return;
        }

        OverridePreset overridePreset = FluidOverrideManager.getForFluid(Fluids.WATER.defaultFluidState(), origin);
        ParticleOverride override = overridePreset.getRandom();
        ParticleContext context = ParticleContext.fluid(clientLevel, Fluids.WATER.defaultFluidState(), BlockPos.containing(x, y, z));

        if(override.hasNoEmitter(origin, context) || ParticleOverrides.getIdOrThrow(override).equals(ParticleOverrides.VANILLA_OVERRIDE_ID)) {
            original.call(instance, particle, x, y, z, xd, yd, zd);
            return;
        }

        double newYd = yd == 0.0 && (xd != 0.0 || zd != 0.0) ?
            0.1d :
            clientLevel.getRandom().nextFloat() * 0.2d + 0.05d;

        override.spawnParticle(
            origin,
            context,
            x,
            y,
            z,
            xd,
            newYd,
            zd
        );
    }
}
