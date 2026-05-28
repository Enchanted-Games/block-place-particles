package games.enchanted.eg_particle_interactions.common.util.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector3d;

public record IntMathModifier(int division, int multiplication, int addition, int subtraction) {
    public static final MapCodec<IntMathModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Codec.INT.optionalFieldOf("divide", 1).forGetter(IntMathModifier::division),
            Codec.INT.optionalFieldOf("multiply", 1).forGetter(IntMathModifier::multiplication),
            Codec.INT.optionalFieldOf("add", 0).forGetter(IntMathModifier::addition),
            Codec.INT.optionalFieldOf("subtract", 0).forGetter(IntMathModifier::subtraction)
        ).apply(
            i,
            IntMathModifier::new
        )
    );

    public int apply(int value) {
        return value / division * multiplication + addition - subtraction;
    }
}
