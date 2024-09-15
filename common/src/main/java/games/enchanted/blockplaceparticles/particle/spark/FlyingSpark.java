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
import org.joml.Vector3f;

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
        this.xd = spark_xVelocityBeforeBounce = (xSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806;
        this.yd = spark_startingYVelocity = (ySpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806;
        this.zd = spark_zVelocityBeforeBounce = (zSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806;

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
    public int getLightColor(float f) {
        return 240;
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        if(this.prevPrevX == this.x && this.prevPrevY == this.y && this.prevPrevZ == this.z) {
            return FacingCameraMode.LOOKAT_XYZ;
        }
        return (quaternionf, camera, d) -> quaternionf.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
    }

    @Override
    protected void renderRotatedQuad(@NotNull VertexConsumer consumer, @NotNull Camera camera, @NotNull Quaternionf quaternionf, float d) {
        Vec3 cameraPosition = camera.getPosition();
        float xPos = (float) ((Mth.lerp(d,  this.xo, this.x) - cameraPosition.x()) );
        float yPos = (float) ((Mth.lerp(d,  this.yo, this.y) - cameraPosition.y()) );
        float zPos = (float) ((Mth.lerp(d,  this.zo, this.z) - cameraPosition.z()) );
        Vector3f pos = new Vector3f(xPos, yPos, zPos);
        float prevXPos = (float) ((Mth.lerp(d,  this.prevPrevX, this.xo) - cameraPosition.x()) );
        float prevYPos = (float) ((Mth.lerp(d,  this.prevPrevY, this.yo) - cameraPosition.y()) );
        float prevZPos = (float) ((Mth.lerp(d,  this.prevPrevZ, this.zo) - cameraPosition.z()) );
        Vector3f prevPos = new Vector3f(prevXPos, prevYPos, prevZPos);
        this.renderSparkQuads(consumer, quaternionf, pos, prevPos, d);
    }

    private void renderSparkQuads(@NotNull VertexConsumer consumer, @NotNull Quaternionf quaternionf, Vector3f pos, Vector3f prevPos, float d) {
        float quadSize = this.getQuadSize(d);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightColor(d);

        float yDiff = 0f;

        for (int i = 0; i < 4; i++) {
            Quaternionf q = new Quaternionf(quaternionf).rotateY((float) Math.toRadians(i * 90));
            this.renderVertex(consumer, q, prevPos.x, prevPos.y + yDiff, prevPos.z, 1.0f, -1.0f, quadSize, u1, v1, lightColor);
            this.renderVertex(consumer, q, pos.x, pos.y - yDiff, pos.z, 1.0f, 1.0f, quadSize, u1, v0, lightColor);
            this.renderVertex(consumer, q, pos.x, pos.y + yDiff, pos.z, -1.0f, 1.0f, quadSize, u0, v0, lightColor);
            this.renderVertex(consumer, q, prevPos.x, prevPos.y - yDiff, prevPos.z, -1.0f, -1.0f, quadSize, u0, v1, lightColor);
        }
    }

    private void renderVertex(VertexConsumer consumer, Quaternionf quaternionf, float x, float y, float z, float width, float height, float sizeMultiplier, float u, float v, int lightColor) {
        Vector3f vertexPos = new Vector3f(width, height, 0.0f).rotate(quaternionf).mul(sizeMultiplier).add(x, y, z);
        consumer.addVertex(vertexPos.x(), vertexPos.y(), vertexPos.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lightColor);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
