package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public abstract class Emitter {
    static final String VELOCITY_MULTIPLIER_NAME = "velocity_multiplier";
    public static final Vector3d VELOCITY_MULTIPLIER_DEFAULT = new Vector3d(1.0);

    private final Vector3d velocityMultiplier;

    public Emitter(Vector3d velocityMultiplier) {
        this.velocityMultiplier = velocityMultiplier;
    }

    public Emitter(double velocityMultiplierScalar) {
        this.velocityMultiplier = new Vector3d(velocityMultiplierScalar);
    }

    public abstract void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);

    protected Vector3d getVelocityMultiplier() {
        return this.velocityMultiplier;
    }

    public abstract MapCodec<? extends Emitter> codec();
}
