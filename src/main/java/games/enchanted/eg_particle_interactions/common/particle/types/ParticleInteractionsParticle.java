package games.enchanted.eg_particle_interactions.common.particle.types;

import games.enchanted.eg_particle_interactions.common.particle.render.ModParticleRenderTypes;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
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
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.VertexQuadConsumer;
*///?} else {
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.StateQuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.CustomParticleGeometryRenderState;
import net.minecraft.client.particle.SingleQuadParticle;
import games.enchanted.eg_particle_interactions.common.rendering.ModRenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
//?}

public abstract class ParticleInteractionsParticle extends Particle {
    private float scale;
    private float prevScale;
    protected float roll;
    protected float prevRoll;
    protected float billboardYOffset = 0.0F;
    protected float billboardXOffset = 0.0F;

    protected TextureAtlasSprite sprite;

    private float rCol = 1.0F;
    private float prevRCol = 1.0F;
    private float gCol = 1.0F;
    private float prevGCol = 1.0F;
    private float bCol = 1.0F;
    private float prevBCol = 1.0F;
    private float alpha = 1.0F;
    private float prevAlpha = 1.0F;

    protected ParticleInteractionsParticle(ClientLevel clientLevel, double x, double y, double z, TextureAtlasSprite textureAtlasSprite) {
        this(clientLevel, x, y, z, 0, 0, 0, textureAtlasSprite);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    protected ParticleInteractionsParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite textureAtlasSprite) {
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

    //? if minecraft: <= 1.21.8 {
    /*@Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
    *///?} else {
    public void extract(CustomParticleGeometryRenderState state, Camera camera, float partialTicks) {
    //?}
        Quaternionf quaternionf = new Quaternionf();
        this.getBillboardMode().rotate(quaternionf, camera, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.prevRoll, this.roll));
        }

        //? if minecraft: <= 1.21.8 {
        /*VertexQuadConsumer consumer = new VertexQuadConsumer(vertexConsumer);
        *///?} else {
        StateQuadConsumer consumer = new StateQuadConsumer(state, this.getLayer());
        //?}
        this.adjustPositionBeforeExtraction(consumer, camera, quaternionf, partialTicks);
    }

    protected void adjustPositionBeforeExtraction(QuadConsumer consumer, Camera camera, Quaternionf quaternionf, float partialTicks) {
        Vec3 cameraPosition = camera.position();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z());
        this.extractGeometry(consumer, quaternionf, x, y, z, partialTicks);
    }

    protected void extractGeometry(QuadConsumer consumer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        int light = this.getLightmapCoords(partialTicks);
        float scale = this.getLerpedScale(partialTicks);
        float r = this.getLerpedRed(partialTicks);
        float g = this.getLerpedGreen(partialTicks);
        float b = this.getLerpedBlue(partialTicks);
        float a = this.getLerpedAlpha(partialTicks);

        consumer.startQuad();
        consumer.addVertex(quaternion, x, y, z,  1.0F + this.billboardXOffset, -1.0F + this.billboardYOffset, scale, getU1(), getV1(), light, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z,  1.0F + this.billboardXOffset,  1.0F + this.billboardYOffset, scale, getU1(), getV0(), light, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, -1.0F + this.billboardXOffset,  1.0F + this.billboardYOffset, scale, getU0(), getV0(), light, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, -1.0F + this.billboardXOffset, -1.0F + this.billboardYOffset, scale, getU0(), getV1(), light, r, g, b, a);
        consumer.finishQuad();
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


    protected int getLightmapCoords(float partialTick) {
        //? if minecraft: < 26.1 {
        return this.getLightColor(partialTick);
        //? } else {
        /*return this.getLightmapCoords(partialTick);
        *///? }
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
        if(lerp) {
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
        if(lerp) {
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
        if(lerp) {
            this.prevScale = this.scale;
        } else {
            this.prevScale = scale;
        }
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
