package games.enchanted.blockplaceparticles.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.blockplaceparticles.shapes.ShapeDefinitions;
import games.enchanted.blockplaceparticles.shapes.VectorShape;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
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

    protected double distanceBetweenPreviousAndCurrentPos() {
        return new Vector3d(this.x, this.y, this.z).distance(this.xo, this.yo, this.zo);
    }
    protected boolean isParticleMoving() {
        return this.distanceBetweenPreviousAndCurrentPos() < 0.001;
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        return (quaternionf, camera, d) -> quaternionf.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
    }

    @Override
    protected void renderRotatedQuad(@NotNull VertexConsumer consumer, @NotNull Camera camera, @NotNull Quaternionf quaternionf, float d) {
        float xPos = (float) Mth.lerp(d, this.xo, this.x);
        float yPos = (float) Mth.lerp(d, this.yo, this.y);
        float zPos = (float) Mth.lerp(d, this.zo, this.z);
        Vector3f pos = new Vector3f(xPos, yPos, zPos);
        float prevXPos = (float) Mth.lerp(d, this.prevPrevX, this.xo);
        float prevYPos = (float) Mth.lerp(d, this.prevPrevY, this.yo);
        float prevZPos = (float) Mth.lerp(d, this.prevPrevZ, this.zo);
        Vector3f prevPos = new Vector3f(prevXPos, prevYPos, prevZPos);

        Vec3 cameraPosition = camera.getPosition();
        Vector3f centerPos = MathHelpers.getPosBetween3DPoints(pos, prevPos).sub(cameraPosition.toVector3f());

        this.renderCubeGeometry(consumer, centerPos, pos, prevPos, d);
    }

    private void renderCubeGeometry(@NotNull VertexConsumer consumer, Vector3f particleCenterPos, Vector3f rawPos, Vector3f rawPrevPos, float d) {
        float cuboidSize = this.getQuadSize(d);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightColor(d);

        float angleX = (float) MathHelpers.angleBetween2DPoints(new Vector2f(rawPos.z, rawPos.y), new Vector2f(rawPrevPos.z, rawPrevPos.y));
        float angleY = (float) MathHelpers.angleBetween2DPoints(new Vector2f(rawPos.x, rawPos.z), new Vector2f(rawPrevPos.x, rawPrevPos.z));

        VectorShape cuboidShape = VectorShape.copyShape(ShapeDefinitions.CUBE).scale(new Vector3f(1, (float) Math.max(Math.abs(this.distanceBetweenPreviousAndCurrentPos()) * 16, 1), 1));
        cuboidShape.renderShapeWithRotation(consumer, new Vector2f[]{new Vector2f(u0, v0), new Vector2f(u1, v1)}, particleCenterPos.x, particleCenterPos.y, particleCenterPos.z, cuboidSize, new Vector3f(angleX < 0 ? -angleX : angleX, angleY, 0), lightColor, new Vector4f(1));
    }
}
