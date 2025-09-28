package games.enchanted.eg_particle_interactions.common.particle.emitter.random_distribution;

import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import games.enchanted.eg_particle_interactions.common.particle.option.RandomDistributionEmitterOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SparkEmitter extends AbstractRandomDistributionEmitter {
    protected SparkEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomDistributionEmitterOptions emitterOptions) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
    }

    @Override
    protected ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z) {
        return ModParticleTypes.FLYING_SPARK;
    }

    public static class Provider implements ParticleProvider<RandomDistributionEmitterOptions> {
        public Provider(SpriteSet spriteSet) {}

        @Override
        public @Nullable Particle createParticle(
            RandomDistributionEmitterOptions emitterOptions,
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
            return new SparkEmitter(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        }
    }
}
