package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberInt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;

public class StaticValueIntModifier extends IntFieldModifier {
    public static final MapCodec<StaticValueIntModifier> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.INT.fieldOf("value").forGetter(StaticValueIntModifier::getValue)
        ).apply(
            i,
            StaticValueIntModifier::new
        )
    );

    final int value;

    StaticValueIntModifier(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public MapCodec<? extends FieldModifier<Integer>> codec() {
        return CODEC;
    }

    @Override
    public Integer modify(Integer fieldValue) {
        return this.value;
    }
}
