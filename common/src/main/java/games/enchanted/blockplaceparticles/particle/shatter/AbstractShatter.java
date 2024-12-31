package games.enchanted.blockplaceparticles.particle.shatter;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.blockplaceparticles.particle.ModParticleRenderTypes;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import games.enchanted.blockplaceparticles.util.TextureHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class AbstractShatter extends Particle {
    protected final float slice0X;
    protected final float slice0Y;
    protected final float slice1X;
    protected final float slice1Y;
    protected final float uvScale;
    protected final float uvOffset;
    protected final boolean inverseSlicePositions;
    protected TextureAtlasSprite sprite;

    protected AbstractShatter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.sprite = TextureHelpers.getDebugSprite();

        int spriteWidth = this.sprite.contents().width();
        int randomSize = MathHelpers.randomBetween(3,5);
        int randomSizeThird = randomSize / 3;
        this.uvOffset = (float) MathHelpers.randomBetween(0, spriteWidth - randomSize) / spriteWidth;
        this.uvScale = (float) randomSize / spriteWidth;
        this.slice0X = (float) MathHelpers.randomBetween(1, randomSizeThird) / randomSize;
        this.slice0Y = (float) MathHelpers.randomBetween(2, randomSizeThird) / randomSize;
        this.slice1X = (float) MathHelpers.randomBetween((randomSizeThird * 2) + 1, randomSize - 1) / randomSize;
        this.slice1Y = (float) MathHelpers.randomBetween((randomSizeThird * 2) - 1, randomSize - 1) / randomSize;

        this.inverseSlicePositions = level.random.nextBoolean();
        this.roll = level.random.nextFloat() >= 0.5f ? 0f : (float) Math.toRadians(90);
        this.oRoll = this.roll;

        setInitialVelocity(xSpeed, ySpeed, zSpeed, 0.1f);

        this.gravity = MathHelpers.randomBetween(0.75f, 0.9f);
        this.lifetime = MathHelpers.randomBetween(10, 50);
    }

    protected void setInitialVelocity(double xSpeed, double ySpeed, double zSpeed, float variance) {
        this.xd = xSpeed + ((level.random.nextFloat() * variance) - (variance / 2));
        this.yd = ySpeed + ((level.random.nextFloat() * variance) - (variance / 2));
        this.zd = zSpeed + ((level.random.nextFloat() * variance) - (variance / 2));
    }

    @Override
    public void tick() {
        this.xd *= 0.95f;
        this.yd *= 0.95f;
        this.zd *= 0.95f;
        super.tick();
    }

    protected void renderTick(float partialTicks) {
        float percentageAge = (float) this.age / this.lifetime;
        if(percentageAge > 0.8) {
            float finalA = (Mth.lerp(partialTicks, this.age, this.age + 0.5f) / this.lifetime) - 0.8f;
            this.alpha = Math.abs(1 - (finalA * 5f));
        }
    }

    protected float getParticleScale(float partialTicks) {
        return this.uvScale;
    }

    protected abstract Direction getParticleFacingDirection();

    private SingleQuadParticle.FacingCameraMode getFacingMode(@Nullable Direction facingDirection) {
        switch (facingDirection) {
            case NORTH -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(270), 0f));
            }
            case EAST -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(180), 0f));
            }
            case SOUTH -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), 0f));
            }
            case WEST -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, 0f, 0f));
            }
            case UP -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), (float) Math.toRadians(90)));
            }
            case DOWN -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), (float) Math.toRadians(-90)));
            }
            case null, default -> {
                return SingleQuadParticle.FacingCameraMode.LOOKAT_XYZ;
            }
        }
    }

    @Override
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float partialTicks) {
        this.renderTick(partialTicks);
        Quaternionf quaternionf = new Quaternionf();
        this.getFacingMode(this.getParticleFacingDirection()).setRotation(quaternionf, camera, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }
        this.renderQuads(vertexConsumer, camera, quaternionf, partialTicks);
    };

    protected void renderQuads(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z());
        int lightColour = this.getLightColor(partialTicks);

        this.renderSlice0(buffer, quaternion, x, y, z, lightColour, partialTicks);
        this.renderSlice1(buffer, quaternion, x, y, z, lightColour, partialTicks);
        this.renderSlice2(buffer, quaternion, x, y, z, lightColour, partialTicks);
    }

    protected float getScaledUVCoord(float uv) {
        return uv * this.uvScale + this.uvOffset;
    }

    protected void renderSlice0(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, int lightColour, float partialTicks) {
        float scale = this.getParticleScale(partialTicks);
        float u0 = this.sprite.getU(this.getScaledUVCoord(0));
        float u1 = this.sprite.getU(this.getScaledUVCoord(this.slice0X));
        float v0 = this.sprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 - this.slice0Y : 0));
        float v1 = this.sprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 : this.slice0Y));

        this.renderVertex(buffer, quaternion, x, y, z, this.slice0X, this.inverseSlicePositions ? 0 : 1 - this.slice0Y, u1, v1, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z, this.slice0X, this.inverseSlicePositions ? this.slice0Y : 1,     u1, v0, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z,     0, this.inverseSlicePositions ? this.slice0Y : 1,     u0, v0, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z,     0, this.inverseSlicePositions ? 0 : 1 - this.slice0Y, u0, v1, lightColour, scale);
    }

    protected void renderSlice1(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, int lightColour, float partialTicks) {
        float scale = this.getParticleScale(partialTicks);
        float u0 = this.sprite.getU(this.getScaledUVCoord(this.slice0X));
        float u1 = this.sprite.getU(this.getScaledUVCoord(this.slice1X));
        float v0 = this.sprite.getV(this.getScaledUVCoord(0));
        float v1 = this.sprite.getV(this.getScaledUVCoord(1));

        this.renderVertex(buffer, quaternion, x, y, z, this.slice1X, 0, u1, v1, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z, this.slice1X, 1, u1, v0, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z, this.slice0X, 1, u0, v0, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z, this.slice0X, 0, u0, v1, lightColour, scale);
    }

    protected void renderSlice2(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, int lightColour, float partialTicks) {
        float scale = this.getParticleScale(partialTicks);
        float u0 = this.sprite.getU(this.getScaledUVCoord(this.slice1X));
        float u1 = this.sprite.getU(this.getScaledUVCoord(1));
        float v0 = this.sprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 0 : this.slice1Y));
        float v1 = this.sprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 - this.slice1Y : 1));

        this.renderVertex(buffer, quaternion, x, y, z,     1, this.inverseSlicePositions ? this.slice1Y : 0,     u1, v1, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z,     1, this.inverseSlicePositions ? 1 : 1 - this.slice1Y, u1, v0, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z, this.slice1X, this.inverseSlicePositions ? 1 : 1 - this.slice1Y, u0, v0, lightColour, scale);
        this.renderVertex(buffer, quaternion, x, y, z, this.slice1X, this.inverseSlicePositions ? this.slice1Y : 0,     u0, v1, lightColour, scale);
    }

    protected void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float u, float v, int packedLight, float scale) {
        xOffset -= 0.5f;
        yOffset -= 0.5f;
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(scale).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(packedLight);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.BACKFACE_TERRAIN_PARTICLE;
    }
}
