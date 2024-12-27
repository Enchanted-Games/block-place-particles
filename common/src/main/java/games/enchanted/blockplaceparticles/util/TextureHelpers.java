package games.enchanted.blockplaceparticles.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TextureHelpers {
    public static @Nullable TextureAtlas getTextureAtlas(ResourceLocation atlasLocation) {
        AbstractTexture abstractTexture = Minecraft.getInstance().getTextureManager().getTexture(atlasLocation);
        if(!(abstractTexture instanceof TextureAtlas)) {
            return null;
        }
        return (TextureAtlas) abstractTexture;
    }

    public static TextureAtlasSprite getDebugSprite() {
        return getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(ResourceLocation.withDefaultNamespace("block/debug"));
    }
}
