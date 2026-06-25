package games.enchanted.eg_particle_interactions.common.util.math.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public record FloatMathModifier(float division, float multiplication, float addition, float subtraction, @Nullable FloatRange clamp) {
    public static final MapCodec<FloatMathModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Codec.FLOAT.optionalFieldOf("divide", 1f).forGetter(FloatMathModifier::division),
            Codec.FLOAT.optionalFieldOf("multiply", 1f).forGetter(FloatMathModifier::multiplication),
            Codec.FLOAT.optionalFieldOf("add", 0f).forGetter(FloatMathModifier::addition),
            Codec.FLOAT.optionalFieldOf("subtract", 0f).forGetter(FloatMathModifier::subtraction),
            FloatRange.CODEC.optionalFieldOf("clamp").forGetter(modifier -> Optional.ofNullable(modifier.clamp()))
        ).apply(
            i,
            (division, multiplication, addition, subtraction, clamp) -> {
                return new FloatMathModifier(division, multiplication, addition, subtraction, clamp.orElse(null));
            }
        )
    );

    public float apply(float value) {
        float modified = value / division * multiplication + addition - subtraction;
        if(this.clamp() == null) return modified;
        return this.clamp().clampWithin(modified);
    }
}
