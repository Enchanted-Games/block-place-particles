package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberInt;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.FloatMathModifier;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.IntMathModifier;

public class ModifyIntModifier extends IntFieldModifier {
    public static final MapCodec<ModifyIntModifier> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            IntMathModifier.CODEC.fieldOf("modifier").forGetter(o -> o.modifier)
        ).apply(
            i,
            ModifyIntModifier::new
        )
    );

    final IntMathModifier modifier;

    ModifyIntModifier(IntMathModifier modifier) {
        this.modifier = modifier;
    }

    @Override
    public MapCodec<? extends FieldModifier<Integer>> codec() {
        return CODEC;
    }

    @Override
    public Integer modify(Integer fieldValue) {
        return this.modifier.apply(fieldValue);
    }
}
