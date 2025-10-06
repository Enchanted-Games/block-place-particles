package games.enchanted.eg_particle_interactions.common.particle.shatter;

import games.enchanted.eg_particle_interactions.common.particle.compat.CustomGeometryParticle;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import games.enchanted.eg_particle_interactions.common.util.render.RenderingUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

//? if minecraft: > 1.21.8 {
import games.enchanted.eg_particle_interactions.common.rendering.particle.state.CustomParticleGeometryRenderState;
import games.enchanted.eg_particle_interactions.common.util.render.StateAndLayer;
import net.minecraft.client.particle.SingleQuadParticle;
//?} else {
/*import com.mojang.blaze3d.vertex.VertexConsumer;
*///?}

public abstract class AbstractShatter extends CustomGeometryParticle {
    protected final float slice0X;
    protected final float slice0Y;
    protected final float slice1X;
    protected final float slice1Y;
    protected final float uvScale;
    protected final float uvOffset;
    protected final boolean inverseSlicePositions;

    protected AbstractShatter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, TextureHelpers.getDebugSprite());

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
        this.roll = (float) Math.toRadians(MathHelpers.randomBetween(0, 3) * 90);
        this.oRoll = this.roll;

        setInitialVelocity(xSpeed, ySpeed, zSpeed, 0.1f);

        this.gravity = MathHelpers.randomBetween(0.75f, 0.9f);
        this.lifetime = MathHelpers.randomBetween(5, 25);

//        this.gravity = MathHelpers.randomBetween(0.1f, 0.25f);
//        this.lifetime = MathHelpers.randomBetween(5, 14);
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

    @Override
    protected void renderTick(float partialTicks) {
        float percentageAge = (float) this.age / this.lifetime;
        if(percentageAge > 0.8) {
            float finalA = 1 - (((Mth.lerp(partialTicks, this.age, this.age + 1f) / this.lifetime) - 0.8f) * 5f);
            if(finalA < 0) {
                this.alpha = 0f;
                return;
            }
            this.alpha = finalA;
        }
    }

    protected float getParticleScale(float partialTicks) {
        return this.uvScale;
    }

    protected @Nullable Direction getParticleFacingDirection() {
        return null;
    }

    @Override
    public BillboardMode getBillboardMode() {
        Direction facingDirection = this.getParticleFacingDirection();
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
                return BillboardMode.XYZ;
            }
        }
    }

    protected float getScaledUVCoord(float uv) {
        return uv * this.uvScale + this.uvOffset;
    }

    @Override
    protected void extractGeometry(
        //? if minecraft: <= 1.21.8 {
        /*VertexConsumer consumer,
        *///?} else {
        CustomParticleGeometryRenderState state,
         //?}
        Quaternionf quaternion, float x, float y, float z, float partialTicks
    ) {
        //? if minecraft: > 1.21.8 {
        SingleQuadParticle.Layer layer = this.getLayer();
        StateAndLayer consumer = new StateAndLayer(state, layer);
        //?}

        float scale = this.getParticleScale(partialTicks);
        int lightColour = getLightColor(partialTicks);

        float u0 = this.sprite.getU(this.getScaledUVCoord(0));
        float u1 = this.sprite.getU(this.getScaledUVCoord(this.slice0X));
        float v0 = this.sprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 - this.slice0Y : 0));
        float v1 = this.sprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 : this.slice0Y));

        //? if minecraft: > 1.21.8 {
        state.startQuad(layer);
        //?}
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, this.slice0X, this.inverseSlicePositions ? 0 : 1 - this.slice0Y, scale, u1, v1, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, this.slice0X, this.inverseSlicePositions ? this.slice0Y : 1,     scale, u1, v0, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z,            0, this.inverseSlicePositions ? this.slice0Y : 1,     scale, u0, v0, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z,            0, this.inverseSlicePositions ? 0 : 1 - this.slice0Y, scale, u0, v1, lightColour);
        //? if minecraft: > 1.21.8 {
        state.finishQuad(layer);
        //?}

        scale = this.getParticleScale(partialTicks);
        u0 = this.sprite.getU(this.getScaledUVCoord(this.slice0X));
        u1 = this.sprite.getU(this.getScaledUVCoord(this.slice1X));
        v0 = this.sprite.getV(this.getScaledUVCoord(0));
        v1 = this.sprite.getV(this.getScaledUVCoord(1));

        //? if minecraft: > 1.21.8 {
        state.startQuad(layer);
        //?}
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, this.slice1X, 0, scale, u1, v1, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, this.slice1X, 1, scale, u1, v0, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, this.slice0X, 1, scale, u0, v0, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, this.slice0X, 0, scale, u0, v1, lightColour);
        //? if minecraft: > 1.21.8 {
        state.finishQuad(layer);
        //?}

        scale = this.getParticleScale(partialTicks);
        u0 = this.sprite.getU(this.getScaledUVCoord(this.slice1X));
        u1 = this.sprite.getU(this.getScaledUVCoord(1));
        v0 = this.sprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 0 : this.slice1Y));
        v1 = this.sprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 - this.slice1Y : 1));

        //? if minecraft: > 1.21.8 {
        state.startQuad(layer);
        //?}
        RenderingUtil.addVertex(consumer, quaternion, x, y, z,            1, this.inverseSlicePositions ? this.slice1Y : 0,     scale, u1, v1, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z,            1, this.inverseSlicePositions ? 1 : 1 - this.slice1Y, scale, u1, v0, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, this.slice1X, this.inverseSlicePositions ? 1 : 1 - this.slice1Y, scale, u0, v0, lightColour);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, this.slice1X, this.inverseSlicePositions ? this.slice1Y : 0,     scale, u0, v1, lightColour);
        //? if minecraft: > 1.21.8 {
        state.finishQuad(layer);
        //?}
    }

    @Override
    protected ParticleLayer getParticleLayer() {
        return ParticleLayer.BACKFACE_TERRAIN;
    }
}
