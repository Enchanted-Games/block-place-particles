package games.enchanted.eg_particle_interactions.common.util.math.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.util.math.range.IntRange;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public record IntMathModifier(int division, int multiplication, int addition, int subtraction, @Nullable IntRange clamp) {
    public static final MapCodec<IntMathModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Codec.INT.optionalFieldOf("divide", 1).forGetter(IntMathModifier::division),
            Codec.INT.optionalFieldOf("multiply", 1).forGetter(IntMathModifier::multiplication),
            Codec.INT.optionalFieldOf("add", 0).forGetter(IntMathModifier::addition),
            Codec.INT.optionalFieldOf("subtract", 0).forGetter(IntMathModifier::subtraction),
            IntRange.CODEC.optionalFieldOf("clamp").forGetter(modifier -> Optional.ofNullable(modifier.clamp()))
        ).apply(
            i,
            (division, multiplication, addition, subtraction, clamp) -> {
                return new IntMathModifier(division, multiplication, addition, subtraction, clamp.orElse(null));
            }
        )
    );

    public int apply(int value) {
        int modified = value / division * multiplication + addition - subtraction;
        if(this.clamp() == null) return modified;
        return this.clamp().clampWithin(modified);
    }
}
