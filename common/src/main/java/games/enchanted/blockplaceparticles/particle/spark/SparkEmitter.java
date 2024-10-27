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

public class SparkEmitter extends AbstractParticleEmitter {
    protected SparkEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleEmitterOptions emitterOptions) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
    }

    @Override
    protected ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z) {
        return ModParticleTypes.FLYING_SPARK;
    }

    public static class Provider implements ParticleProvider<ParticleEmitterOptions> {
        public Provider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull ParticleEmitterOptions emitterOptions, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SparkEmitter(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        }
    }
}
