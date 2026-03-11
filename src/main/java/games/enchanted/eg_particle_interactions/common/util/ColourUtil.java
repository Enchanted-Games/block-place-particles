package games.enchanted.eg_particle_interactions.common.util;

import com.mojang.blaze3d.platform.NativeImage;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.NativeImageAccessor;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.SpriteContentsAccessor;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ColourUtil {
    private static final int OPAQUE_PIXELS_THRESHOLD = 20;
    private static final HashMap<BlockState, Palette> BLOCKSTATE_TO_PALETTE_CACHE = new HashMap<>();

    /**
     * Gets a random pixel's colour from a {@link BlockState}'s particle texture
     *
     * @param blockState the block state to get a random colour from
     * @return the colour in an array of a, r, g, b
     */
    public static int[] getRandomBlockColour(BlockState blockState, int[] tintColour) {
        TextureAtlasSprite paletteSprite;

        if(BLOCKSTATE_TO_PALETTE_CACHE.containsKey(blockState)) {
            Palette palette = BLOCKSTATE_TO_PALETTE_CACHE.get(blockState);
            return getRandomColourFromPalette(palette, tintColour);
        }

        var model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(blockState);
        TextureAtlasSprite particleSprite =
            //? if minecraft: >= 26.1 {
            model.particleMaterial().sprite();
            //? } else {
            /*model.particleIcon();
            *///? }

        Identifier particleSpriteLocation = particleSprite.contents().name();
        paletteSprite = TextureHelpers.getParticlePaletteOrBlockSprite(RegistryHelpers.getLocationFromBlock(blockState.getBlock()), particleSpriteLocation);

        SpriteContents spriteContents = paletteSprite.contents();
        ParticlePaletteAtlasManager.ParticlePaletteSettingsMetadataSection paletteMetadata = ParticlePaletteAtlasManager.getMetadataFromSprite(paletteSprite);
        Palette palette = collectValidPalettePixels(spriteContents, paletteMetadata.useBiomeTint());
        if(palette.cacheable()) {
            BLOCKSTATE_TO_PALETTE_CACHE.put(blockState, palette);
        }

        return getRandomColourFromPalette(palette, tintColour);
    }

    private static int[] getRandomColourFromPalette(Palette palette, int[] tintColour) {
        if(palette.hasTint()) {
            return ColourUtil.multiplyColours(palette.getRandomEntry().argbAsArray(), tintColour);
        }
        return palette.getRandomEntry().argbAsArray();
    }

    private static Palette collectValidPalettePixels(SpriteContents paletteSprite, boolean hasTint) {
        ArrayList<Integer> colours = new ArrayList<>();
        NativeImage image = ((SpriteContentsAccessor) paletteSprite).eg_particle_interactions$getOriginalImage();

        if(((NativeImageAccessor) (Object) image).eg_particle_interactions$getPixels() == 0L) {
            // if image is somehow not allocated, return white as fallback
            Logging.textureDebugInfo("Sprite {} is not allocated", paletteSprite.name());
            return Palette.BLANK;
        }

        for (int x = 0; x < paletteSprite.width(); x++) {
            for (int y = 0; y < paletteSprite.height(); y++) {
                int sampledColour = image.getPixel(x, y);
                int alpha = ARGB.alpha(sampledColour);

                if(alpha <= OPAQUE_PIXELS_THRESHOLD) continue;
                colours.add(sampledColour);
            }
        }

        if(colours.isEmpty()) {
            // image has no valid palette colours
            Logging.textureDebugInfo("Sprite {} contains no valid pixels for palette", paletteSprite.name());
            return Palette.BLANK;
        }

        Logging.textureDebugInfo("Sprite {} has {} valid palette pixels", paletteSprite.name(), colours.size());

        PaletteEntry[] paletteEntries = new PaletteEntry[colours.size()];
        for (int i = 0; i < colours.size(); i++) {
            paletteEntries[i] = new PaletteEntry(colours.get(i));
        }
        return new Palette(paletteEntries, true, hasTint);
    }

    private record PaletteEntry(int argb) {
        int[] argbAsArray() {
            return ColourUtil.ARGBint_to_ARGB(argb());
        }
    }

    private record Palette(PaletteEntry[] entries, boolean cacheable, boolean hasTint) {
        static final Palette BLANK = new Palette(new PaletteEntry[]{new PaletteEntry(-1)}, false);

        Palette(PaletteEntry[] entries, boolean cacheable) {
            this(entries, cacheable, false);
        }

        PaletteEntry getRandomEntry() {
            return entries()[MathHelpers.randomBetween(0, entries().length - 1)];
        }
    }

    /**
     * Clears all calculated average colours
     */
    public static void invalidateCaches() {
        ColourUtil.BLOCKSTATE_TO_PALETTE_CACHE.clear();
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
     * @param colourChannels the colour
     * @param amount amount to randomise by, 0 is no randomisation and 1 is full randomisation
     * @return the randomised colour
     */
    public static int[] randomiseNegative(int[] colourChannels, float amount) {
        int[] randomised = new int[colourChannels.length];
        for (int i = 0; i < colourChannels.length; i++) {
            randomised[i] = variateColourComponent(colourChannels[i], -MathHelpers.randomBetween(0, amount));
        }
        return randomised;
    }

    /**
     * Randomises the value all channels by the same amount, effectively changes the brightness of the colour. The colour is randomly decreased
     *
     * @param colourChannels the colour
     * @param amount amount to randomise by, 0 is no randomisation and 1 is full randomisation
     * @return the randomised colour
     */
    public static int[] randomiseNegativeUniform(int[] colourChannels, float amount) {
        float randomAmount = -MathHelpers.randomBetween(0, amount);
        int[] randomised = new int[colourChannels.length];
        for (int i = 0; i < colourChannels.length; i++) {
            randomised[i] = variateColourComponent(colourChannels[i], randomAmount);
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
