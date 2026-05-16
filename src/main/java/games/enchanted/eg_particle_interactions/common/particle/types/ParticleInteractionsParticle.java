package games.enchanted.eg_particle_interactions.common.particle.types;

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
import games.enchanted.eg_particle_interactions.common.particle.render.ModParticleRenderTypes;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.StateQuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.layer.ParticleLayer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.CustomParticleGeometryRenderState;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public abstract class ParticleInteractionsParticle extends Particle {
    private float scale;
    private float prevScale;
    protected float roll;
    protected float prevRoll;
    protected float billboardYOffset = 0.0f;
    protected float billboardXOffset = 0.0f;
    protected final int minLightEmission;
    protected final float gravityDecay;
    protected final float velocityDecay;

    protected ParticleContext context;
    protected ParticleAppearance appearance;
    protected ParticleLayer layer;

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
        this(components, appearance, context, config, y, z, 0, 0, 0, x);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    protected ParticleInteractionsParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, ParticleConfig config, double y, double z, double xSpeed, double ySpeed, double zSpeed, double x) {
        super(context.level(), x, y, z, xSpeed, ySpeed, zSpeed);
        ClientLevel level = context.level();

        this.xd = xSpeed + ((level.getRandom().nextFloat() - 0.5f) * 2f * config.getInitialVelocityRandomness());
        this.yd = ySpeed + ((level.getRandom().nextFloat() - 0.5f) * 2f * config.getInitialVelocityRandomness());
        this.zd = zSpeed + ((level.getRandom().nextFloat() - 0.5f) * 2f * config.getInitialVelocityRandomness());

        this.context = context;
        this.appearance = appearance;

        this.currentSprite = TextureHelpers.missingParticleSprite();
        this.pickSpriteForAppearance();

        this.gravity = config.getGravityProvider().getValue(context);
        var gravityComponent = components.get(ParticleComponents.GRAVITY);
        if(gravityComponent != null) {
            this.gravity = gravityComponent.initialGravity().getValue(context);
        }

        this.lifetime = config.getLifetimeProvider().getValue(context);
        float collisionSize = config.getCollisionSizeProvider().getValue(context);
        this.setSize(collisionSize, collisionSize);

        // 0.1 - 0.2
        this.scale = 0.1f * (this.random.nextFloat() * 0.5f + 0.5f) * 2.0f;

        int[] colour = appearance.colourSource().getARGB(context);
        this.setRGBA(
            (float) colour[1] / 255f,
            (float) colour[2] / 255f,
            (float) colour[3] / 255f,
            (float) colour[0] / 255f
        );

        this.minLightEmission = appearance.lightEmission();

        this.layer = ParticleLayer.fromAppearance(context, appearance);

        this.gravityDecay = (1 - config.getGravityDecayProvider().getValue(context));
        this.velocityDecay = (1 - config.getVelocityDecayProvider().getValue(context));
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
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z());
        this.extractGeometry(consumer, quaternionf, x, y, z, partialTicks);

        if (GeneralOptions.DEBUG_PARTICLE_TICK_BOUNDING_BOXES.getValue()) {
            ParticleDebugShapes.particlePosition(this.x, this.y, this.z, ParticleDebugShapes.PARTICLE_TICK_POSITION);

            ParticleDebugShapes.box(
                this.getBoundingBox(),
                ((ParticleAccessor) this).eg_particle_interactions$getStoppedByCollision() ? ParticleDebugShapes.PARTICLE_BOUNDING_BOX_STOPPED : ParticleDebugShapes.PARTICLE_BOUNDING_BOX
            );
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
        this.pickSpriteForAppearance();
        super.tick();
        this.applyGravityAndVelocityDecays();
    }

    protected void applyGravityAndVelocityDecays() {
        this.xd *= this.velocityDecay;
        this.yd *= this.velocityDecay;
        this.zd *= this.velocityDecay;

        this.gravity *= this.gravityDecay;
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

    public void setCurrentSprite(TextureAtlasSprite sprite) {
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

    public interface BillboardMode {
        BillboardMode FIXED = (quaternion, camera, partialTicks) -> quaternion.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
        BillboardMode XYZ = (quaternion, camera, partialTicks) -> quaternion.set(camera.rotation());

        void rotate(Quaternionf quaternion, Camera camera, float partialTicks);
    }
}
