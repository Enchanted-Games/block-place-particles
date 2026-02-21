package games.enchanted.eg_particle_interactions.common.particle.types.swirling;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class WaterVapour extends SwirlingParticle {
    protected WaterVapour(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean shouldSwirl) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, shouldSwirl);
        this.setInitialVelocity(xSpeed, ySpeed, zSpeed, 0.015f);

        this.gravity = MathHelpers.randomBetween(-0.04f, -0.07f);

        this.lifetime = MathHelpers.randomBetween(4, 15);

        this.rotSpeed = 0f;
        this.spinAcceleration = 0f;
        this.swirlStrength = MathHelpers.randomBetween(3f, 6f) * (level.getRandom().nextBoolean() ? -1 : 1);
        this.swirlPeriod = MathHelpers.randomBetween(1, 4);

        this.setScale(2f / 32f);
        this.setSize(1f / 32f, 1f / 32f);
    }

    public static class WaterVapourProvider implements PIParticleProvider<PIParticleType.Simple> {
        public WaterVapourProvider() {
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
            return new WaterVapour(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, true);
        }
    }
}
