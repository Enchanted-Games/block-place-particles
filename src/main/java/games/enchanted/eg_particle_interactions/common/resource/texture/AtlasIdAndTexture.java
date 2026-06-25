package games.enchanted.eg_particle_interactions.common.resource.texture;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;

public record AtlasIdAndTexture(Identifier id, Identifier texturePath) {
    public static final AtlasIdAndTexture BLOCKS = new AtlasIdAndTexture(AtlasIds.BLOCKS, TextureAtlas.LOCATION_BLOCKS);
}
