package games.enchanted.blockplaceparticles.util;

import com.mojang.blaze3d.platform.NativeImage;
import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.mixin.accessor.client.SpriteContentsAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ColourUtil {
    /**
     * Stores the average colour for a sprite resource location
     */
    private static final HashMap<ResourceLocation, Integer> averageSpriteColourCache = new HashMap<>();

    private static final int OPAQUE_PIXELS_THRESHOLD = 20;
    /**
     * Stores a list of non-transparent pixel coordinates for a sprite resource location.
     * If the length of the list is 1 and at least 1 coordinate is negative, assume the sprite contains entirely non-transparent pixels
     */
    private static final HashMap<ResourceLocation, ImageCoordinate[]> spriteOpaquePixelsCache = new HashMap<>();

    /**
     * Calculates and caches the average colour from a {@link BlockState}'s pixel texture
     *
     * @param blockState the block state
     * @return the average colour in an array of a, r, g, b
     */
    public static int[] getAverageBlockColour(BlockState blockState) {
        TextureAtlasSprite particleSprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).particleIcon();
        ResourceLocation particleSpriteLocation = particleSprite.contents().name();
        if(averageSpriteColourCache.containsKey(particleSpriteLocation)) {
            return ARGBint_to_ARGB(averageSpriteColourCache.get(particleSpriteLocation));
        }
        int average = calculateAverageSpriteColour(particleSprite);
        averageSpriteColourCache.put(particleSpriteLocation, average);
        return ARGBint_to_ARGB(average);
    }

    /**
     * Calculates the average from a {@link TextureAtlasSprite}
     *
     * @param sprite a sprite to calculate the average colour of
     * @return the average colour as an argb int
     */
    public static int calculateAverageSpriteColour(TextureAtlasSprite sprite) {
        if (sprite == null) return -1;
        SpriteContents spriteContents = sprite.contents();
        if (spriteContents.getUniqueFrames().count() == 0) return -1;
        float total = 0, red = 0, blue = 0, green = 0, alpha = 0;
        for (int x = 0; x < spriteContents.width(); x++) {
            for (int y = 0; y < spriteContents.height(); y++) {
                int color = ((SpriteContentsAccessor) spriteContents).block_place_particle$getOriginalImage().getPixel(x, y);
                int[] argb = ARGBint_to_ARGB(color);
                int pixelAlpha = argb[0];
                if (pixelAlpha <= 10) continue;
                total++;
                alpha += pixelAlpha;
                red += argb[1];
                green += argb[2];
                blue += argb[3];
            }
        }

        float[] hsb = Color.RGBtoHSB((int) (red / total), (int) (green / total), (int) (blue / total), null);
        hsb[2] *= 1.05f;
        int[] rgb = RGBint_to_RGB(
            Color.HSBtoRGB(
                Math.clamp(hsb[0], 0, 1),
                Math.clamp(hsb[1], 0, 1),
                Math.clamp(hsb[2], 0, 1)
            )
        );

        return ARGB_to_ARGBint((int) (alpha / total), rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Gets a random pixel's colour from a {@link BlockState}'s particle texture
     *
     * @param blockState the block state to get a random colour from
     * @return the colour in an array of a, r, g, b
     */
    public static int[] getRandomBlockColour(BlockState blockState) {
        TextureAtlasSprite particleSprite = blockState.getBlock() == Blocks.GRASS_BLOCK ?
            TextureHelpers.getSpriteFromBlockAtlas(ResourceLocation.withDefaultNamespace("block/grass_block_top")) :
            Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).particleIcon();
        SpriteContents spriteContents = particleSprite.contents();

        ResourceLocation particleSpriteLocation = particleSprite.contents().name();

        ImageCoordinate[] pixelCoordinatesList;

        if(spriteOpaquePixelsCache.containsKey(particleSpriteLocation)) {
            pixelCoordinatesList = spriteOpaquePixelsCache.get(particleSpriteLocation);
        } else {
            // calculate valid coordinates
            pixelCoordinatesList = findOutWhereOpaquePixelCoordinatesAre(spriteContents);
            spriteOpaquePixelsCache.put(particleSpriteLocation, pixelCoordinatesList);
            ParticleInteractionsLogging.textureDebugInfo("Opaque pixels list for sprite: {} has been cached", spriteContents.name());
        }

        ImageCoordinate randomPixelCoordinate;

        if(pixelCoordinatesList.length == 0) {
            // no valid pixels, so just return transparent white
            return new int[]{0, 255, 255, 255};
        }
        else if(pixelCoordinatesList.length == 1 && (pixelCoordinatesList[0].x() < 0 || pixelCoordinatesList[0].y() < 0) ) {
            // assume all pixels in the sprite are opaque
            randomPixelCoordinate = new ImageCoordinate(
                MathHelpers.randomBetween(0, spriteContents.width() - 1),
                MathHelpers.randomBetween(0, spriteContents.height() - 1)
            );
        }
        else {
            // otherwise get a random coordinate from the list
            randomPixelCoordinate = pixelCoordinatesList[MathHelpers.randomBetween(0, pixelCoordinatesList.length - 1)];
        }

        NativeImage particleImage = ((SpriteContentsAccessor) spriteContents).block_place_particle$getOriginalImage();

        if(randomPixelCoordinate.x() > particleImage.getWidth() - 1 || randomPixelCoordinate.y() > particleImage.getHeight() - 1) {
            return new int[]{255, 255, 255, 255};
        }

        int sampledColour = particleImage.getPixel(randomPixelCoordinate.x(), randomPixelCoordinate.y());
        return ARGBint_to_ARGB(sampledColour);
    }

    private static ImageCoordinate[] findOutWhereOpaquePixelCoordinatesAre(SpriteContents spriteContents) {
        ArrayList<ImageCoordinate> coordinatesList = new ArrayList<>();
        int totalOpaquePixels = 0;

        for (int x = 0; x < spriteContents.width(); x++) {
            for (int y = 0; y < spriteContents.height(); y++) {
                int sampledColour = ((SpriteContentsAccessor) spriteContents).block_place_particle$getOriginalImage().getPixel(x, y);
                int[] argb = ARGBint_to_ARGB(sampledColour);

                if(argb[0] <= OPAQUE_PIXELS_THRESHOLD) continue;

                coordinatesList.add(new ImageCoordinate(x, y));
                totalOpaquePixels++;
            }
        }

        if(totalOpaquePixels >= (spriteContents.width() - 1) * (spriteContents.height() - 1)) {
            // image is entirely opaque pixels
            ParticleInteractionsLogging.textureDebugInfo("Sprite {} contains entirely opaque pixels", spriteContents.name());
            return new ImageCoordinate[]{new ImageCoordinate(-1, -1)};
        }

        ImageCoordinate[] coordinatesArray = new ImageCoordinate[coordinatesList.size()];
        coordinatesArray = coordinatesList.toArray(coordinatesArray);

        ParticleInteractionsLogging.textureDebugInfo("Sprite {} has {} opaque pixels", spriteContents.name(), totalOpaquePixels);

        return coordinatesArray;
    }

    private record ImageCoordinate(int x, int y) {}

    /**
     * Clears all calculated average colours
     */
    public static void invalidateCaches() {
        ColourUtil.averageSpriteColourCache.clear();
        ColourUtil.spriteOpaquePixelsCache.clear();
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
     * Converts rgb to an int in rgb decimal format
     */
    public static int RGB_to_RGBint(int r, int g, int b) {
        int red = MathHelpers.clampInt(r, 0, 255);
        int green = MathHelpers.clampInt(g, 0, 255);
        int blue = MathHelpers.clampInt(b, 0, 255);
        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Converts an int in rgb decimal format to an array of r, g, b
     */
    public static int[] RGBint_to_RGB(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new int[]{r, g, b};
    }

    /**
     * Converts an int in rgb decimal format to an array of a, r, g, b
     */
    public static int[] RGBint_to_ARGB(int rgb) {
        int[] rgbArray = RGBint_to_RGB(rgb);
        return new int[]{255, rgbArray[0], rgbArray[1], rgbArray[2]};
    }

    /**
     * Converts a r g b values in 0-1 range to an array of a, r, g, b
     */
    public static int[] ARGBfloats_to_ARGB(float a, float r, float g, float b) {
        return new int[]{(int) (a * 255), (int) (r * 255), (int) (g * 255), (int) (b * 255)};
    }

    /**
     * Randomises the value each channel seperately. The colour is randomly decreased
     *
     * @param colours the colour
     * @param amount amount to randomise by, 0 is no randomisation and 1 is full randomisation
     * @return the randomised colour
     */
    public static int[] randomiseNegative(int[] colours, float amount) {
        int[] randomised = new int[colours.length];
        for (int i = 0; i < colours.length; i++) {
            randomised[i] = variateColourComponent(colours[i], -MathHelpers.randomBetween(0, amount));
        }
        return randomised;
    }

    /**
     * Randomises the value all channels by the same amount, effectively changes the brightness of the colour. The colour is randomly decreased
     *
     * @param colours the colour
     * @param amount amount to randomise by, 0 is no randomisation and 1 is full randomisation
     * @return the randomised colour
     */
    public static int[] randomiseNegativeUniform(int[] colours, float amount) {
        float randomAmount = -MathHelpers.randomBetween(0, amount);
        int[] randomised = new int[colours.length];
        for (int i = 0; i < colours.length; i++) {
            randomised[i] = variateColourComponent(colours[i], randomAmount);
        }
        return randomised;
    }

    public static int variateColourComponent(int colour, float variation) {
        return MathHelpers.clampInt(colour + (int)(variation * 255), 0, 255);
    }

    /**
     * Multiply two colours together. The inputs can be either ARGB or RGB, but they must both be the same format
     *
     * @return the colour in ARGB or RGB
     */
    public static int[] multiplyColours(int[] colour1, int[] colour2) {
        if(colour1.length != colour2.length) {
            throw new IllegalArgumentException(ColourUtil.class.getName() + "#multiplyColours: colour1 and colour2 must both be either ARGB or RGB arrays. colour1: " + Arrays.toString(colour1) + ", colour2: " + Arrays.toString(colour2));
        }
        if(!(colour1.length == 4 || colour1.length == 3)) {
            throw new IllegalArgumentException(ColourUtil.class.getName() + "#multiplyColours: both colours must have 4 or 3 values. colour1: " + Arrays.toString(colour1) + ", colour2: " + Arrays.toString(colour2));
        }
        int[] multipliedColour = new int[colour1.length];
        for (int i = 0; i < colour1.length; i++) {
            multipliedColour[i] = (int) (Math.clamp((colour1[i] / 255f) * (colour2[i] / 255f), 0f, 255f) * 255);
        }
        return multipliedColour;
    }
}
