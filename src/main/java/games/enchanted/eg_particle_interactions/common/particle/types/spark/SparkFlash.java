package games.enchanted.eg_particle_interactions.common.particle.types.spark;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.LightUtil;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class SparkFlash extends ParticleInteractionsParticle {
    private final float originalQuadSize;
    protected final boolean useRandomAnimation;

    SparkFlash(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean useRandomAnimation) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
        this.speedUpWhenYMotionIsBlocked = true;
        this.friction = 0.96F;

        this.xd = (xSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.yd = (ySpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.zd = (zSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.useRandomAnimation = useRandomAnimation;
        if (useRandomAnimation) {
            int rot = this.random.nextIntBetweenInclusive(0, 3);
            this.roll = rot * 90;
            this.prevRoll = roll;
        }

        this.lifetime = this.random.nextInt(4) + 3;

        this.setScale(2 / 16f);
        this.originalQuadSize = this.getScale();
    }

    @Override
    public void tick() {
        super.tick();
        this.setScale(this.originalQuadSize * (0.5f + (Math.abs(1 - (this.age / this.lifetime)) * 0.5f)), true);
    }

    public static class Provider implements PIParticleProvider<PIParticleType.Simple> {
        public Provider() {
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
            return new SparkFlash(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, false);
        }
    }

    public static class RandomAnimationProvider implements PIParticleProvider<PIParticleType.Simple> {
        public RandomAnimationProvider() {
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
            return new SparkFlash(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, true);
        }
    }
}
