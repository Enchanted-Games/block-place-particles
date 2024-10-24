package games.enchanted.blockplaceparticles.util;

import games.enchanted.blockplaceparticles.mixin.accessor.SpriteContentsAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class ColourUtil {
    private static final HashMap<ResourceLocation, Integer> colourCache = new HashMap<>();

    /**
     * Calculates the average colour of the passed blockstate's particle texture.
     *
     * @param blockState the block state
     * @return the average colour
     */
    public int getAverageBlockColour(BlockState blockState) {
        TextureAtlasSprite particleSprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).getParticleIcon();
        ResourceLocation particleSpriteLocation = particleSprite.atlasLocation();
        if(colourCache.containsKey(particleSpriteLocation)) {
            return colourCache.get(particleSpriteLocation);
        }
        int average = calculateAverageSpriteColour(particleSprite);
        colourCache.put(particleSpriteLocation, average);
        return average;
    }

    private int calculateAverageSpriteColour(TextureAtlasSprite sprite) {
        if (sprite == null) return -1;
        SpriteContents spriteContents = sprite.contents();
        if (spriteContents.getUniqueFrames().count() == 0) return -1;
        float total = 0, red = 0, blue = 0, green = 0;
        for (int x = 0; x < spriteContents.width(); x++) {
            for (int y = 0; y < spriteContents.height(); y++) {
                int color = ((SpriteContentsAccessor) spriteContents).getOriginalImage().getPixelRGBA(x, y);
                int alpha = color >> 24 & 0xFF;
                if (alpha <= 10) continue;
                total += alpha;
                red += (color & 0xFF);
                green += (color >> 8 & 0xFF);
                blue += (color >> 16 & 0xFF);
            }
        }
        return argbToInt(255, (int) (red / total), (int) (green / total), (int) (blue / total));
    }

    /**
     * Converts argb to an int in argb decimal format
     */
    public int argbToInt(int a, int r, int g, int b) {
        int alpha = MathHelpers.clampInt(a, 0, 255);
        int red = MathHelpers.clampInt(r, 0, 255);
        int green = MathHelpers.clampInt(g, 0, 255);
        int blue = MathHelpers.clampInt(b, 0, 255);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Converts an int in argb decimal format to an array of a, r, g, b
     */
    public int[] intToArgb(int argb) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return new int[]{a, r, g, b};
    }
}
