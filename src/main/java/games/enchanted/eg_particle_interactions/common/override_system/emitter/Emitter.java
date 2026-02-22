package games.enchanted.eg_particle_interactions.common.override_system.emitter;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public abstract class Emitter {
    static final MapCodec<? extends Emitter> EMPTY_CODEC = MapCodec.unit(new EmptyEmitter());

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


    private static class EmptyEmitter extends Emitter {
        public EmptyEmitter() {
            super(0);
        }

        @Override
        public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {

        }

        @Override
        public MapCodec<? extends Emitter> codec() {
            return null;
        }
    }
}
