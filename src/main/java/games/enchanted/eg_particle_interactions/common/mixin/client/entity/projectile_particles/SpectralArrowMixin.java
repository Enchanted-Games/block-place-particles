package games.enchanted.eg_particle_interactions.common.mixin.client.entity.projectile_particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.resource.tweaker.ParticleTweakerIds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpectralArrow.class)
public abstract class SpectralArrowMixin extends AbstractArrow {
    protected SpectralArrowMixin(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "tick"
    )
    private void eg_particle_interactions$disableVanillaSpectralParticles(Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original) {
        if(ParticleTweakerIds.SPECTRAL_ARROW_EFFECT.get().disableEffect()) return;
        original.call(instance, particle, x, y, z, xd, yd, zd);
    }
}
