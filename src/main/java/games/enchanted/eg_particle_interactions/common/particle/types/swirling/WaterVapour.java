package games.enchanted.eg_particle_interactions.common.particle.types.swirling;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.options.SimpleParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class WaterVapour extends SwirlingParticle {
    protected WaterVapour(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, ParticleConfig config, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean shouldSwirl) {
        super(components, appearance, context, config, x, y, z, xSpeed, ySpeed, zSpeed, shouldSwirl);
        this.setInitialVelocity(xSpeed, ySpeed, zSpeed, 0.015f);

        this.rotSpeed = 0f;
        this.spinAcceleration = 0f;
        this.swirlStrength = MathHelper.randomBetween(3f, 6f) * (level.getRandom().nextBoolean() ? -1 : 1);
        this.swirlPeriod = MathHelper.randomBetween(1, 4);

        this.setScale(2f / 32f);
    }

    public static class WaterVapourProvider implements PIParticleProvider<SimpleParticleOptions> {
        public WaterVapourProvider() {
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
            return new WaterVapour(components, appearance, context, options.config(), x, y, z, xSpeed, ySpeed, zSpeed, true);
        }
    }
}
