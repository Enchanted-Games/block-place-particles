package games.enchanted.eg_particle_interactions.common.particle.types.emitter.arc;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class ArcEmitter extends AbstractArcEmitter {
    protected ArcEmitter(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, ArcEmitterOptions options) {
        super(context, appearance, x, y, z, 0, 0, 0, options);
    }

    @Override
    protected @Nullable PIParticleOptions getParticleToEmit(ParticleContext context, double x, double y, double z) {
        return level.getRandom().nextFloat() > ((float) this.age / this.lifetime) ? ParticleTypesRegistry.LIGHTNING_FLASH : null;
    }

    public static class Provider implements PIParticleProvider<ArcEmitterOptions> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            ArcEmitterOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new ArcEmitter(context, appearance, x, y, z, options);
        }
    }
}
