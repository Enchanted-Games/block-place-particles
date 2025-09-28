package games.enchanted.eg_particle_interactions.common.particle.compat;

import games.enchanted.eg_particle_interactions.common.particle.ModParticleRenderTypes;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;

import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

//? if minecraft: <= 1.21.8 {
/*import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
*///?} else {
import games.enchanted.eg_particle_interactions.common.rendering.state.CustomParticleGeometryRenderState;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.SingleQuadParticle;
import org.joml.Quaternionf;
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
        }
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    *///?} else {

    protected float scale;
    protected float roll;
    protected float prevRoll;
    protected TextureAtlasSprite sprite;
    protected float rCol = 1.0F;
    protected float gCol = 1.0F;
    protected float bCol = 1.0F;
    protected float alpha = 1.0F;

    protected CustomGeometryParticle(ClientLevel clientLevel, double x, double y, double z, TextureAtlasSprite textureAtlasSprite) {
        this(clientLevel, x, y, z, 0, 0, 0, textureAtlasSprite);
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
        }
        return SingleQuadParticle.Layer.OPAQUE;
    }

    public void extract(CustomParticleGeometryRenderState state, Camera camera, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        this.getBillboardMode().rotate(quaternionf, camera, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.prevRoll, this.roll));
        }

        this.extractRotatedQuad(state, camera, quaternionf, partialTicks);
    }

    protected void extractRotatedQuad(CustomParticleGeometryRenderState state, Camera camera, Quaternionf quaternionf, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z());
        this.extractRotatedQuad(state, quaternionf, x, y, z, partialTicks);
    }

    protected void extractRotatedQuad(CustomParticleGeometryRenderState state, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        SingleQuadParticle.Layer layer = getLayer();
        state.startQuad(layer);
        int light = getLightColor(partialTicks);
        state.addVertex(layer, quaternion, x, y, z,  1.0F, -1.0F, 1, getU1(), getV1(), light, this.rCol, this.gCol, this.bCol, this.alpha);
        state.addVertex(layer, quaternion, x, y, z,  1.0F,  1.0F, 1, getU1(), getV0(), light, this.rCol, this.gCol, this.bCol, this.alpha);
        state.addVertex(layer, quaternion, x, y, z, -1.0F,  1.0F, 1, getU0(), getV0(), light, this.rCol, this.gCol, this.bCol, this.alpha);
        state.addVertex(layer, quaternion, x, y, z, -1.0F, -1.0F, 1, getU0(), getV1(), light, this.rCol, this.gCol, this.bCol, this.alpha);
        state.finishQuad(layer);
//        state.add(this.getLayer(), f, g, h, quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w, this.getQuadSize(i), this.getU0(), this.getU1(), this.getV0(), this.getV1(), ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol), this.getLightColor(i));
    }

    @Override
    public @NotNull ParticleRenderType getGroup() {
        return ModParticleRenderTypes.CUSTOM_GEOMETRY;
    }
    //?}

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
        return this.scale;
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
