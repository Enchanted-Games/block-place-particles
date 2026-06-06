package games.enchanted.eg_particle_interactions.common.util.math.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.util.math.range.DoubleRange;
import org.joml.Vector3d;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record Vector3dMathModifier(Vector3d division, Vector3d multiplication, Vector3d addition, Vector3d subtraction, @Nullable List<DoubleRange> clamps) {
    private static final Vector3d ONE = new Vector3d(1);
    private static final Vector3d ZERO = new Vector3d(0);

    public static final MapCodec<Vector3dMathModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            ModCodecs.VECTOR3D.optionalFieldOf("divide", ONE).forGetter(Vector3dMathModifier::division),
            ModCodecs.VECTOR3D.optionalFieldOf("multiply", ONE).forGetter(Vector3dMathModifier::multiplication),
            ModCodecs.VECTOR3D.optionalFieldOf("add", ZERO).forGetter(Vector3dMathModifier::addition),
            ModCodecs.VECTOR3D.optionalFieldOf("subtract", ZERO).forGetter(Vector3dMathModifier::subtraction),
            Codec.list(DoubleRange.CODEC, 3, 3).optionalFieldOf("clamp").forGetter(modifier -> Optional.ofNullable(modifier.clamps))
        ).apply(
            i,
            (division, multiplication, addition, subtraction, clamps) -> {
                return new Vector3dMathModifier(division, multiplication, addition, subtraction, clamps.orElse(null));
            }
        )
    );

    public Vector3d apply(Vector3d vec) {
        Vector3d modified = vec.div(division).mul(multiplication).add(addition).sub(subtraction);
        if(this.clamps == null) return modified;
        return new Vector3d(
            this.clamps.get(0).clampWithin(modified.x()),
            this.clamps.get(1).clampWithin(modified.y()),
            this.clamps.get(2).clampWithin(modified.z())
        );
    }
}
