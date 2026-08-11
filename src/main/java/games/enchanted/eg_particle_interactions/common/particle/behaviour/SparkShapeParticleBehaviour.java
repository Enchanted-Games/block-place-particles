package games.enchanted.eg_particle_interactions.common.particle.behaviour;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.debug.ParticleDebugShapes;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.ParticleAccessor;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.billboard.FacingCameraMode;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.shapes.QuadFaceShape;
import games.enchanted.eg_particle_interactions.common.shapes.ShapeDefinitions;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class SparkShapeParticleBehaviour extends ParticleInteractionsParticle {
    protected double prevPrevX;
    protected double prevPrevY;
    protected double prevPrevZ;

    private final QuadFaceShape particleShape;
    protected Vector3f particleShapeScale;

    protected float prevPitch;
    protected float prevYaw;

    protected SparkShapeParticleBehaviour(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double y, double z, double ySpeed, double zSpeed, double xSpeed) {
        super(components, appearance, context, x, y, z, ySpeed, zSpeed, xSpeed);

        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;

        this.particleShape = ShapeDefinitions.VERTICAL_CROSS;
        this.particleShapeScale = new Vector3f(1);
        this.particleShapeScale.x = Mth.randomBetween(level.getRandom(), 0.4f, 1.1f);
        this.particleShapeScale.z = Mth.randomBetween(level.getRandom(), 0.4f, 1.1f);
    }

    @Override
    public void tick() {
        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;

        super.tick();
    }

    protected Vector3f getShapeOffset() {
        Vector3f shapeOffset = new Vector3f(0);
        this.modelOffset.div(16, shapeOffset);
        return shapeOffset;
    }

    @Override
    protected void adjustPositionBeforeExtraction(QuadConsumer consumer, Camera camera, Quaternionf quaternionf, Vec3 lerpedPos, float partialTicks) {
        Vector3f cameraPosition = camera.position().toVector3f();

        double xPos = lerpedPos.x();
        double yPos = lerpedPos.y();
        double zPos = lerpedPos.z();
        Vector3f pos = new Vector3f((float) xPos, (float) yPos, (float) zPos).sub(cameraPosition);
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
        if (!Float.isFinite(pitch)) pitch = this.prevPitch;
        this.prevPitch = pitch;

        float yaw = (float) Math.toDegrees(Math.atan2(normalisedMovementDir.x, normalisedMovementDir.z));
        if (!Float.isFinite(yaw)) yaw = this.prevYaw;
        this.prevYaw = yaw;

        Vector3f shapePos = MathHelper.getPosBetween3DPoints(pos, prevPos).add(this.getShapeOffset());
        Vector3f shapeScale = new Vector3f(1, Math.max(Math.abs(MathHelper.getDistanceBetweenVectors(pos, prevPos) * 40), 1), 1)
            .mul(this.particleShapeScale)
            .mul(this.getLerpedScale(partialTicks));
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
    public AABB getCullingBox(float partialTicks) {
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

        return new AABB(pos1, pos2).move(this.getShapeOffset()).inflate(this.getLerpedScale(partialTicks));
    }

    public static class Provider implements ParticleBehaviourProvider {
        @Override
        public @Nullable Particle createParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double z, double y, double xSpeed, double ySpeed, double zSpeed) {
            return new SparkShapeParticleBehaviour(components, appearance, context, x, y, z, ySpeed, zSpeed, xSpeed);
        }
    }
}
