package games.enchanted.eg_particle_interactions.common.util.math;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import org.joml.Vector3d;

public record Vector3dMathModifier(Vector3d division, Vector3d multiplication, Vector3d addition, Vector3d subtraction) {
    private static final Vector3d ONE = new Vector3d(1);
    private static final Vector3d ZERO = new Vector3d(0);

    public static final MapCodec<Vector3dMathModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            ModCodecs.VECTOR3D.optionalFieldOf("divide", ONE).forGetter(Vector3dMathModifier::division),
            ModCodecs.VECTOR3D.optionalFieldOf("multiply", ONE).forGetter(Vector3dMathModifier::multiplication),
            ModCodecs.VECTOR3D.optionalFieldOf("add", ZERO).forGetter(Vector3dMathModifier::addition),
            ModCodecs.VECTOR3D.optionalFieldOf("subtract", ZERO).forGetter(Vector3dMathModifier::subtraction)
        ).apply(
            i,
            Vector3dMathModifier::new
        )
    );

    public Vector3d apply(Vector3d vec) {
        return vec.div(division).mul(multiplication).add(addition).sub(subtraction);
    }
}
