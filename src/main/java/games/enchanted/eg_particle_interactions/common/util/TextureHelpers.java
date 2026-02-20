package games.enchanted.eg_particle_interactions.common.util;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
//? if minecraft: > 1.21.8 {
import net.minecraft.data.AtlasIds;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
//?}

public class TextureHelpers {
    public static @NotNull TextureAtlas getTextureAtlas(Identifier atlasLocation) {
        return Minecraft.getInstance()
            //? if minecraft: <= 1.21.8 {
            /*.getModelManager().getAtlas(atlasLocation);
            *///?} else {
            .getAtlasManager().getAtlasOrThrow(atlasLocation);
            //?}
    }

    public static TextureAtlasSprite getSpriteFromBlockAtlas(Identifier location) {
        return getTextureAtlas(getBlocksAtlasID()).getSprite(location);
    }

    public static TextureAtlasSprite getSpriteFromAtlas(Identifier spriteId, Identifier atlasId) {
        return getTextureAtlas(atlasId).getSprite(spriteId);
    }

    public static TextureAtlasSprite getDebugSprite() {
        return getSpriteFromBlockAtlas(Identifier.withDefaultNamespace("block/debug"));
    }

    public static TextureAtlasSprite getParticlePaletteSprite(Identifier location) {
        //? if minecraft: <= 1.21.8 {
        /*return ParticleInteractionsMod.particlePaletteAtlas.get(location);
        *///?} else {
        return getTextureAtlas(ParticlePaletteAtlasManager.ATLAS_ID).getSprite(location);
        //?}
    }

    public static TextureAtlasSprite getParticlePaletteOrBlockSprite(Identifier blockLocation, Identifier fallbackSpriteLocation) {
        TextureAtlasSprite particlePaletteSprite = getParticlePaletteSprite(blockLocation);
        if(particlePaletteSprite.contents().name() == MissingTextureAtlasSprite.getLocation()) {
            return getTextureAtlas(getBlocksAtlasID()).getSprite(fallbackSpriteLocation);
        }
        return particlePaletteSprite;
    }

    public static Identifier getBlocksAtlasID() {
        //? if minecraft: <= 1.21.8 {
        /*return TextureAtlas.LOCATION_BLOCKS;
         *///?} else {
        return AtlasIds.BLOCKS;
        //?}
    }
}
