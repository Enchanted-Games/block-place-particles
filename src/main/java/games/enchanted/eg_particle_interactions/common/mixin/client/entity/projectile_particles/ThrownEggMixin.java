package games.enchanted.eg_particle_interactions.common.mixin.client.entity.projectile_particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownEgg.class)
public class ThrownEggMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "handleEntityEvent"
    )
    private void eg_particle_interactions$modifyParticleVelocity(Level instance, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if(!GeneralOptions.PROJECTILE_BREAKING_PARTICLE_VELOCITY_FIX.getValue()) {
            original.call(instance, particleData, x, y, z, xSpeed, ySpeed, zSpeed);
            return;
        }
        original.call(
            instance,
            particleData,
            x,
            y,
            z,
            xSpeed + ((instance.getRandom().nextFloat() - 0.5f) * 0.05f) + (instance.getRandom().nextBoolean() ? 0.05f : -0.05f),
            ySpeed + ((instance.getRandom().nextFloat() - 0.5f) * 0.05f) + (instance.getRandom().nextBoolean() ? 0.05f : -0.05f),
            zSpeed + ((instance.getRandom().nextFloat() - 0.5f) * 0.05f) + (instance.getRandom().nextBoolean() ? 0.05f : -0.05f)
        );
    }
}
