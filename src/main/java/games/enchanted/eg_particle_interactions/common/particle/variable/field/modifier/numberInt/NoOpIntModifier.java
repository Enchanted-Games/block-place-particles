package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberInt;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;

public class NoOpIntModifier extends IntFieldModifier {
    public static final MapCodec<NoOpIntModifier> CODEC = MapCodec.unit(NoOpIntModifier::new);

    @Override
    public MapCodec<? extends FieldModifier<Integer>> codec() {
        return CODEC;
    }

    @Override
    public Integer modify(Integer fieldValue) {
        return fieldValue;
    }
}
