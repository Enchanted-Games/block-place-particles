package games.enchanted.eg_particle_interactions.common.util.math;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record Vector3fMathModifier(Vector3fc division, Vector3fc multiplication, Vector3fc addition, Vector3fc subtraction) {
    private static final Vector3fc ONE = new Vector3f(1);
    private static final Vector3fc ZERO = new Vector3f(0);

    public static final MapCodec<Vector3fMathModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            ExtraCodecs.VECTOR3F.optionalFieldOf("divide", ONE).forGetter(Vector3fMathModifier::division),
            ExtraCodecs.VECTOR3F.optionalFieldOf("multiply", ONE).forGetter(Vector3fMathModifier::multiplication),
            ExtraCodecs.VECTOR3F.optionalFieldOf("add", ZERO).forGetter(Vector3fMathModifier::addition),
            ExtraCodecs.VECTOR3F.optionalFieldOf("subtract", ZERO).forGetter(Vector3fMathModifier::subtraction)
        ).apply(
            i,
            Vector3fMathModifier::new
        )
    );

    public Vector3f apply(Vector3f vec) {
        return vec.div(division).mul(multiplication).add(addition).sub(subtraction);
    }
}
