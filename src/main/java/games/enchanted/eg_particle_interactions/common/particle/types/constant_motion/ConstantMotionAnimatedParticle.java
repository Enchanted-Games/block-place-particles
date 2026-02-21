package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import org.jspecify.annotations.NonNull;

public class ConstantMotionAnimatedParticle extends ParticleInteractionsParticle {
    boolean translucent;

    protected ConstantMotionAnimatedParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, int lifetime, float quadSize, boolean translucent) {
        super(context, appearance, x, y, z);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.translucent = translucent;
        this.lifetime = lifetime;
        this.setScale(quadSize);
    }

    protected ConstantMotionAnimatedParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, int lifetime, float quadSize, boolean translucent) {
        this(context, appearance, x, y, z, lifetime, quadSize, translucent);
        this.xd = constantXSpeed;
        this.yd = constantYSpeed;
        this.zd = constantZSpeed;
    }

    @Override
    public void tick() {
        if (this.removed) return;
        if (this.age > this.lifetime) {
            this.remove();
        }

        this.pickSpriteForAppearance();
        ++this.age;

        this.x -= this.xd;
        this.y -= this.yd;
        this.z -= this.zd;
    }

    @Override
    protected @NonNull ParticleLayer getParticleLayer() {
        return this.translucent ? ParticleLayer.TRANSLUCENT : ParticleLayer.CUTOUT;
    }
}
