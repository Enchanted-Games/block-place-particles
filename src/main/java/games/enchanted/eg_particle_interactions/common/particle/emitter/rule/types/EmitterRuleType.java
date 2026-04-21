package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;

public abstract class EmitterRuleType {
    public static final String EMITTER_FIELD = "emitter";
    public static final String POS_OFFSET_FIELD = "position_offset";
    public static final String PREDICATE_FIELD = "predicate";

    protected final Emitter emitter;

    protected EmitterRuleType(Emitter emitter) {
        this.emitter = emitter;
    }

    public Emitter getEmitter() {
        return this.emitter;
    }

    public abstract boolean matches(ParticleContext context);

    public abstract MapCodec<? extends EmitterRuleType> codec();
}