package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public class LavaPop extends ConstantMotionAnimatedParticle {
    protected LavaPop(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, int lifetime, float quadSize, boolean transparency) {
        super(context, appearance, x, y, z, lifetime, quadSize, transparency);
        this.billboardYOffset = 1.0f;
    }

    public static class LavaPopProvider implements PIParticleProvider<PIParticleType.Simple> {
        public LavaPopProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new LavaPop(context, appearance, x, y, z, MathHelpers.randomBetween(26, 32), 2 / 8f, false);
        }
    }
}
