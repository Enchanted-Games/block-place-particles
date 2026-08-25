package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberFloat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.FloatMathModifier;

public class ModifyFloatModifier extends FloatFieldModifier {
    public static final MapCodec<ModifyFloatModifier> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            FloatMathModifier.CODEC.fieldOf("modifier").forGetter(o -> o.modifier)
        ).apply(
            i,
            ModifyFloatModifier::new
        )
    );

    final FloatMathModifier modifier;

    ModifyFloatModifier(FloatMathModifier modifier) {
        this.modifier = modifier;
    }

    @Override
    public MapCodec<? extends FieldModifier<Float>> codec() {
        return CODEC;
    }

    @Override
    public Float modify(Float fieldValue) {
        return this.modifier.apply(fieldValue);
    }
}
