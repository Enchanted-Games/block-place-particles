package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public class ConstantMotionParticle extends ParticleInteractionsParticle {
    protected ConstantMotionParticle(ParticleContext context, ParticleAppearance appearance, ParticleConfig config, double x, double y, double z, float quadSize) {
        super(context, appearance, config, x, y, z);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.setScale(quadSize);
    }

    protected ConstantMotionParticle(ParticleContext context, ParticleAppearance appearance, ParticleConfig config, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, float quadSize) {
        this(context, appearance, config, x, y, z, quadSize);
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
}
