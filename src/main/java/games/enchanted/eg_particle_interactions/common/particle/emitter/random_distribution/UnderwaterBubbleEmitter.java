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

public class UnderwaterBubbleEmitter extends AbstractRandomDistributionEmitter {
    protected UnderwaterBubbleEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomDistributionEmitterOptions emitterOptions) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
    }

    @Override
    protected ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z) {
        return ModParticleTypes.UNDERWATER_RISING_BUBBLE_SMALL;
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
            return new UnderwaterBubbleEmitter(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        }
    }
}
