package games.enchanted.eg_particle_interactions.common.particle.override;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.core.particles.ParticleOptions;

public class Emitter {
    final ParticleOptions particleOptions;
    final float velocityMultiplier;

    public Emitter(ParticleOptions options, float velocityMultiplier) {
        this.particleOptions = options;
        this.velocityMultiplier = velocityMultiplier;
    }

    public Emitter(ParticleOptions options) {
        this(options, 1);
    }

    public void spawnParticle(ParticleContext context, double x, double y, double z, float xSpeed, float ySpeed, float zSpeed) {
        context.level().addParticle(particleOptions, x, y, z, xSpeed * this.velocityMultiplier, ySpeed * this.velocityMultiplier, zSpeed * this.velocityMultiplier);
    }
}
