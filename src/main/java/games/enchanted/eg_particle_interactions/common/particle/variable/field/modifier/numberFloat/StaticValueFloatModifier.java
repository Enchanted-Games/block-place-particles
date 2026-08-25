package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberFloat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;

public class StaticValueFloatModifier extends FloatFieldModifier {
    public static final MapCodec<StaticValueFloatModifier> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.FLOAT.fieldOf("value").forGetter(StaticValueFloatModifier::getValue)
        ).apply(
            i,
            StaticValueFloatModifier::new
        )
    );

    final float value;

    StaticValueFloatModifier(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public MapCodec<? extends FieldModifier<Float>> codec() {
        return CODEC;
    }

    @Override
    public Float modify(Float fieldValue) {
        return this.value;
    }
}
