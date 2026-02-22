package games.enchanted.eg_particle_interactions.common.particle.types.falling_spin;

import games.enchanted.eg_particle_interactions.common.duck.ParticleAccess;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.ParticleAccessor;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

public class FallingSpinningParticle extends ParticleInteractionsParticle {
    private float rotSpeed;
    protected float spinAcceleration;
    protected float maxSpinSpeed = 1f;

    protected FallingSpinningParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravityMultiplier) {
        super(context, appearance, x, y, z);
        this.gravity = Mth.randomBetween(this.random, 0.25F, 0.38F);

        this.friction = 1.0F;
        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.lifetime = (int) (16.0 / ((double) this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.spinAcceleration = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.roll = (float) Math.toRadians(this.random.nextIntBetweenInclusive(0, 360));
        this.prevRoll = this.roll;

        float particleSize = this.random.nextBoolean() ? 0.07F : 0.08F;
        this.setScale(particleSize);
        this.setSize(particleSize, particleSize);
        this.gravity *= gravityMultiplier;

        ((ParticleAccess) this).eg_particle_interactions$setBypassMovementCollisionCheck(true);
    }

    @Override
    public void tick() {
        this.rotSpeed += this.rotSpeed >= this.maxSpinSpeed ? 0 : (this.spinAcceleration / 2.0f);
        if (this.rotSpeed > this.maxSpinSpeed) this.rotSpeed = this.maxSpinSpeed;

        this.prevRoll = this.roll;
        if (!this.onGround && !((ParticleAccessor) this).block_place_particle$getStoppedByCollision()) {
            this.roll += this.rotSpeed / 6.5f;
        }

        this.xd *= 0.949999988079071;
        this.yd *= 0.8999999761581421;
        this.zd *= 0.949999988079071;

        // if moving downwards
        if (this.yd < 0 && ((ParticleAccess) this).eg_particle_interactions$getBypassMovementCollisionCheck()) {
            ((ParticleAccess) this).eg_particle_interactions$setBypassMovementCollisionCheck(false);
        }

        super.tick();
    }

    public static class GenericLeafProvider implements PIParticleProvider<PIParticleType.Simple> {
        public GenericLeafProvider() {
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
            return new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 1);
        }
    }

    public static class RandomisedSizeMoreGravityProvider implements PIParticleProvider<PIParticleType.Simple> {
        public RandomisedSizeMoreGravityProvider() {
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
            FallingSpinningParticle particle = new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 2f);
            float particleSize = MathHelpers.randomBetween(0.08f, 0.12f);
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class PaleOakProvider implements PIParticleProvider<PIParticleType.Simple> {
        public PaleOakProvider() {
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
            FallingSpinningParticle particle = new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.6f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.1f : 0.15f;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            particle.maxSpinSpeed = 0.1f;
            particle.spinAcceleration = (float) Math.toRadians(context.level().getRandom().nextBoolean() ? -1.0 : 1.0);
            return particle;
        }
    }

    public static class TintedLeafProvider implements PIParticleProvider<PIParticleType.Simple> {
        public TintedLeafProvider() {
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
            FallingSpinningParticle particle = new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 1f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.1f : 0.15f;
            particle.setScale(particleSize);
            particle.maxSpinSpeed = 0.5f;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class FlowerPetalProvider implements PIParticleProvider<PIParticleType.Simple> {
        public FlowerPetalProvider() {
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
            FallingSpinningParticle particle = new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 1f);
            particle.maxSpinSpeed = 0.5f;
            return particle;
        }
    }

    public static class GrassBladeProvider implements PIParticleProvider<PIParticleType.Simple> {
        public GrassBladeProvider() {
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
            FallingSpinningParticle particle = new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 1f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.10F : 0.12F;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class HeavyGrassBladeProvider implements PIParticleProvider<PIParticleType.Simple> {
        public HeavyGrassBladeProvider() {
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
            FallingSpinningParticle particle = new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 2f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.10F : 0.12F;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class ChainSnapProvider implements PIParticleProvider<PIParticleType.Simple> {
        public ChainSnapProvider() {
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
            FallingSpinningParticle particle = new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 3f);
            ClientLevel level = context.level();
            float particleSize = level.getRandom().nextBoolean() ? 0.14F : 0.15F;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            particle.maxSpinSpeed = 0.2f;
            particle.spinAcceleration = (float) Math.toRadians(level.getRandom().nextBoolean() ? -1.0 : 1.0);
            return particle;
        }
    }

    public static class SugarCaneProvider implements PIParticleProvider<PIParticleType.Simple> {
        public SugarCaneProvider() {
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
            FallingSpinningParticle particle = new FallingSpinningParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 2.5f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.11F : 0.13F;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }
}