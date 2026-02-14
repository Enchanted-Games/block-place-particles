package games.enchanted.eg_particle_interactions.common.override_system;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.core.particles.ParticleOptions;
import org.jspecify.annotations.Nullable;

public class Emitter {
    public static final Emitter EMPTY = new Emitter();

    private final @Nullable ParticleOptions particleOptions;
    private final double velocityMultiplier;

    public Emitter(ParticleOptions options, double velocityMultiplier) {
        this.particleOptions = options;
        this.velocityMultiplier = velocityMultiplier;
    }

    public Emitter(ParticleOptions options) {
        this(options, 1);
    }

    protected Emitter() {
        this.particleOptions = null;
        this.velocityMultiplier = 0;
    }

    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        if(this.particleOptions == null) return;
        context.level().addParticle(this.particleOptions, x, y, z, xSpeed * this.velocityMultiplier, ySpeed * this.velocityMultiplier, zSpeed * this.velocityMultiplier);
    }
}
