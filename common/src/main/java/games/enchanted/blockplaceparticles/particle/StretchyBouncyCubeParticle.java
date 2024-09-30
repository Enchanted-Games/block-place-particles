package games.enchanted.blockplaceparticles.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.blockplaceparticles.shapes.ShapeDefinitions;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.lang.Math;

public abstract class StretchyBouncyCubeParticle extends BouncyParticle {
    private double prevPrevX;
    private double prevPrevY;
    private double prevPrevZ;

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
    protected StretchyBouncyCubeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;
    }

    @Override
    public void tick() {
        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;

        super.tick();
    }

    protected boolean isParticleMoving() {
        return this.getTotalVelocity() < 0.001;
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        return (quaternionf, camera, d) -> quaternionf.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
    }

    @Override
    protected void renderRotatedQuad(@NotNull VertexConsumer consumer, @NotNull Camera camera, @NotNull Quaternionf quaternionf, float d) {
        Vector3f cameraPosition = camera.getPosition().toVector3f();

        float xPos = (float) Mth.lerp(d, this.xo, this.x);
        float yPos = (float) Mth.lerp(d, this.yo, this.y);
        float zPos = (float) Mth.lerp(d, this.zo, this.z);
        Vector3f pos = new Vector3f(xPos, yPos, zPos).sub(cameraPosition);
        float prevXPos = (float) Mth.lerp(d, this.prevPrevX, this.xo);
        float prevYPos = (float) Mth.lerp(d, this.prevPrevY, this.yo);
        float prevZPos = (float) Mth.lerp(d, this.prevPrevZ, this.zo);
        Vector3f prevPos = new Vector3f(prevXPos, prevYPos, prevZPos).sub(cameraPosition);

        this.renderCubeGeometry(consumer, quaternionf, pos, prevPos, d);
    }

    private void renderCubeGeometry(@NotNull VertexConsumer consumer, Quaternionf quaternionf, Vector3f pos, Vector3f prevPos, float d) {
        float cuboidSize = this.getQuadSize(d);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightColor(d);

        Vector3f normalisedMovementDir = new Vector3f(pos).sub(prevPos).normalize();
        float pitch = (float) Math.toDegrees(Math.asin(normalisedMovementDir.y));
        if(!Float.isFinite(pitch)) pitch = 0;
        float yaw = (float) Math.toDegrees(Math.atan2(normalisedMovementDir.x, normalisedMovementDir.z));
        if(!Float.isFinite(yaw)) yaw = 0;

        Vector3f shapeScale = new Vector3f(1, Math.max(Math.abs(MathHelpers.getDistanceBetweenVectors(pos, prevPos) * 40), 1), 1);
        Vector3f shapeRotation = new Vector3f(-(pitch - 90), yaw, 0);
        ShapeDefinitions.VERTICAL_PLUS.renderShapeWithRotation(consumer, new Vector2f[]{new Vector2f(u0, v0), new Vector2f(u1, v1)}, prevPos, shapeScale, quaternionf, shapeRotation, cuboidSize, lightColor, new Vector4f(this.rCol, this.gCol, this.bCol, this.alpha));
    }
}
