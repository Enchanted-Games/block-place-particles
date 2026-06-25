package games.enchanted.eg_particle_interactions.common.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.eg_particle_interactions.common.override_system.OverridePreset;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "spawnSprintParticle"
    )
    private void eg_particle_interactions$wrapSprintParticles(
        Level level,
        ParticleOptions particle,
        double x,
        double y,
        double z,
        double xd,
        double yd,
        double zd,
        Operation<Void> original,
        @Local(ordinal = 0) BlockPos blockPos,
        @Local(ordinal = 0) BlockState blockState
        ) {
        if(!(level instanceof ClientLevel clientLevel)) {
            original.call(level, particle, x, y, z, xd, yd, zd);
            return;
        }

        ParticleOrigin origin = ParticleOrigin.BLOCK_SPRINTED_ON;
        OverridePreset overridePreset = BlockOverrideManager.getForBlock(blockState, origin);
        ParticleOverride override = overridePreset.getRandom();
        Identifier id = ParticleOverrides.getIdOrThrow(override);

        if(id.equals(ParticleOverrides.VANILLA_OVERRIDE_ID)) {
            original.call(level, particle, x, y, z, xd, yd, zd);
            return;
        }

        ParticleContext context = ParticleContext.block(clientLevel, blockState, blockPos);

        override.spawnParticle(
            origin,
            context,
            x,
            y,
            z,
            xd * 0.5,
            yd * 0.5,
            zd * 0.5
        );
    }
}
