package games.enchanted.eg_particle_interactions.common.util.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FloatMathModifier(float division, float multiplication, float addition, float subtraction) {
    public static final MapCodec<FloatMathModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Codec.FLOAT.optionalFieldOf("divide", 1f).forGetter(FloatMathModifier::division),
            Codec.FLOAT.optionalFieldOf("multiply", 1f).forGetter(FloatMathModifier::multiplication),
            Codec.FLOAT.optionalFieldOf("add", 0f).forGetter(FloatMathModifier::addition),
            Codec.FLOAT.optionalFieldOf("subtract", 0f).forGetter(FloatMathModifier::subtraction)
        ).apply(
            i,
            FloatMathModifier::new
        )
    );

    public float apply(float value) {
        return value / division * multiplication + addition - subtraction;
    }
}
