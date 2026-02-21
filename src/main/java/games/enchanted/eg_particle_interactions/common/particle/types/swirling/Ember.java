package games.enchanted.eg_particle_interactions.common.particle.types.swirling;

import games.enchanted.eg_particle_interactions.common.duck.ParticleAccess;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.util.LightUtil;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class Ember extends SwirlingParticle {
    protected Ember(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean shouldSwirl) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, shouldSwirl);
        this.setInitialVelocity(xSpeed, ySpeed, zSpeed, 0.015f);

        this.gravity = MathHelpers.randomBetween(-0.03f, -0.08f);

        this.lifetime = MathHelpers.randomBetween(20, 100);

        this.rotSpeed = 0f;
        this.spinAcceleration = 0f;
        this.swirlStrength = MathHelpers.randomBetween(0.7f, 0.9f) * (level.getRandom().nextBoolean() ? -1 : 1);
        this.swirlPeriod = MathHelpers.randomBetween(708, 720);

        this.setScale(2.5f / 32f);
        this.setSize(1 / 32f, 1 / 32f);

        ((ParticleAccess) this).eg_particle_interactions$setBypassMovementCollisionCheck(true);
    }

    @Override
    public void tick() {
        super.tick();

        float percentageAge = (float) this.age / this.lifetime;
        if (percentageAge > 0.8) {
            float finalA = 1 - ((percentageAge - 0.8f) * 5f);
            if (finalA < 0) {
                this.setAlpha(0, true);
                return;
            }
            this.setAlpha(finalA, true);
        }
    }

    @Override
    public void applyGravity() {
        this.yd = -this.gravity;
    }

    @Override
    public int getLightmapCoords(float partialTicks) {
        return LightUtil.FULL_BRIGHT;
    }

    public static class EmberProvider implements PIParticleProvider<PIParticleType.Simple> {
        public EmberProvider() {
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
            return new Ember(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, true);
        }
    }
}
