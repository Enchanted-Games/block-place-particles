package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public class ConstantMotionParticle extends ParticleInteractionsParticle {
    protected ConstantMotionParticle(ParticleContext context, ParticleAppearance appearance, ParticleConfig config, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, float quadSize) {
        super(context, appearance, config, x, y, z, constantXSpeed, constantYSpeed, constantZSpeed);
        this.setScale(quadSize);
    }

    @Override
    public void tick() {
        if (this.removed) return;
        if (this.age > this.lifetime) {
            this.remove();
        }

        this.pickSpriteForAppearance();
        ++this.age;

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
    }
}
