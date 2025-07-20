package games.enchanted.eg_particle_interactions.common.resource;

import games.enchanted.eg_particle_interactions.common.Constants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

public class ParticlePaletteAtlasManager extends TextureAtlasHolder {
    private static final String ATLAS_ID = "particle_palettes";

    public ParticlePaletteAtlasManager(TextureManager textureManager) {
        super(textureManager, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/atlas/" + ATLAS_ID), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, ATLAS_ID));
    }

    public TextureAtlasSprite get(ResourceLocation location) {
        return this.getSprite(location);
    }
}
