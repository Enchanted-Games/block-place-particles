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
     * @return the average colour in an array of a, r, g, b
     */
    public static int[] getAverageBlockColour(BlockState blockState) {
        TextureAtlasSprite particleSprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).getParticleIcon();
        ResourceLocation particleSpriteLocation = particleSprite.contents().name();
        if(colourCache.containsKey(particleSpriteLocation)) {
            return ARGBint_to_ARGB(colourCache.get(particleSpriteLocation));
        }
        int average = calculateAverageSpriteColour(particleSprite);
        colourCache.put(particleSpriteLocation, average);
        return ARGBint_to_ARGB(average);
    }

    private static int calculateAverageSpriteColour(TextureAtlasSprite sprite) {
        if (sprite == null) return -1;
        SpriteContents spriteContents = sprite.contents();
        if (spriteContents.getUniqueFrames().count() == 0) return -1;
        float total = 0, red = 0, blue = 0, green = 0;
        for (int x = 0; x < spriteContents.width(); x++) {
            for (int y = 0; y < spriteContents.height(); y++) {
                int color = ((SpriteContentsAccessor) spriteContents).getOriginalImage().getPixelRGBA(x, y);
                int[] argb = ABGRint_to_RGBA(color);
                int alpha = argb[0];
                if (alpha <= 10) continue;
                total += alpha;
                red += argb[1];
                green += argb[2];
                blue += argb[3];
            }
        }
        total /= 255;
        return ARGB_to_ARGBint(255, (int) (red / total), (int) (green / total), (int) (blue / total));
    }

    /**
     * Converts argb to an int in argb decimal format
     */
    public static int ARGB_to_ARGBint(int a, int r, int g, int b) {
        int alpha = MathHelpers.clampInt(a, 0, 255);
        int red = MathHelpers.clampInt(r, 0, 255);
        int green = MathHelpers.clampInt(g, 0, 255);
        int blue = MathHelpers.clampInt(b, 0, 255);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Converts an int in argb decimal format to an array of a, r, g, b
     */
    public static int[] ARGBint_to_ARGB(int argb) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return new int[]{a, r, g, b};
    }

    /**
     * Converts an int in rgba decimal format to an array of a, r, g, b
     */
    public static int[] RGBAint_to_ARGB(int rgba) {
        int r = (rgba >> 24) & 0xFF;
        int g = (rgba >> 16) & 0xFF;
        int b = (rgba >> 8) & 0xFF;
        int a = rgba & 0xFF;
        return new int[]{a, r, g, b};
    }

    /**
     * Converts an int in abgr decimal format to an array of a, r, g, b
     */
    public static int[] ABGRint_to_RGBA(int abgr) {
        int a = (abgr >> 24) & 0xFF;
        int b = (abgr >> 16) & 0xFF;
        int g = (abgr >> 8) & 0xFF;
        int r = abgr & 0xFF;
        return new int[]{a, r, g, b};
    }
}
