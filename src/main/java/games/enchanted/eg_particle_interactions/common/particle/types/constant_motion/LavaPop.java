package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public class LavaPop extends ConstantMotionAnimatedParticle {
    protected LavaPop(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, int lifetime, float quadSize, boolean translucent) {
        super(context, appearance, x, y, z, constantXSpeed, constantYSpeed, constantZSpeed, lifetime, quadSize, translucent);
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
            return new LavaPop(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, MathHelpers.randomBetween(26, 32), 2 / 8f, false);
        }
    }
}
