package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import org.joml.Vector3d;

public abstract class Emitter {
    static final String VELOCITY_MULTIPLIER_NAME = "velocity_multiplier";
    public static final Vector3d VELOCITY_MULTIPLIER_DEFAULT = new Vector3d(1.0);
    static final String POSITION_OFFSET_NAME = "position_offset";
    public static final Vector3d POSITION_OFFSET_DEFAULT = new Vector3d(0.0);

    private final Vector3d velocityMultiplier;
    private final Vector3d positionOffset;

    public Emitter(Vector3d velocityMultiplier, Vector3d positionOffset) {
        this.velocityMultiplier = velocityMultiplier;
        this.positionOffset = positionOffset;
    }

    public Emitter(double velocityMultiplierScalar, Vector3d positionOffset) {
        this(new Vector3d(velocityMultiplierScalar), positionOffset);
    }

    public final void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this.emit(
            context,
            x + this.positionOffset.x,
            y + this.positionOffset.y,
            z + this.positionOffset.z,
            xSpeed,
            ySpeed,
            zSpeed
        );
    }

    protected abstract void emit(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);

    protected Vector3d getVelocityMultiplier() {
        return this.velocityMultiplier;
    }

    protected Vector3d getPositionOffset() {
        return this.positionOffset;
    }

    public abstract MapCodec<? extends Emitter> codec();
}
