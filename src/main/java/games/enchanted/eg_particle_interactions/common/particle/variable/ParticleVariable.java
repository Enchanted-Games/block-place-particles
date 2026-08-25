package games.enchanted.eg_particle_interactions.common.particle.variable;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.ParticleField;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;

public record ParticleVariable<T>(ParticleField<T> field, FieldModifier<T> fieldModifier) {
    public static <T> Codec<ParticleVariable<T>> createCodec(ParticleField<T> field, Codec<FieldModifier<T>> fieldModifierCodec) {
        return fieldModifierCodec.xmap(
            modifier -> new ParticleVariable<>(field, modifier),
            ParticleVariable::fieldModifier
        );
    }

    public T getInitial(ParticleInteractionsParticle particle) {
        return fieldModifier.modify(field.getInitial(particle));
    }
}
