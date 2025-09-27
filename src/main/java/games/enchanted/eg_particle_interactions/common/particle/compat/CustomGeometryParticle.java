package games.enchanted.eg_particle_interactions.common.particle.compat;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;

import org.jetbrains.annotations.NotNull;

//? if minecraft: <= 1.21.8 {
/*import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
*///?} else {
import games.enchanted.eg_particle_interactions.common.rendering.state.CustomParticleGeometryRenderState;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.SingleQuadParticle;
//?}

public abstract class CustomGeometryParticle
    //? if minecraft: <= 1.21.8 {
    /*extends TextureSheetParticle
     *///?} else {
    extends SingleQuadParticle
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
    protected CustomGeometryParticle(ClientLevel clientLevel, double x, double y, double z, TextureAtlasSprite textureAtlasSprite) {
        this(clientLevel, x, y, z, 0, 0, 0, textureAtlasSprite);
    }

    protected CustomGeometryParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite textureAtlasSprite) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, textureAtlasSprite);
    }

    @Override
    protected @NotNull Layer getLayer() {
        switch (getParticleLayer()) {
            case OPAQUE -> {
                return Layer.OPAQUE;
            }
            case TERRAIN -> {
                return Layer.TERRAIN;
            }
            case TRANSLUCENT -> {
                return Layer.TRANSLUCENT;
            }
        }
        return Layer.OPAQUE;
    }

    public void extract(CustomParticleGeometryRenderState state, Camera camera, float partialTicks) {
    }
    //?}

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getZ() {
        return this.z;
    }

    protected abstract ParticleLayer getParticleLayer();

    public enum ParticleLayer {
        OPAQUE,
        TERRAIN,
        TRANSLUCENT,
        BACKFACE_TERRAIN
    }
}
