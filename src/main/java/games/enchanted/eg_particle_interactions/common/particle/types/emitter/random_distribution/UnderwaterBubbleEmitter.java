package games.enchanted.eg_particle_interactions.common.particle.types.emitter.random_distribution;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.options.DefaultParticles;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.RandomDistributionEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class UnderwaterBubbleEmitter extends AbstractRandomDistributionEmitter {
    protected UnderwaterBubbleEmitter(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomDistributionEmitterOptions emitterOptions) {
        super(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
    }

    @Override
    protected PIParticleOptions getParticleToEmit(ParticleContext context, double x, double y, double z) {
        return DefaultParticles.UNDERWATER_RISING_BUBBLE_SMALL.get();
    }

    public static class Provider implements PIParticleProvider<RandomDistributionEmitterOptions> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            RandomDistributionEmitterOptions emitterOptions,
            ParticleComponentMap components,
            ParticleAppearance appearance,
            ParticleContext context,
            double x,
            double z,
            double y,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new UnderwaterBubbleEmitter(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        }
    }
}
