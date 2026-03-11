package games.enchanted.eg_particle_interactions.common.particle.types.physics;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.debug.ParticleDebugShapes;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.ParticleAccessor;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.shapes.QuadFaceShape;
import games.enchanted.eg_particle_interactions.common.shapes.ShapeDefinitions;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

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
     */
    protected StretchyBouncyShapeParticle(ParticleContext context, ParticleAppearance appearance, ParticleConfig config, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, config, x, y, z, xSpeed, ySpeed, zSpeed);
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
    protected void adjustPositionBeforeExtraction(QuadConsumer consumer, Camera camera, Quaternionf quaternionf, float partialTicks) {
        Vector3f cameraPosition = camera.position().toVector3f();

        float xPos = (float) Mth.lerp(partialTicks, this.xo, this.x);
        float yPos = (float) Mth.lerp(partialTicks, this.yo, this.y);
        float zPos = (float) Mth.lerp(partialTicks, this.zo, this.z);
        Vector3f pos = new Vector3f(xPos, yPos, zPos).sub(cameraPosition);
        float prevXPos = (float) Mth.lerp(partialTicks, this.prevPrevX, this.xo);
        float prevYPos = (float) Mth.lerp(partialTicks, this.prevPrevY, this.yo);
        float prevZPos = (float) Mth.lerp(partialTicks, this.prevPrevZ, this.zo);
        Vector3f prevPos = new Vector3f(prevXPos, prevYPos, prevZPos).sub(cameraPosition);

        this.extractShapeGeometry(consumer, pos, prevPos, partialTicks);

        if (GeneralOptions.DEBUG_PARTICLE_TICK_BOUNDING_BOXES.getValue()) {
            ParticleDebugShapes.particlePosition(this.x, this.y, this.z, ParticleDebugShapes.PARTICLE_TICK_POSITION);

            ParticleDebugShapes.box(
                this.getBoundingBox(),
                ((ParticleAccessor) this).eg_particle_interactions$getStoppedByCollision() ? ParticleDebugShapes.PARTICLE_BOUNDING_BOX_STOPPED : ParticleDebugShapes.PARTICLE_BOUNDING_BOX
            );
        }
        if (GeneralOptions.DEBUG_PARTICLE_RENDER_BOUNDING_BOXES.getValue()) {
            ParticleDebugShapes.particlePosition(xPos, yPos, zPos, ParticleDebugShapes.PARTICLE_RENDER_POSITION);
            ParticleDebugShapes.particlePosition(prevXPos, prevYPos, prevZPos, ParticleDebugShapes.PARTICLE_PREV_RENDER_POSITION);

            ParticleDebugShapes.box(this.getCullingBox(partialTicks), ParticleDebugShapes.PARTICLE_CULLING_BOX);
        }
    }

    private void extractShapeGeometry(QuadConsumer consumer, Vector3f pos, Vector3f prevPos, float partialTicks) {
        float cuboidSize = this.getLerpedScale(partialTicks);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightmapCoords(partialTicks);

        Vector3f normalisedMovementDir = new Vector3f(pos).sub(prevPos).normalize();
        float pitch = (float) Math.toDegrees(Math.asin(normalisedMovementDir.y));
        if (!Float.isFinite(pitch)) pitch = prevPitch;
        prevPitch = pitch;

        float yaw = (float) Math.toDegrees(Math.atan2(normalisedMovementDir.x, normalisedMovementDir.z));
        if (!Float.isFinite(yaw)) yaw = prevYaw;
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
            ColourUtil.ARGBfloats_to_ARGB(
                this.getLerpedAlpha(partialTicks),
                this.getLerpedRed(partialTicks),
                this.getLerpedGreen(partialTicks),
                this.getLerpedBlue(partialTicks)
            )
        );
    }

    @Override
    public @NotNull AABB getCullingBox(float partialTicks) {
        // expand the culling box by the size of the particle and move it to the middle of the current pos and previous pos
        Vec3 pos1 = new Vec3(
            Mth.lerp(partialTicks, this.xo, this.x),
            Mth.lerp(partialTicks, this.yo, this.y),
            Mth.lerp(partialTicks, this.zo, this.z)
        );
        Vec3 pos2 = new Vec3(
            Mth.lerp(partialTicks, this.prevPrevX, this.xo),
            Mth.lerp(partialTicks, this.prevPrevY, this.yo),
            Mth.lerp(partialTicks, this.prevPrevZ, this.zo)
        );

        return new AABB(pos1, pos2).inflate(this.getLerpedScale(partialTicks));
    }


    //? if neoforge && minecraft: <= 1.21.8 {
    /*@Override
    public @NotNull AABB getRenderBoundingBox(float partialTicks) {
        // expand the culling box by the size of the particle and move it to the middle of the current pos and previous pos
        double diffX = this.x - this.xo;
        double diffY = this.y - this.yo;
        double diffZ = this.z - this.zo;
        return super.getRenderBoundingBox(partialTicks).move(-diffX / 2, -diffY / 2, -diffZ / 2).inflate( Math.abs(new Vec3(this.x, this.y, this.z).distanceTo(new Vec3(this.xo, this.yo, this.zo)) / 2 ));
    }
    *///?}
}
