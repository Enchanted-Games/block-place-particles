package games.enchanted.eg_particle_interactions.common.util.math.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.util.math.range.DoubleRange;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record Vector3fMathModifier(Vector3fc division, Vector3fc multiplication, Vector3fc addition, Vector3fc subtraction, @Nullable List<FloatRange> clamps) {
    private static final Vector3fc ONE = new Vector3f(1);
    private static final Vector3fc ZERO = new Vector3f(0);

    public static final MapCodec<Vector3fMathModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            ExtraCodecs.VECTOR3F.optionalFieldOf("divide", ONE).forGetter(Vector3fMathModifier::division),
            ExtraCodecs.VECTOR3F.optionalFieldOf("multiply", ONE).forGetter(Vector3fMathModifier::multiplication),
            ExtraCodecs.VECTOR3F.optionalFieldOf("add", ZERO).forGetter(Vector3fMathModifier::addition),
            ExtraCodecs.VECTOR3F.optionalFieldOf("subtract", ZERO).forGetter(Vector3fMathModifier::subtraction),
            Codec.list(FloatRange.CODEC, 3, 3).optionalFieldOf("clamp").forGetter(modifier -> Optional.ofNullable(modifier.clamps))
        ).apply(
            i,
            (division, multiplication, addition, subtraction, clamps) -> {
                return new Vector3fMathModifier(division, multiplication, addition, subtraction, clamps.orElse(null));
            }
        )
    );

    public Vector3f apply(Vector3f vec) {
        Vector3f modified = vec.div(division).mul(multiplication).add(addition).sub(subtraction);
        if(this.clamps == null) return modified;
        return new Vector3f(
            this.clamps.get(0).clampWithin(modified.x()),
            this.clamps.get(1).clampWithin(modified.y()),
            this.clamps.get(2).clampWithin(modified.z())
        );
    }
}
