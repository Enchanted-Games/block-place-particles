package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.SimpleParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public class LavaPop extends ConstantMotionParticle {
    protected LavaPop(ParticleContext context, ParticleAppearance appearance, ParticleConfig config, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, float quadSize) {
        super(context, appearance, config, x, y, z, constantXSpeed, constantYSpeed, constantZSpeed, quadSize);
        this.billboardYOffset = 1.0f;
    }

    public static class LavaPopProvider implements PIParticleProvider<SimpleParticleOptions> {
        public LavaPopProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new LavaPop(context, appearance, options.config(), x, y, z, xSpeed, ySpeed, zSpeed, 2 / 8f);
        }
    }
}
