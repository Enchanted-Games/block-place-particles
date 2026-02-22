package games.enchanted.eg_particle_interactions.common.particle.types;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.debug.ParticleDebugShapes;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.ParticleAccessor;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.render.ModParticleRenderTypes;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.StateQuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.CustomParticleGeometryRenderState;
import games.enchanted.eg_particle_interactions.common.rendering.ModRenderPipelines;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public abstract class ParticleInteractionsParticle extends Particle {
    private float scale;
    private float prevScale;
    protected float roll;
    protected float prevRoll;
    protected float billboardYOffset = 0.0F;
    protected float billboardXOffset = 0.0F;
    protected final int minLightEmission;

    protected ParticleContext context;
    protected ParticleAppearance appearance;

    protected boolean shouldUpdateSpriteBasedOnAge = true;
    protected TextureAtlasSprite currentSprite;

    private float rCol = 1.0F;
    private float prevRCol = 1.0F;
    private float gCol = 1.0F;
    private float prevGCol = 1.0F;
    private float bCol = 1.0F;
    private float prevBCol = 1.0F;
    private float alpha = 1.0F;
    private float prevAlpha = 1.0F;

    protected ParticleInteractionsParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z) {
        this(context, appearance, x, y, z, 0, 0, 0);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    protected ParticleInteractionsParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context.level(), x, y, z, xSpeed, ySpeed, zSpeed);
        this.context = context;
        this.appearance = appearance;

        this.currentSprite = TextureHelpers.missingParticleSprite();
        this.pickSpriteForAppearance();

        this.scale = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;

        int[] colour = appearance.colourSource().getARGB(context);
        this.setRGBA(
            (float) colour[1] / 255f,
            (float) colour[2] / 255f,
            (float) colour[3] / 255f,
            (float) colour[0] / 255f
        );

        this.minLightEmission = appearance.lightEmission();
    }

    public static final SingleQuadParticle.Layer BACKFACE_TERRAIN_LAYER = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_BLOCKS, ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE);

    protected SingleQuadParticle.Layer getLayer() {
        ParticleLayer layer = this.getParticleLayer();
        return layer.layer;
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

        StateQuadConsumer consumer = new StateQuadConsumer(state, this.getLayer());
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
                ((ParticleAccessor) this).block_place_particle$getStoppedByCollision() ? ParticleDebugShapes.PARTICLE_BOUNDING_BOX_STOPPED : ParticleDebugShapes.PARTICLE_BOUNDING_BOX
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


    /**
     * Pick sprite based on particle appearance. If no texture config is present, the block particle texture is used.
     * If no block context is present, the item particle texture is used. If no item context is present, a missing
     * texture is used.
     */
    public void pickSpriteForAppearance() {
        if (this.appearance.textureConfig() == null) {
            this.setSpriteForContext();
            return;
        }

        this.setSpriteForTextureConfig();
    }

    private void setSpriteForTextureConfig() {
        if (this.removed) return;
        if (!this.shouldUpdateSpriteBasedOnAge) return;

        ParticleAppearance appearance = this.appearance;
        if (appearance.textureConfig() == null) return;

        ParticleAppearance.TextureConfig textureConfig = appearance.textureConfig();

        if (textureConfig.chooseRandomSprite()) {
            this.shouldUpdateSpriteBasedOnAge = false;
            this.setCurrentSprite(textureConfig.getRandom(this.random));
            return;
        }

        this.setCurrentSprite(textureConfig.getAt(this.getAgeForSprite(), this.lifetime));
    }

    protected int getAgeForSprite() {
        return this.age;
    }

    private void setSpriteForContext() {
        ParticleContext context = this.context;

        if (context.blockContext() != null) {
            BlockState state = context.blockContext().state();
            this.setCurrentSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleMaterial(state).sprite());
        } else if (context.stack() != null) {
            this.setCurrentSprite(TextureHelpers.getItemParticleSprite(ItemStackTemplate.fromNonEmptyStack(context.stack()), this.level, this.random));
        } else {
            this.setCurrentSprite(TextureHelpers.missingParticleSprite());
        }
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
        return this.getAlpha() < 0.99 ? ParticleLayer.TRANSLUCENT : ParticleLayer.CUTOUT;
    }

    public record ParticleLayer(SingleQuadParticle.Layer layer) {
        public static final ParticleLayer TRANSLUCENT = new ParticleLayer(
            SingleQuadParticle.Layer.TRANSLUCENT
        );
        public static final ParticleLayer CUTOUT = new ParticleLayer(
            SingleQuadParticle.Layer.OPAQUE
        );
        public static final ParticleLayer TERRAIN = new ParticleLayer(
            //? if minecraft: < 26.1 {
            /*SingleQuadParticle.Layer.TERRAIN
             *///? } else {
            SingleQuadParticle.Layer.TRANSLUCENT_TERRAIN
            //? }
        );
        public static final ParticleLayer BACKFACE_TERRAIN = new ParticleLayer(
            BACKFACE_TERRAIN_LAYER
        );
    }

    public interface BillboardMode {
        BillboardMode FIXED = (quaternion, camera, partialTicks) -> quaternion.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
        BillboardMode XYZ = (quaternion, camera, partialTicks) -> quaternion.set(camera.rotation());

        void rotate(Quaternionf quaternion, Camera camera, float partialTicks);
    }
}
