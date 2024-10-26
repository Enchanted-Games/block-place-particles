package games.enchanted.blockplaceparticles.particle.spark;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.emitter.AbstractParticleEmitter;
import games.enchanted.blockplaceparticles.particle.option.ParticleEmitterOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FloatingSparkEmitter extends AbstractParticleEmitter {
    final boolean shorterLife;
    protected FloatingSparkEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleEmitterOptions emitterOptions) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        this.shorterLife = false;
    }
    protected FloatingSparkEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleEmitterOptions emitterOptions, boolean shorterLife) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        this.shorterLife = shorterLife;
    }

    @Override
    protected ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z) {
        return shorterLife ? ModParticleTypes.FLOATING_SPARK_SHORT : ModParticleTypes.FLOATING_SPARK;
    }

    public static class Provider implements ParticleProvider<ParticleEmitterOptions> {
        public Provider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull ParticleEmitterOptions emitterOptions, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FloatingSparkEmitter(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        }
    }

    public static class ShorterLifeProvider implements ParticleProvider<ParticleEmitterOptions> {
        public ShorterLifeProvider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull ParticleEmitterOptions emitterOptions, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FloatingSparkEmitter(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions, true);
        }
    }
}
