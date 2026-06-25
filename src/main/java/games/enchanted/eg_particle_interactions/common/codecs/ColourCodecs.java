package games.enchanted.eg_particle_interactions.common.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.util.ARGB;

import java.util.List;
import java.util.Locale;

public class ColourCodecs {
    public static Codec<Integer> RGB_HEX_CODEC = Codec.STRING.comapFlatMap(
        input -> {
            if(!input.matches("^#[0-9a-fA-F]{6}$")) {
                return DataResult.error(() -> "Invalid hexadecimal colour. Value '" + input + "' is not valid");
            }
            try {
                int parsedRgb = Integer.parseInt(input.substring(1), 16);
                return DataResult.success(ARGB.color(255, parsedRgb));
            }
            catch (NumberFormatException numberFormatException) {
                return DataResult.error(() -> "Invalid hexadecimal value '" + input + "'");
            }
        },
        input -> String.format(Locale.ROOT, "#%06X", input)
    );

    public static Codec<Integer> ARGB_HEX_CODEC = Codec.STRING.comapFlatMap(
        input -> {
            if(!input.matches("^#[0-9a-fA-F]{8}$")) {
                return DataResult.error(() -> "Invalid hexadecimal colour. Value '" + input + "' is not valid");
            }
            try {
                int parsedArgb = Integer.parseInt(input.substring(1), 16);
                return DataResult.success(parsedArgb);
            }
            catch (NumberFormatException numberFormatException) {
                return DataResult.error(() -> "Invalid hexadecimal value '" + input + "'");
            }
        },
        input -> String.format(Locale.ROOT, "#%06X", input)
    );

    public static Codec<Integer> ARGB_INT_LIST_CODEC = Codec.INT.listOf().comapFlatMap(
        (input) -> {
            if(input.size() != 4) {
                return DataResult.error(() -> "Invalid colour. Must be a list of 4 ints, got size of '" + input.size() + "' instead.");
            }
            return DataResult.success(ColourUtil.ARGB_to_ARGBint(input.get(0), input.get(1), input.get(2), input.get(3)));
        },
        (input) -> {
            int[] rgba = ColourUtil.ARGBint_to_ARGB(input);
            return List.of(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
    );

    public static Codec<Integer> HEX_OR_ARGB_LIST_CODEC = Codec.withAlternative(RGB_HEX_CODEC.withAlternative(ARGB_HEX_CODEC), ARGB_INT_LIST_CODEC);
}