package games.enchanted.blockplaceparticles.particle.petal;

import games.enchanted.blockplaceparticles.duck.ParticleAccess;
import games.enchanted.blockplaceparticles.mixin.accessor.client.ParticleAccessor;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FallingPetal extends TextureSheetParticle {
    private float rotSpeed;
    private float spinAcceleration;
    protected float maxSpinSpeed = 1f;
    private boolean transparency;

    protected FallingPetal(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier) {
        super(level, x, y, z);
        this.setSprite(spriteSet.get(this.random));
        this.gravity = Mth.randomBetween(this.random, 0.25F, 0.38F);;
        this.friction = 1.0F;
        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.lifetime = (int)(16.0 / ((double)this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.spinAcceleration = (float)Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.roll = (float) Math.toRadians(this.random.nextIntBetweenInclusive(0, 360));
        this.oRoll = this.roll;

        float particleSize = this.random.nextBoolean() ? 0.07F : 0.08F;
        this.quadSize = particleSize;
        this.setSize(particleSize, particleSize);
        this.gravity *= gravityMultiplier;

        this.transparency = false;

        ((ParticleAccess) this).setBypassMovementCollisionCheck(true);
    }
    protected FallingPetal(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier, boolean transparency) {
        this(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier);
        this.transparency = transparency;
    }

    @Override
    public void tick() {
        this.rotSpeed += this.rotSpeed >= this.maxSpinSpeed ? 0 : (this.spinAcceleration / 2.0f);
        if(this.rotSpeed > this.maxSpinSpeed) this.rotSpeed = this.maxSpinSpeed;

        this.oRoll = this.roll;
        if( !this.onGround && !((ParticleAccessor) this).block_place_particle$getStoppedByCollision() ) {
            this.roll += this.rotSpeed / 6.5f;
        }

        this.xd *= 0.949999988079071;
        this.yd *= 0.8999999761581421;
        this.zd *= 0.949999988079071;

        if(this.age >= 6 && ((ParticleAccess) this).getBypassMovementCollisionCheck()) {
            ((ParticleAccess) this).setBypassMovementCollisionCheck(false);
        }

        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return this.transparency ? ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT : ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class GenericLeafProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public GenericLeafProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FallingPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, 1);
        }
    }

    public static class SnowflakeProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public SnowflakeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingPetal particle = new FallingPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, 0.4f, true);
            float particleSize = level.random.nextBoolean() ? 0.10F : 0.12F;
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            particle.spinAcceleration = 0;
            particle.maxSpinSpeed = 0;
            particle.roll = 0;
            particle.oRoll = 0;
            return particle;
        }
    }

    public static class RandomisedSizeMoreGravityProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public RandomisedSizeMoreGravityProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingPetal particle = new FallingPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, 2f);
            float particleSize = MathHelpers.randomBetween(0.08f, 0.12f);
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class PaleOakProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public PaleOakProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingPetal particle = new FallingPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, 0.6f);
            float particleSize = level.random.nextBoolean() ? 0.1f : 0.15f;
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            particle.maxSpinSpeed = 0.1f;
            particle.spinAcceleration = (float)Math.toRadians(level.random.nextBoolean() ? -1.0 : 1.0);
            return particle;
        }
    }
}