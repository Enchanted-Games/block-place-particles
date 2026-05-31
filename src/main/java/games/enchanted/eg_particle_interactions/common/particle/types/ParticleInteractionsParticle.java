package games.enchanted.eg_particle_interactions.common.particle.types;

import games.enchanted.eg_particle_interactions.common.duck.ParticleDuck;
import games.enchanted.eg_particle_interactions.common.particle.VelocityProvider;
import games.enchanted.eg_particle_interactions.common.particle.behaviour.ParticleBehaviourProvider;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.debug.ParticleDebugShapes;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.ParticleAccessor;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.SpriteCycleMode;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfig;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponents;
import games.enchanted.eg_particle_interactions.common.particle.component.type.*;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.event.EventStack;
import games.enchanted.eg_particle_interactions.common.util.math.IntMathModifier;
import games.enchanted.eg_particle_interactions.common.util.math.Vector3dMathModifier;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomIntProvider;
import games.enchanted.eg_particle_interactions.common.particle.render.ModParticleRenderTypes;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.StateQuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.layer.ParticleLayer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.CustomParticleGeometryRenderState;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ParticleInteractionsParticle extends Particle {
    protected static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0F);
    private static final double MIN_BOUNCE_CUTOFF = 0.05;

    private float scale;
    private float prevScale;
    protected float roll;
    protected float prevRoll;
    protected float billboardYOffset = 0.0f;
    protected float billboardXOffset = 0.0f;
    protected final int minLightEmission;
    protected final float gravityDecay;
    protected final Vec3 velocityDecay;

    protected float bounciness = 0f;
    protected FluidState inFluid = Fluids.EMPTY.defaultFluidState();
    protected FluidState inFluidLastTick = this.inFluid;
    protected boolean onGroundLastTick = false;
    protected Vec3 collisionResult = Vec3.ZERO;

    protected ParticleContext context;
    protected ParticleAppearance appearance;
    protected ParticleLayer layer;

    protected final EventStack lifetimeEventStack;
    protected EventStack appearanceEventStack;

    protected boolean updateSpritesAfterFirstCall = true;
    protected TextureAtlasSprite currentSprite;

    private float rCol = 1.0f;
    private float prevRCol = 1.0f;
    private float gCol = 1.0f;
    private float prevGCol = 1.0f;
    private float bCol = 1.0f;
    private float prevBCol = 1.0f;
    private float alpha = 1.0f;
    private float prevAlpha = 1.0f;

    protected ParticleInteractionsParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, ParticleConfig config, double x, double y, double z) {
        this(components, appearance, context, config, x, y, z, 0, 0, 0);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    protected ParticleInteractionsParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, ParticleConfig config, double x, double y, double z, double ySpeed, double zSpeed, double xSpeed) {
        super(context.level(), x, y, z, xSpeed, ySpeed, zSpeed);
        ClientLevel level = context.level();

        Vec3Component velocityRandomnessComponent = components.getOrFallback(ParticleComponents.VELOCITY_INITIAL_RANDOMNESS, Vec3Component.ZERO);
        this.xd = xSpeed + ((level.getRandom().nextFloat() - 0.5f) * 2f * velocityRandomnessComponent.vec3().x());
        this.yd = ySpeed + ((level.getRandom().nextFloat() - 0.5f) * 2f * velocityRandomnessComponent.vec3().y());
        this.zd = zSpeed + ((level.getRandom().nextFloat() - 0.5f) * 2f * velocityRandomnessComponent.vec3().z());

        Vec3Component velocityDecayComponent = components.getOrFallback(ParticleComponents.VELOCITY_DECAY, Vec3Component.ONE);
        this.velocityDecay = velocityDecayComponent.vec3();

        this.context = context;

        this.setAppearance(appearance);

        FloatProviderComponent gravityComponent = components.getOrFallback(ParticleComponents.GRAVITY_INITIAL, FloatProviderComponent.ZERO);
        this.gravity = gravityComponent.provider().getValue(context);
        FloatProviderComponent gravityDecayComponent = components.getOrFallback(ParticleComponents.GRAVITY_DECAY, FloatProviderComponent.ONE);
        this.gravityDecay = gravityDecayComponent.provider().getValue(context);

        IntProviderComponent lifetimeComponent = components.getOrFallback(ParticleComponents.LIFETIME, new IntProviderComponent(new RandomIntProvider(20, 20)));
        this.lifetime = lifetimeComponent.provider().getValue(context);

        FloatProviderComponent collisionSizeComponent = components.getOrFallback(ParticleComponents.PHYSICS_COLLISION_SIZE, FloatProviderComponent.ZERO);
        float collisionSize = collisionSizeComponent.provider().getValue(context) / 16;
        this.setSize(collisionSize, collisionSize);

        FloatProviderComponent frictionComponent = components.getOrFallback(ParticleComponents.PHYSICS_FRICTION, FloatProviderComponent.ZERO);
        this.friction = frictionComponent.provider().getValue(context);

        FloatProviderComponent bouncinessComponent = components.getOrFallback(ParticleComponents.PHYSICS_BOUNCINESS, FloatProviderComponent.ZERO);
        this.bounciness = bouncinessComponent.provider().getValue(context);


        // 0.1 - 0.2
        this.scale = 0.1f * (this.random.nextFloat() * 0.5f + 0.5f) * 2.0f;
        this.prevScale = scale;

        int[] colour = appearance.colourSource().getARGB(context);
        this.setRGBA(
            (float) colour[1] / 255f,
            (float) colour[2] / 255f,
            (float) colour[3] / 255f,
            (float) colour[0] / 255f
        );

        this.minLightEmission = appearance.lightEmission();

        this.layer = ParticleLayer.fromAppearance(context, appearance);

        BooleanComponent bypassCollisionCheckComponent = components.get(ParticleComponents.PHYSICS_BYPASS_COLLISION_CHECK);
        ((ParticleDuck) this).eg_particle_interactions$setBypassMovementCollisionCheck(bypassCollisionCheckComponent == null ? this.friction < 0.99 : bypassCollisionCheckComponent.value());

        LifetimeEventsComponent lifetimeEventsComponent = components.get(ParticleComponents.LIFETIME_EVENTS);
        this.lifetimeEventStack = new EventStack(lifetimeEventsComponent == null ? List.of() : lifetimeEventsComponent.events(), this);
    }

    public void setAppearance(ParticleAppearance appearance) {
        this.appearance = appearance;
        this.appearanceEventStack = new EventStack(appearance.events(), this);

        this.updateSpritesAfterFirstCall = true;
        this.currentSprite = TextureHelpers.missingParticleSprite();
        this.pickSpriteForAppearance();
    }

    protected SingleQuadParticle.Layer getVanillaLayer() {
        ParticleLayer layer = this.getParticleLayer();
        return layer.vanillaLayer();
    }

    @Override
    public ParticleRenderType getGroup() {
        return ModParticleRenderTypes.PARTICLE_INTERACTIONS;
    }


    public void extract(CustomParticleGeometryRenderState state, Camera camera, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        this.getBillboardMode().rotate(quaternionf, camera, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.prevRoll, this.roll));
        }

        StateQuadConsumer consumer = new StateQuadConsumer(state, this.getVanillaLayer());
        this.adjustPositionBeforeExtraction(consumer, camera, quaternionf, partialTicks);
    }

    protected void adjustPositionBeforeExtraction(QuadConsumer consumer, Camera camera, Quaternionf quaternionf, float partialTicks) {
        Vec3 cameraPosition = camera.position();
        float lerpedX = (float) Mth.lerp(partialTicks, this.xo, this.x);
        float lerpedY = (float) Mth.lerp(partialTicks, this.yo, this.y);
        float lerpedZ = (float) Mth.lerp(partialTicks, this.zo, this.z);
        float x = (float) (lerpedX - cameraPosition.x());
        float y = (float) (lerpedY - cameraPosition.y());
        float z = (float) (lerpedZ - cameraPosition.z());
        this.extractGeometry(consumer, quaternionf, x, y, z, partialTicks);

        if (GeneralOptions.DEBUG_PARTICLE_TICK_BOUNDING_BOXES.getValue()) {
            ParticleDebugShapes.particlePosition(this.x, this.y, this.z, ParticleDebugShapes.PARTICLE_TICK_POSITION);

            ParticleDebugShapes.box(
                this.getBoundingBox(),
                ((ParticleAccessor) this).eg_particle_interactions$getStoppedByCollision() ? ParticleDebugShapes.PARTICLE_BOUNDING_BOX_STOPPED : ParticleDebugShapes.PARTICLE_BOUNDING_BOX
            );

            ParticleDebugShapes.onGroundMarker(this.getBoundingBox(), this.onGround);

            Gizmos.billboardText("Age: " + this.age, new Vec3(lerpedX, lerpedY + 0.2, lerpedZ), TextGizmo.Style.whiteAndCentered());
            Gizmos.billboardText("Lifetime: " + this.lifetime, new Vec3(lerpedX, lerpedY, lerpedZ), TextGizmo.Style.whiteAndCentered());
        }
        if (GeneralOptions.DEBUG_PARTICLE_RENDER_BOUNDING_BOXES.getValue()) {
            ParticleDebugShapes.particlePosition(x + cameraPosition.x(), y + cameraPosition.y(), z + cameraPosition.z(), ParticleDebugShapes.PARTICLE_RENDER_POSITION);

            ParticleDebugShapes.box(this.getCullingBox(partialTicks), ParticleDebugShapes.PARTICLE_CULLING_BOX);
        }
    }

    protected void extractGeometry(QuadConsumer consumer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        int light = this.getLightmapCoords(partialTicks);
        float scale = this.getLerpedScale(partialTicks);
        float r = this.getLerpedRed(partialTicks);
        float g = this.getLerpedGreen(partialTicks);
        float b = this.getLerpedBlue(partialTicks);
        float a = this.getLerpedAlpha(partialTicks);

        consumer.startQuad();
        consumer.addVertex(quaternion, x, y, z, 1.0F + this.billboardXOffset, -1.0F + this.billboardYOffset, scale, getU1(), getV1(), light, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, 1.0F + this.billboardXOffset, 1.0F + this.billboardYOffset, scale, getU1(), getV0(), light, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, -1.0F + this.billboardXOffset, 1.0F + this.billboardYOffset, scale, getU0(), getV0(), light, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, -1.0F + this.billboardXOffset, -1.0F + this.billboardYOffset, scale, getU0(), getV1(), light, r, g, b, a);
        consumer.finishQuad();
    }

    public BillboardMode getBillboardMode() {
        return BillboardMode.XYZ;
    }

    @Override
    public void tick() {
        if(this.removed) return;
        if(this.age == 0) this.forEventStacks(EventStack::particleSpawn);

        this.pickSpriteForAppearance();

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.inFluidLastTick = this.inFluid;
        this.inFluid = this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z));

        if(!this.onGround) {
            this.yd -= 0.04 * this.gravity;
        }

        this.doCollisions();
        this.move(this.xd, this.yd, this.zd);

        if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
            this.xd *= 1.1;
            this.zd *= 1.1;
        }
        if (this.onGround) {
            final float effectiveFriction = 1 - this.friction;
            this.xd *= effectiveFriction;
            this.yd *= effectiveFriction;
            this.zd *= effectiveFriction;
        }

        this.applyGravityAndVelocityDecays();
        this.forEventStacks(EventStack::tick);
    }

    protected void doCollisions() {
        if (this.age > 0 && this.hasPhysics) {
            final double xVel = this.xd;
            final double yVel = this.yd;
            final double zVel = this.zd;
            if (xVel * xVel + yVel * yVel + zVel * zVel > MAXIMUM_COLLISION_VELOCITY_SQUARED) return;

            final boolean xVelInCutoffRange = MathHelpers.isInRange(xVel, -MIN_BOUNCE_CUTOFF, MIN_BOUNCE_CUTOFF);
            final boolean yVelInCutoffRange = MathHelpers.isInRange(yVel, -MIN_BOUNCE_CUTOFF, MIN_BOUNCE_CUTOFF);
            final boolean zVelInCutoffRange = MathHelpers.isInRange(zVel, -MIN_BOUNCE_CUTOFF, MIN_BOUNCE_CUTOFF);

            this.collisionResult = Entity.collideBoundingBox(null, new Vec3(xVel, yVel, zVel), this.getBoundingBox(), this.level, List.of());
            this.xd = this.collisionResult.x == 0.0 && !xVelInCutoffRange ? -xVel * this.bounciness * 0.99999 : xVel;
            this.yd = this.collisionResult.y == 0.0 && !yVelInCutoffRange ? -yVel * this.bounciness * 0.99999 : yVel;
            this.zd = this.collisionResult.z == 0.0 && !zVelInCutoffRange ? -zVel * this.bounciness * 0.99999 : zVel;

            if(
                this.bounciness > 0 && (
                    (!xVelInCutoffRange && this.collisionResult.x == 0.0) ||
                    (!yVelInCutoffRange && this.collisionResult.y == 0.0) ||
                    (!zVelInCutoffRange && this.collisionResult.z == 0.0)
                )
            ) {
                this.forEventStacks(EventStack::particleBounce);
            }
        }
    }

    protected void applyGravityAndVelocityDecays() {
        this.xd *= this.velocityDecay.x();
        this.yd *= this.velocityDecay.y();
        this.zd *= this.velocityDecay.z();

        this.gravity *= this.gravityDecay;
    }

    @Override
    public void move(double xa, double ya, double za) {
        this.onGroundLastTick = this.onGround;
        if (!((ParticleAccessor) this).eg_particle_interactions$getStoppedByCollision()) {
            final double originalYa = ya;
            if (this.hasPhysics && (xa != 0.0 || ya != 0.0 || za != 0.0) && xa * xa + ya * ya + za * za < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
                Vec3 movement = this.collisionResult;
                xa = movement.x;
                ya = movement.y;
                za = movement.z;
            }

            if (xa != 0.0 || ya != 0.0 || za != 0.0) {
                this.setBoundingBox(this.getBoundingBox().move(xa, ya, za));
                this.setLocationFromBoundingbox();
            }

            final double epsilon = 0.00001;
            if (!((ParticleDuck) this).eg_particle_interactions$getBypassMovementCollisionCheck() && Math.abs(originalYa) >= epsilon && Math.abs(ya) < epsilon) {
                ((ParticleDuck) this).eg_particle_interactions$setHasStoppedByCollision(true);
            }

            this.onGround = originalYa != ya && originalYa < 0.0;
        }
    }

    /**
     * Pick sprite based on particle appearance
     */
    public void pickSpriteForAppearance() {
        this.setSpriteForTextureConfig();
    }

    private void setSpriteForTextureConfig() {
        if (this.removed) return;
        if (!this.updateSpritesAfterFirstCall) return;

        TextureConfig textureConfig = this.appearance.textureConfig();
        SpriteCycleMode cycleMode = textureConfig.getSpriteCycleMode(this.context);

        if (cycleMode == SpriteCycleMode.RANDOM_ON_SPAWN) {
            this.updateSpritesAfterFirstCall = false;
            this.setCurrentSprite(textureConfig.getRandom(this.context, this.random));
            return;
        }

        if (cycleMode == SpriteCycleMode.RANDOM_PER_TICK) {
            this.setCurrentSprite(textureConfig.getRandom(this.context, this.random));
            return;
        }

        this.setCurrentSprite(textureConfig.getAt(this.context, this.getAgeForSprite(), this.lifetime));
    }

    protected int getAgeForSprite() {
        return this.age;
    }

    protected void setCurrentSprite(TextureAtlasSprite sprite) {
        this.currentSprite = sprite;
    }


    public AABB getCullingBox(float partialTicks) {
        Vec3 pos = new Vec3(
            Mth.lerp(partialTicks, this.xo, this.x),
            Mth.lerp(partialTicks, this.yo, this.y),
            Mth.lerp(partialTicks, this.zo, this.z)
        );
        float scale = this.getLerpedScale(partialTicks);
        return new AABB(pos.subtract(scale), pos.add(scale));
    }

    @Override
    protected final void setSize(float w, float h) {
        if (w != this.bbWidth || h != this.bbHeight) {
            this.bbWidth = w;
            this.bbHeight = h;
            AABB aabb = this.getBoundingBox();
            double newMinX = (aabb.minX + aabb.maxX - w) / 2.0d;
            double newMinY = aabb.minY - this.bbHeight / 2;
            double newMinZ = (aabb.minZ + aabb.maxZ - w) / 2.0d;
            this.setBoundingBox(new AABB(
                newMinX,
                newMinY,
                newMinZ,
                newMinX + this.bbWidth,
                newMinY + this.bbHeight,
                newMinZ + this.bbWidth
            ));
        }
    }

    @Override
    protected void setLocationFromBoundingbox() {
        AABB aabb = this.getBoundingBox();
        this.x = (aabb.minX + aabb.maxX) / 2.0d;
        this.y = (aabb.minY + aabb.maxY) / 2.0d;
        this.z = (aabb.minZ + aabb.maxZ) / 2.0d;
    }

    /**
     * Wrapper around minecrafts light coords / light color methods for easier multi version support
     *
     * @param partialTick partial tick
     * @return the lightmap coords
     */
    protected int getLightmapCoords(float partialTick) {
        return LightCoordsUtil.lightCoordsWithEmission(this.getLightCoords(partialTick), this.minLightEmission);
    }

    @Override
    protected final int getLightCoords(float a) {
        return super.getLightCoords(a);
    }


    protected float getU0() {
        return this.currentSprite.getU0();
    }

    protected float getU1() {
        return this.currentSprite.getU1();
    }

    protected float getV0() {
        return this.currentSprite.getV0();
    }

    protected float getV1() {
        return this.currentSprite.getV1();
    }


    public float getLerpedRed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevRCol, this.rCol);
    }

    public float getRed() {
        return this.rCol;
    }

    public float getLerpedGreen(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevGCol, this.gCol);
    }

    public float getGreen() {
        return this.gCol;
    }

    public float getLerpedBlue(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevBCol, this.bCol);
    }

    public float getBlue() {
        return this.bCol;
    }

    public float getLerpedAlpha(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevAlpha, this.alpha);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float a) {
        this.setAlpha(a, false);
    }

    public void setAlpha(float a, boolean lerp) {
        if (lerp) {
            this.prevAlpha = this.alpha;
        } else {
            this.prevAlpha = a;
        }
        this.alpha = a;
    }

    public void setRGB(float r, float g, float b) {
        this.setRGBA(r, g, b, this.getAlpha(), false);
    }

    public void setRGBA(float r, float g, float b, float a) {
        this.setRGBA(r, g, b, a, false);
    }

    public void setRGBA(float r, float g, float b, float a, boolean lerp) {
        if (lerp) {
            this.prevRCol = this.rCol;
            this.prevGCol = this.gCol;
            this.prevBCol = this.bCol;
            this.prevAlpha = this.alpha;
        } else {
            this.prevRCol = r;
            this.prevGCol = g;
            this.prevBCol = b;
            this.prevAlpha = a;
        }
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.alpha = a;
    }


    public float getLerpedScale(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevScale, this.scale);
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.setScale(scale, false);
    }

    public void setScale(float scale, boolean lerp) {
        if (lerp) {
            this.prevScale = this.scale;
        } else {
            this.prevScale = scale;
        }
        this.scale = scale;
    }

    protected ParticleLayer getParticleLayer() {
        return this.layer;
    }

    @Override
    public void setLifetime(int lifetime) {
        super.setLifetime(Math.max(0, lifetime));
        this.age = Math.min(this.age, this.lifetime);
        if(this.lifetime <= 0) this.remove();
    }

    protected Vector3d velocityAsVector() {
        return new Vector3d(this.xd, this.yd, this.zd);
    }

    public void modifyVelocity(Vector3dMathModifier modifier) {
        Vector3d delta = modifier.apply(this.velocityAsVector());
        this.xd = delta.x();
        this.yd = delta.y();
        this.zd = delta.z();
    }

    public void modifyLifetime(IntMathModifier modifier) {
        this.setLifetime(modifier.apply(this.getLifetime()));
    }

    public void emit(Emitter emitter, Vector3d positionOffset, VelocityProvider velocityProvider) {
        Vector3d vel = velocityProvider.getVelocity(this.velocityAsVector());
        emitter.spawnParticle(
            this.context,
            this.x + positionOffset.x,
            this.y + positionOffset.y,
            this.z + positionOffset.z,
            vel.x,
            vel.y,
            vel.z
        );
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean isOnGroundOneshot() {
        return this.onGround && !this.onGroundLastTick;
    }

    public boolean isInAirOneshot() {
        return !this.onGround && this.onGroundLastTick;
    }

    public FluidState getInFluid() {
        return this.inFluid;
    }

    public FluidState getInFluidLastTick() {
        return this.inFluidLastTick;
    }

    public int getAge() {
        return this.age;
    }

    public float getAgePercent() {
        return (float) this.age / this.lifetime;
    }

    protected void forEventStacks(Consumer<EventStack> eventStackConsumer) {
        eventStackConsumer.accept(this.lifetimeEventStack);
        eventStackConsumer.accept(this.appearanceEventStack);
    }


    public interface BillboardMode {
        BillboardMode FIXED = (quaternion, camera, partialTicks) -> quaternion.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
        BillboardMode XYZ = (quaternion, camera, partialTicks) -> quaternion.set(camera.rotation());

        void rotate(Quaternionf quaternion, Camera camera, float partialTicks);
    }

    public static class Provider implements ParticleBehaviourProvider {
        @Override
        public @Nullable Particle createParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double z, double y, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleInteractionsParticle(components, appearance, context, ParticleConfig.DEFAULT, x, y, z, ySpeed, zSpeed, xSpeed);
        }
    }
}
