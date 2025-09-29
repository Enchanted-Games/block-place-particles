package games.enchanted.eg_particle_interactions.common.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.eg_particle_interactions.common.shapes.QuadFaceShape;
import games.enchanted.eg_particle_interactions.common.shapes.ShapeDefinitions;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

//? if minecraft: > 1.21.8 {
import games.enchanted.eg_particle_interactions.common.rendering.state.CustomParticleGeometryRenderState;
import games.enchanted.eg_particle_interactions.common.util.render.StateAndLayer;
//?}

import java.lang.Math;

public abstract class StretchyBouncyShapeParticle extends BouncyParticle {
    protected double prevPrevX;
    protected double prevPrevY;
    protected double prevPrevZ;
    private QuadFaceShape particleShape;
    /**
     * The scale that the particle will be rendered at
     */
    protected Vector3f particleShapeScale;
    protected float prevPitch;
    protected float prevYaw;

    /**
     * A 3d cube particle that stretches between its current and previous position when moving, this particle also has bounce physics
     * Set {@link #physics_canBounce} to false in your particle constructor to disable bouncing
     *
     * @param level  level
     * @param x      x pos
     * @param y      y pos
     * @param z      z pos
     * @param xSpeed x velocity
     * @param ySpeed y velocity
     * @param zSpeed z velocity
     */
    protected StretchyBouncyShapeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;
        this.speedUpWhenYMotionIsBlocked = true;

        this.particleShapeScale = new Vector3f(1);
        this.setShape(ShapeDefinitions.CUBE);
    }

    @Override
    public void tick() {
        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;

        super.tick();
    }

    /**
     * Sets the shape that the particle will use to render
     *
     * @param newShape the new shape
     */
    protected void setShape(QuadFaceShape newShape) {
        this.particleShape = newShape;
    }

    protected boolean isParticleMoving() {
        return this.getTotalVelocity() < 0.001;
    }

    @Override
    public @NotNull BillboardMode getBillboardMode() {
        return BillboardMode.FIXED;
    }

    @Override
    protected void adjustPositionBeforeExtraction(
        //? if minecraft: <= 1.21.8 {
        /*VertexConsumer consumer,
        *///?} else {
        CustomParticleGeometryRenderState state,
         //?}
        Camera camera, Quaternionf quaternionf, float partialTicks
    ) {
        Vector3f cameraPosition = camera.getPosition().toVector3f();

        float xPos = (float) Mth.lerp(partialTicks, this.xo, this.x);
        float yPos = (float) Mth.lerp(partialTicks, this.yo, this.y);
        float zPos = (float) Mth.lerp(partialTicks, this.zo, this.z);
        Vector3f pos = new Vector3f(xPos, yPos, zPos).sub(cameraPosition);
        float prevXPos = (float) Mth.lerp(partialTicks, this.prevPrevX, this.xo);
        float prevYPos = (float) Mth.lerp(partialTicks, this.prevPrevY, this.yo);
        float prevZPos = (float) Mth.lerp(partialTicks, this.prevPrevZ, this.zo);
        Vector3f prevPos = new Vector3f(prevXPos, prevYPos, prevZPos).sub(cameraPosition);

        //? if minecraft: > 1.21.8 {
        StateAndLayer consumer = new StateAndLayer(state, this.getLayer());
        //?}
        this.extractShapeGeometry(consumer, pos, prevPos, partialTicks);
    }

    private void extractShapeGeometry(
        //? if minecraft: <= 1.21.8 {
        /*VertexConsumer consumer,
        *///?} else {
        StateAndLayer consumer,
         //?}
        Vector3f pos, Vector3f prevPos, float partialTicks
    ) {
        float cuboidSize = this.getScale();
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightColor(partialTicks);

        Vector3f normalisedMovementDir = new Vector3f(pos).sub(prevPos).normalize();
        float pitch = (float) Math.toDegrees(Math.asin(normalisedMovementDir.y));
        if(!Float.isFinite(pitch)) pitch = prevPitch;
        prevPitch = pitch;

        float yaw = (float) Math.toDegrees(Math.atan2(normalisedMovementDir.x, normalisedMovementDir.z));
        if(!Float.isFinite(yaw)) yaw = prevYaw;
        prevYaw = yaw;

        Vector3f shapePos = MathHelpers.getPosBetween3DPoints(pos, prevPos);
        Vector3f shapeScale = new Vector3f(1, Math.max(Math.abs(MathHelpers.getDistanceBetweenVectors(pos, prevPos) * 40), 1), 1).mul(this.particleShapeScale);
        Vector3f shapeRotation = new Vector3f(-(pitch - 90), yaw, 0);
        this.particleShape.extractShape(
            consumer,
            new Vector2f[]{new Vector2f(u0, v0), new Vector2f(u1, v1)},
            shapePos,
            shapeScale,
            shapeRotation,
            cuboidSize,
            lightColor,
            ColourUtil.ARGBfloats_to_ARGB(this.alpha, this.rCol, this.gCol, this.bCol)
        );
    }
}
