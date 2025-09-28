package games.enchanted.eg_particle_interactions.common.particle.splash;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LavaSplash extends BucketSplash {
    public LavaSplash(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(level.random));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            float percentAge = (float) this.age / this.lifetime;
            if (this.random.nextFloat() < percentAge * 1.5 && this.random.nextFloat() > 0.5f) {
                this.level.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }
    }
    
    @Override
    public void randomOnParticleLand() {
        super.randomOnParticleLand();
        this.level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.03, 0);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleType type,
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
            return new LavaSplash(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}
