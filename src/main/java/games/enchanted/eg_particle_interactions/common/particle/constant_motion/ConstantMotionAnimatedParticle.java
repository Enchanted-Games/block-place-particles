package games.enchanted.eg_particle_interactions.common.particle.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.compat.CustomGeometryParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;

public class ConstantMotionAnimatedParticle extends CustomGeometryParticle {
    SpriteSet sprites;
    boolean translucent;

    protected ConstantMotionAnimatedParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int lifetime, float quadSize, boolean translucent) {
        super(level, x, y, z, spriteSet.get(level.random));
        this.sprites = spriteSet;
        this.setSpriteFromAge(sprites);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.translucent = translucent;
        this.lifetime = lifetime;
        this.scale = quadSize;
    }
    protected ConstantMotionAnimatedParticle(ClientLevel level, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, SpriteSet spriteSet, int lifetime, float quadSize, boolean translucent) {
        this(level, x, y, z, spriteSet, lifetime, quadSize, translucent);
        this.xd = constantXSpeed;
        this.yd = constantYSpeed;
        this.zd = constantZSpeed;
    }

    @Override
    public void tick() {
        if(this.removed) return;
        if(this.age > this.lifetime){
            this.remove();
        }

        this.setSpriteFromAge(sprites);
        ++this.age;

        this.x -= this.xd;
        this.y -= this.yd;
        this.z -= this.zd;
    }

    @Override
    protected ParticleLayer getParticleLayer() {
        return this.translucent ? ParticleLayer.TRANSLUCENT : ParticleLayer.OPAQUE;
    }
}
