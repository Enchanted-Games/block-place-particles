package games.enchanted.eg_particle_interactions.common.util;

import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;

import java.util.Arrays;

public class ColourUtil {
    /**
     * Converts argb to an int in argb decimal format
     */
    public static int ARGB_to_ARGBint(int a, int r, int g, int b) {
        int alpha = MathHelper.clampInt(a, 0, 255);
        int red = MathHelper.clampInt(r, 0, 255);
        int green = MathHelper.clampInt(g, 0, 255);
        int blue = MathHelper.clampInt(b, 0, 255);
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
        int red = MathHelper.clampInt(r, 0, 255);
        int green = MathHelper.clampInt(g, 0, 255);
        int blue = MathHelper.clampInt(b, 0, 255);
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
            randomised[i] = variateColourComponent(colourChannels[i], -MathHelper.randomBetween(0, amount));
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
        float randomAmount = -MathHelper.randomBetween(0, amount);
        int[] randomised = new int[colourChannels.length];
        for (int i = 0; i < colourChannels.length; i++) {
            randomised[i] = variateColourComponent(colourChannels[i], randomAmount);
        }
        return randomised;
    }

    public static int variateColourComponent(int colour, float variation) {
        return MathHelper.clampInt(colour + (int)(variation * 255), 0, 255);
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
