package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.options.SimpleParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public class LavaPop extends ConstantMotionParticle {
    protected LavaPop(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, ParticleConfig config, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, float quadSize) {
        super(components, appearance, context, config, x, y, z, constantXSpeed, constantYSpeed, constantZSpeed, quadSize);
        this.billboardYOffset = 1.0f;
    }

    public static class LavaPopProvider implements PIParticleProvider<SimpleParticleOptions> {
        public LavaPopProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleOptions options,
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
            return new LavaPop(components, appearance, context, options.config(), x, y, z, xSpeed, ySpeed, zSpeed, 2 / 8f);
        }
    }
}
