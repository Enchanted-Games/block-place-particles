package games.enchanted.eg_particle_interactions.common.particle.compat;

import games.enchanted.eg_particle_interactions.common.particle.ModParticleRenderTypes;
import games.enchanted.eg_particle_interactions.common.util.render.RenderingUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import net.minecraft.client.Camera;

//? if minecraft: <= 1.21.8 {
/*import com.mojang.blaze3d.vertex.VertexConsumer;
*///?} else {
import games.enchanted.eg_particle_interactions.common.rendering.state.CustomParticleGeometryRenderState;
import games.enchanted.eg_particle_interactions.common.util.render.StateAndLayer;
import net.minecraft.client.particle.SingleQuadParticle;
import games.enchanted.eg_particle_interactions.common.rendering.ModRenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
//?}

public abstract class CustomGeometryParticle extends Particle {
    protected float scale;
    protected float roll;
    protected float oRoll;
    protected TextureAtlasSprite sprite;
    protected float rCol = 1.0F;
    protected float gCol = 1.0F;
    protected float bCol = 1.0F;
    protected float alpha = 1.0F;

    protected CustomGeometryParticle(ClientLevel clientLevel, double x, double y, double z, TextureAtlasSprite textureAtlasSprite) {
        this(clientLevel, x, y, z, 0, 0, 0, textureAtlasSprite);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    protected CustomGeometryParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite textureAtlasSprite) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprite = textureAtlasSprite;
        this.scale = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
    }

    //? if minecraft: <= 1.21.8 {
    /*@Override
    public @NotNull ParticleRenderType getRenderType() {
        ParticleLayer layer = getParticleLayer();
        if(layer == null) return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
        return layer.layer;
    }

    *///?} else {
    public static final SingleQuadParticle.Layer BACKFACE_TERRAIN_LAYER = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_BLOCKS, ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE);

    protected @NotNull SingleQuadParticle.Layer getLayer() {
        ParticleLayer layer = getParticleLayer();
        if(layer == null) return SingleQuadParticle.Layer.OPAQUE;
        return layer.layer;
    }

    @Override
    public @NotNull ParticleRenderType getGroup() {
        return ModParticleRenderTypes.CUSTOM_GEOMETRY;
    }
    //?}

    protected void renderTick(float partialTicks) {
    }

    //? if minecraft: <= 1.21.8 {
    /*@Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
    *///?} else {
    public void extract(CustomParticleGeometryRenderState consumer, Camera camera, float partialTicks) {
    //?}
        this.renderTick(partialTicks);
        Quaternionf quaternionf = new Quaternionf();
        this.getBillboardMode().rotate(quaternionf, camera, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }

        this.adjustPositionBeforeExtraction(consumer, camera, quaternionf, partialTicks);
    }

    protected void adjustPositionBeforeExtraction(
        //? if minecraft: <= 1.21.8 {
        /*VertexConsumer consumer,
        *///?} else {
        CustomParticleGeometryRenderState consumer,
        //?}
        Camera camera, Quaternionf quaternionf, float partialTicks
    ) {
        Vec3 cameraPosition = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z());
        this.extractGeometry(consumer, quaternionf, x, y, z, partialTicks);
    }

    protected void extractGeometry(
        //? if minecraft: <= 1.21.8 {
        /*VertexConsumer consumer,
        *///?} else {
        CustomParticleGeometryRenderState state,
         //?}
        Quaternionf quaternion, float x, float y, float z, float partialTicks
    ) {
        int light = getLightColor(partialTicks);
        //? if minecraft: > 1.21.8 {
        SingleQuadParticle.Layer layer = getLayer();
        StateAndLayer consumer = new StateAndLayer(state, layer);
        state.startQuad(layer);
        //?}
        RenderingUtil.addVertex(consumer, quaternion, x, y, z,  1.0F, -1.0F, this.getScale(), getU1(), getV1(), light, this.rCol, this.gCol, this.bCol, this.alpha);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z,  1.0F,  1.0F, this.getScale(), getU1(), getV0(), light, this.rCol, this.gCol, this.bCol, this.alpha);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, -1.0F,  1.0F, this.getScale(), getU0(), getV0(), light, this.rCol, this.gCol, this.bCol, this.alpha);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, -1.0F, -1.0F, this.getScale(), getU0(), getV1(), light, this.rCol, this.gCol, this.bCol, this.alpha);
        //? if minecraft: > 1.21.8 {
        state.finishQuad(layer);
        //?}
    }

    public BillboardMode getBillboardMode() {
        return BillboardMode.XYZ;
    }

    public void setSpriteFromAge(SpriteSet sprites) {
        if (this.removed) return;
        this.setSprite(sprites.get(this.age, this.lifetime));
    }

    public void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    public AABB getCullingBox(float partialTicks) {
        return this.getBoundingBox();
    }

    protected float getU0() {
        return this.sprite.getU0();
    }
    protected float getU1() {
        return this.sprite.getU1();
    }
    protected float getV0() {
        return this.sprite.getV0();
    }
    protected float getV1() {
        return this.sprite.getV1();
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    protected abstract ParticleLayer getParticleLayer();

    public enum ParticleLayer {
        OPAQUE(
            //? if minecraft: <= 1.21.8 {
            /*ParticleRenderType.PARTICLE_SHEET_OPAQUE
             *///?} else {
            SingleQuadParticle.Layer.OPAQUE
            //?}
        ),
        TERRAIN(
            //? if minecraft: <= 1.21.8 {
            /*ParticleRenderType.TERRAIN_SHEET
             *///?} else {
            SingleQuadParticle.Layer.TERRAIN
            //?}
        ),
        TRANSLUCENT(
            //? if minecraft: <= 1.21.8 {
            /*ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
             *///?} else {
            SingleQuadParticle.Layer.TRANSLUCENT
            //?}
        ),
        BACKFACE_TERRAIN(
            //? if minecraft: <= 1.21.8 {
            /*ModParticleRenderTypes.BACKFACE_TERRAIN_PARTICLE
             *///?} else {
            BACKFACE_TERRAIN_LAYER
            //?}
        );

        //? if minecraft: <= 1.21.8 {
        /*public final ParticleRenderType layer;
         *///?} else {
        public final SingleQuadParticle.Layer layer;
        //?}

        ParticleLayer(
            //? if minecraft: <= 1.21.8 {
            /*ParticleRenderType layer
            *///?} else {
            SingleQuadParticle.Layer layer
            //?}
        ) {
            this.layer = layer;
        }
    }

    public interface BillboardMode {
        BillboardMode FIXED = (quaternion, camera, partialTicks) -> quaternion.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
        BillboardMode XYZ = (quaternion, camera, partialTicks) -> quaternion.set(camera.rotation());

        void rotate(Quaternionf quaternion, Camera camera, float partialTicks);
    }
}
