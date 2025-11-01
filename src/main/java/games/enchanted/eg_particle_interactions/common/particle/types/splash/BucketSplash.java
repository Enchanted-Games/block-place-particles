package games.enchanted.eg_particle_interactions.common.particle.types.splash;

import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class BucketSplash extends ParticleInteractionsParticle {
    protected BucketSplash(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);

        this.gravity = 0.95F;
        this.friction = 0.999F;
        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.yd = ySpeed + (Math.random() - 0.5) * ((this.random.nextFloat() * 0.5f) - 0.3F);
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));

        float particleSize = (float) 0.1355 - (this.random.nextBoolean() ? 0.01f : 0.0f);
        this.setScale(particleSize);
        this.setSize(particleSize, particleSize);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed && this.onGround && this.random.nextFloat() > 0.9f) {
            randomOnParticleLand();
        }
    }

    public void randomOnParticleLand() {
        this.remove();
    }

    @Override
    protected ParticleLayer getParticleLayer() {
        return ParticleLayer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleType options,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return new BucketSplash(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(level.random));
        }
    }
}