package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberFloat;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;

public class NoOpFloatModifier extends FloatFieldModifier {
    public static final MapCodec<NoOpFloatModifier> CODEC = MapCodec.unit(NoOpFloatModifier::new);

    @Override
    public MapCodec<? extends FieldModifier<Float>> codec() {
        return CODEC;
    }

    @Override
    public Float modify(Float fieldValue) {
        return fieldValue;
    }
}
