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
                this.rCol *= this.rCol > 0.95 ? 0.985f * colourDecayFactor : 1f;
                this.gCol *= this.gCol > 0.8 ? 0.984f * colourDecayFactor : 1f;
                this.bCol *= this.bCol > 0.35 ? 0.98f * colourDecayFactor : 1f;
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

    public static class FlyingSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public FlyingSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.8F, 0.9F), (int)(64. / ((double)level.random.nextFloat() * 0.8 + 0.2)) + 2, spriteSet);
        }
    }

    public static class FloatingSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public FloatingSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.2F, 0.3F), Mth.randomBetweenInclusive(level.random, 3, 12), spriteSet);
        }
    }
}
