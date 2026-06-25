package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;

public abstract class EmitterCondition {
    public static final String POS_OFFSET_FIELD = "position_offset";
    public static final String PREDICATE_FIELD = "predicate";

    public abstract boolean matches(ParticleContext context);

    public abstract MapCodec<? extends EmitterCondition> codec();
}