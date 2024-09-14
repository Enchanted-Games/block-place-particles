package games.enchanted.blockplaceparticles.particle.spark;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.List;

public class FlyingSpark extends TextureSheetParticle {
    private double spark_xVelocityBeforeBounce;
    private double spark_startingYVelocity;
    private double spark_zVelocityBeforeBounce;
    private int spark_floorBounces = 0;
    private final boolean spark_canBounce = true;
    private int spark_ticksAlive = 0;
    private double prevPrevX;
    private double prevPrevY;
    private double prevPrevZ;

    protected FlyingSpark(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.setSprite(spriteSet.get(this.random.nextInt(12), 12));
        this.gravity = Mth.randomBetween(this.random, 0.8F, 0.9F);
        this.friction = 1.0F;
        this.xd = spark_xVelocityBeforeBounce = (xSpeed / 2) + (Math.random() * 4.0 - 2.0) * 0.05000000074505806;
        this.yd = spark_startingYVelocity = (ySpeed / 2) + (Math.random() * 4.0 - 2.0) * 0.05000000074505806;
        this.zd = spark_zVelocityBeforeBounce = (zSpeed / 2) + (Math.random() * 4.0 - 2.0) * 0.05000000074505806;

        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;

        this.rCol = this.gCol = 1;
        this.bCol = 0.9f;

        this.lifetime = (int)(64. / ((double)this.random.nextFloat() * 0.8 + 0.2)) + 2;

        float particleSize = (this.random.nextBoolean() ? 0.025F : 0.03F);
        this.setSize(particleSize, particleSize);
        this.quadSize = particleSize;
    }

    private float getTotalVelocity() {
        return (float) Math.abs(this.xd + this.yd + this.zd);
    }

    private float getTimeMultiplier(float v) {
        float d = Math.abs(v) * 500;
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

        return spark_ticksAlive <= ticksToStartDarkeningAt ? 1f : 0.85f;
    }

    @Override
    public void tick() {
        spark_ticksAlive++;
        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;

        if(age > 0) {
            float totalVelocity = getTotalVelocity();

            double xVel = this.xd;
            double yVel = this.yd;
            double zVel = this.zd;
            if (this.hasPhysics && xVel * xVel + yVel * yVel + zVel * zVel < Mth.square(100)) {
                Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(xVel, yVel, zVel), this.getBoundingBox(), this.level, List.of());
                if(spark_canBounce) {
                    spark_xVelocityBeforeBounce *= -0.8;
                    spark_zVelocityBeforeBounce *= -0.8;
                    this.xd = vec3.x == 0.0 ? spark_xVelocityBeforeBounce : vec3.x;
                    this.zd = vec3.z == 0.0 ? spark_zVelocityBeforeBounce : vec3.z;
                }
            }
            if(this.onGround) {
                spark_floorBounces += 1;
                double verticalBounceDecay = (double) spark_floorBounces / 2;
                this.yd = Math.abs(spark_startingYVelocity) * Math.abs(1 - verticalBounceDecay);
            }
            if(this.onGround && Math.abs(this.yd) < 0.005) {
                this.age = this.lifetime - Mth.randomBetweenInclusive(this.random, 6, 12);
            }

            if(!(this.age >= this.lifetime - 14)) {
                this.rCol *= this.rCol > 0.95 ? 0.985f * getTimeMultiplier(totalVelocity) : 1f;
                this.gCol *= this.gCol > 0.8 ? 0.984f * getTimeMultiplier(totalVelocity) : 1f;
                this.bCol *= this.bCol > 0.35 ? 0.98f * getTimeMultiplier(totalVelocity) : 1f;
            }
        }

        super.tick();
    }

    @Override
    protected void renderRotatedQuad(@NotNull VertexConsumer consumer, @NotNull Camera camera, @NotNull Quaternionf quaternionf, float d) {
        Vec3 cameraPosition = camera.getPosition();
        float xPos = (float) ((Mth.lerp(d,  this.xo, this.x) - cameraPosition.x()) );
        float yPos = (float) ((Mth.lerp(d,  this.yo, this.y) - cameraPosition.y()) );
        float zPos = (float) ((Mth.lerp(d,  this.zo, this.z) - cameraPosition.z()) );
        Vec3 pos = new Vec3(xPos, yPos, zPos);
        float prevXPos = (float) ((Mth.lerp(d,  this.prevPrevX, this.xo) - cameraPosition.x()) );
        float prevYPos = (float) ((Mth.lerp(d,  this.prevPrevY, this.yo) - cameraPosition.y()) );
        float prevZPos = (float) ((Mth.lerp(d,  this.prevPrevZ, this.zo) - cameraPosition.z()) );
        Vec3 prevPos = new Vec3(prevXPos, prevYPos, prevZPos);
        Vec3 len = pos.vectorTo(pos);
        this.renderRotatedQuad(consumer, quaternionf, xPos, yPos, zPos, d);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float f) {
        return 240;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}
