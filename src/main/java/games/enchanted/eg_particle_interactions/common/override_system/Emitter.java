package games.enchanted.eg_particle_interactions.common.override_system;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.core.particles.ParticleOptions;

public class Emitter {
    private final ParticleOptions particleOptions;
    private final double velocityMultiplier;

    public Emitter(ParticleOptions options, double velocityMultiplier) {
        this.particleOptions = options;
        this.velocityMultiplier = velocityMultiplier;
    }

    public Emitter(ParticleOptions options) {
        this(options, 1);
    }

    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        context.level().addParticle(particleOptions, x, y, z, xSpeed * this.velocityMultiplier, ySpeed * this.velocityMultiplier, zSpeed * this.velocityMultiplier);
    }
}
