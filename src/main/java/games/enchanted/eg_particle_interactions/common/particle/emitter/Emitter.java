package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public abstract class Emitter {
    static final String VELOCITY_MULTIPLIER_NAME = "velocity_multiplier";
    public static final double VELOCITY_MULTIPLIER_DEFAULT = 1.0;

    private final double velocityMultiplier;

    public Emitter(double velocityMultiplier) {
        this.velocityMultiplier = velocityMultiplier;
    }

    public abstract void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);

    protected double getVelocityMultiplier() {
        return this.velocityMultiplier;
    }

    public abstract MapCodec<? extends Emitter> codec();
}
