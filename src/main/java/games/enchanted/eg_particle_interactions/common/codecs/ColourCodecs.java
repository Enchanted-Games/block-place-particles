package games.enchanted.eg_particle_interactions.common.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;

import java.util.List;
import java.util.Locale;

public class ColourCodecs {
    public static Codec<Integer> RGBA_HEX_CODEC = Codec.STRING.comapFlatMap(
        input -> {
            if(!input.matches("^#[0-9a-fA-F]{8}$")) {
                return DataResult.error(() -> "Invalid hexadecimal colour. Value '" + input + "' is not valid");
            }
            try {
                int parsedRgb = Integer.parseInt(input.substring(1), 16);
                return DataResult.success(parsedRgb);
            }
            catch (NumberFormatException numberFormatException) {
                return DataResult.error(() -> "Invalid hexadecimal value '" + input + "'");
            }
        },
        input -> String.format(Locale.ROOT, "#%06X", input)
    );

    public static Codec<Integer> RGBA_INT_LIST_CODEC = Codec.INT.listOf().comapFlatMap(
        (input) -> {
            if(input.size() != 3) {
                return DataResult.error(() -> "Invalid colour. Must be a list of 3 ints, got size of '" + input.size() + "' instead.");
            }
            return DataResult.success(ColourUtil.RGB_to_RGBint(input.get(0), input.get(1), input.get(2)));
        },
        (input) -> {
            int[] rgba = ColourUtil.ARGBint_to_ARGB(input);
            return List.of(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
    );

    public static Codec<Integer> HEX_OR_ARGB_LIST_CODEC = Codec.withAlternative(RGBA_HEX_CODEC, RGBA_INT_LIST_CODEC);
}