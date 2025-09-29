package games.enchanted.eg_particle_interactions.common.particle.compat;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleRenderTypes;
import games.enchanted.eg_particle_interactions.common.rendering.ModRenderPipelines;
import games.enchanted.eg_particle_interactions.common.util.render.RenderingUtil;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import net.minecraft.client.Camera;

//? if minecraft: <= 1.21.8 {
/*import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
*///?} else {
import games.enchanted.eg_particle_interactions.common.rendering.state.CustomParticleGeometryRenderState;
import games.enchanted.eg_particle_interactions.common.util.render.StateAndLayer;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.particle.Particle;
//?}

public abstract class CustomGeometryParticle
    //? if minecraft: <= 1.21.8 {
    /*extends TextureSheetParticle
     *///?} else {
    extends Particle
    //?}
{
    //? if minecraft: <= 1.21.8 {
    
    /*protected CustomGeometryParticle(ClientLevel clientLevel, double x, double y, double z, TextureAtlasSprite textureAtlasSprite) {
        this(clientLevel, x, y, z, 0, 0, 0, textureAtlasSprite);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    protected CustomGeometryParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite textureAtlasSprite) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setSprite(textureAtlasSprite);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        switch (getParticleLayer()) {
            case OPAQUE -> {
                return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
            }
            case TERRAIN -> {
                return ParticleRenderType.TERRAIN_SHEET;
            }
            case TRANSLUCENT -> {
                return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
            }
            case BACKFACE_TERRAIN -> {
                return ModParticleRenderTypes.BACKFACE_TERRAIN_PARTICLE;
            }
        }
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected final void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
    }

    @Override
    protected final void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
    }

    *///?} else {

    protected float scale;
    protected float roll;
    protected float oRoll;
    protected TextureAtlasSprite sprite;
    protected float rCol = 1.0F;
    protected float gCol = 1.0F;
    protected float bCol = 1.0F;
    protected float alpha = 1.0F;

    public static final SingleQuadParticle.Layer BACKFACE_TERRAIN_LAYER = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_BLOCKS, ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE);

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

    protected @NotNull SingleQuadParticle.Layer getLayer() {
        switch (getParticleLayer()) {
            case OPAQUE -> {
                return SingleQuadParticle.Layer.OPAQUE;
            }
            case TERRAIN -> {
                return SingleQuadParticle.Layer.TERRAIN;
            }
            case TRANSLUCENT -> {
                return SingleQuadParticle.Layer.TRANSLUCENT;
            }
            case BACKFACE_TERRAIN -> {
                return BACKFACE_TERRAIN_LAYER;
            }
        }
        return SingleQuadParticle.Layer.OPAQUE;
    }

    @Override
    public @NotNull ParticleRenderType getGroup() {
        return ModParticleRenderTypes.CUSTOM_GEOMETRY;
    }
    //?}

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

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getZ() {
        return this.z;
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
        //? if minecraft: > 1.21.8 {
        return this.scale;
        //?} else {
        /*return this.quadSize;
        *///?}
    }

    public void setScale(float scale) {
        //? if minecraft: > 1.21.8 {
        this.scale = scale;
         //?} else {
        /*this.quadSize = scale;
        *///?}
    }

    protected void renderTick(float partialTicks) {
    }

    protected abstract ParticleLayer getParticleLayer();

    public enum ParticleLayer {
        OPAQUE,
        TERRAIN,
        TRANSLUCENT,
        BACKFACE_TERRAIN
    }

    public interface BillboardMode {
        BillboardMode FIXED = (quaternion, camera, partialTicks) -> quaternion.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
        BillboardMode XYZ = (quaternion, camera, partialTicks) -> quaternion.set(camera.rotation());

        void rotate(Quaternionf quaternion, Camera camera, float partialTicks);
    }
}
