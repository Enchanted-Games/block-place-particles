package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public class ConstantMotionParticle extends ParticleInteractionsParticle {
    protected ConstantMotionParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, ParticleConfig config, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, float quadSize) {
        super(components, appearance, context, config, y, z, constantXSpeed, constantYSpeed, constantZSpeed, x);
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
