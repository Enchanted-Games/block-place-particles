package games.enchanted.blockplaceparticles.particle.spark;

import games.enchanted.blockplaceparticles.particle.StretchyBouncyCubeParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlyingSpark extends StretchyBouncyCubeParticle {
    private boolean hasDecreasedLifespan = false;

    private float redMin;
    private float redDecayRate;
    private float greenMin;
    private float greenDecayRate;
    private float blueMin;
    private float blueDecayRate;

    protected FlyingSpark(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravity, int lifetime, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setSprite(spriteSet.get(this.random.nextInt(12), 12));
        this.gravity = gravity;
        this.friction = 1.0F;

        this.xd = (xSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.yd = (ySpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.zd = (zSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806 * (this.random.nextFloat() > 0.95 ? 2 : 1);

        this.physics_bounceDecay = 0.6f;

        this.rCol = this.gCol = 1;
        this.bCol = 0.9f;

        this.lifetime = lifetime;

        float particleSize = (this.random.nextBoolean() ? 0.025F : 0.03F);
        this.setSize(particleSize, particleSize);
        this.quadSize = particleSize;

        this.redMin = 0.95f;
        this.redDecayRate = 0.985f;
        this.greenMin = 0.8f;
        this.greenDecayRate = 0.984f;
        this.blueMin = 0.35f;
        this.blueDecayRate = 0.98f;
    }

    protected FlyingSpark(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravity, int lifetime, SpriteSet spriteSet, float redMin, float redDecayRate, float greenMin, float greenDecayRate, float blueMin, float blueDecayRate) {
        this(level, x, y, z, xSpeed, ySpeed, zSpeed, gravity, lifetime, spriteSet);
        this.redMin = redMin;
        this.redDecayRate = redDecayRate;
        this.greenMin = greenMin;
        this.greenDecayRate = greenDecayRate;
        this.blueMin = blueMin;
        this.blueDecayRate = blueDecayRate;
    }

    private float getColourDecayFactor(float totalVelocity) {
        float d = Math.abs(totalVelocity) * 500;
        float ticksToStartDarkeningAt;
        if(d > 200) {
            ticksToStartDarkeningAt = 5;
        } else if(d > 150) {
            ticksToStartDarkeningAt = 20;
        } else if (d > 100) {
            ticksToStartDarkeningAt = 40;
        } else if (d > 75) {
            ticksToStartDarkeningAt = 80;
        } else if (d > 50) {
            ticksToStartDarkeningAt = 100;
        }else {
            ticksToStartDarkeningAt = 130;
        }

        return this.ticksAlive <= ticksToStartDarkeningAt ? 1f : 0.85f;
    }

    @Override
    public void tick() {
        super.tick();

        if(age > 0) {
            float totalVelocity = getTotalVelocity();

            if(!hasDecreasedLifespan && this.onGround && !this.isParticleMoving()) {
                this.age = this.lifetime - Mth.randomBetweenInclusive(this.random, 20, 40);
                hasDecreasedLifespan = true;
            }
            if(!(this.age >= this.lifetime - 14)) {
                float colourDecayFactor = getColourDecayFactor(totalVelocity);
                this.rCol *= this.rCol > redMin ? redDecayRate * colourDecayFactor : 1f;
                this.gCol *= this.gCol > greenMin ? greenDecayRate * colourDecayFactor : 1f;
                this.bCol *= this.bCol > blueMin ? blueDecayRate * colourDecayFactor : 1f;
            }
        }
    }

    @Override
    public int getLightColor(float f) {
        return 240;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class LongLifeSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public LongLifeSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.8F, 0.9F), Mth.randomBetweenInclusive(level.random, 80, 120), spriteSet);
        }
    }

    public static class ShortLifeSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public ShortLifeSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.2F, 0.3F), Mth.randomBetweenInclusive(level.random, 4, 12), spriteSet);
        }
    }

    public static class LongLifeSoulSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        static final float redMin = 0.05f;
        static final float redDecayRate = 0.98f;
        static final float greenMin = 0.79f;
        static final float greenDecayRate = 0.95f;
        static final float blueMin = 0.82f;
        static final float blueDecayRate = 0.935f;

        public LongLifeSoulSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.8F, 0.9F), Mth.randomBetweenInclusive(level.random, 80, 120), spriteSet, redMin, redDecayRate, greenMin, greenDecayRate, blueMin, blueDecayRate);
        }
    }

    public static class ShortLifeSoulSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        static final float redMin = 0.05f;
        static final float redDecayRate = 0.98f;
        static final float greenMin = 0.79f;
        static final float greenDecayRate = 0.95f;
        static final float blueMin = 0.82f;
        static final float blueDecayRate = 0.935f;

        public ShortLifeSoulSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.2F, 0.3F), Mth.randomBetweenInclusive(level.random, 4, 12), spriteSet, redMin, redDecayRate, greenMin, greenDecayRate, blueMin, blueDecayRate);
        }
    }
}
