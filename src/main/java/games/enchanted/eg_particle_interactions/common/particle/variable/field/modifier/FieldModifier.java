package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier;

import com.mojang.serialization.MapCodec;

public abstract class FieldModifier<T> {
    public abstract MapCodec<? extends FieldModifier<T>> codec();

    public abstract T modify(T fieldValue);
}
