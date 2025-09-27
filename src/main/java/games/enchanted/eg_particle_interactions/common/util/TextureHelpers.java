package games.enchanted.eg_particle_interactions.common.util;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TextureHelpers {
    public static @NotNull TextureAtlas getTextureAtlas(ResourceLocation atlasLocation) {
        return Minecraft.getInstance()
            //? if minecraft: <= 1.21.8 {
            /*.getModelManager().getAtlas(atlasLocation);
            *///?} else {
            .getAtlasManager().getAtlasOrThrow(atlasLocation);
            //?}
    }

    public static TextureAtlasSprite getSpriteFromBlockAtlas(ResourceLocation location) {
        return getTextureAtlas(getBlocksAtlasID()).getSprite(location);
    }

    public static TextureAtlasSprite getDebugSprite() {
        return getSpriteFromBlockAtlas(ResourceLocation.withDefaultNamespace("block/debug"));
    }

    public static TextureAtlasSprite getParticlePaletteSprite(ResourceLocation location) {
        //? if minecraft: <= 1.21.8 {
        /*return ParticleInteractionsMod.particlePaletteAtlas.get(location);
        *///?} else {
        return getTextureAtlas(ParticlePaletteAtlasManager.ATLAS_ID).getSprite(location);
        //?}
    }

    public static TextureAtlasSprite getParticlePaletteOrBlockSprite(ResourceLocation blockLocation, ResourceLocation fallbackSpriteLocation) {
        TextureAtlasSprite particlePaletteSprite = getParticlePaletteSprite(blockLocation);
        if(particlePaletteSprite.contents().name() == MissingTextureAtlasSprite.getLocation()) {
            return getTextureAtlas(getBlocksAtlasID()).getSprite(fallbackSpriteLocation);
        }
        return particlePaletteSprite;
    }

    public static ResourceLocation getBlocksAtlasID() {
        //? if minecraft: <= 1.21.8 {
        /*return TextureAtlas.LOCATION_BLOCKS;
         *///?} else {
        return AtlasIds.BLOCKS;
        //?}
    }
}
