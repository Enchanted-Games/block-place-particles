package games.enchanted.eg_particle_interactions.common.particle.emitter.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.particle.emitter.EmptyEmitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions.EmitterCondition;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions.EmitterConditions;

import java.util.List;

public record EmitterRule(List<EmitterCondition> conditions, Emitter emitter) {
    public static final Codec<EmitterRule> CODEC = RecordCodecBuilder.create(
        i -> i.group(
            Codec.list(EmitterConditions.CODEC).optionalFieldOf("conditions", List.of()).forGetter(EmitterRule::conditions),
            Emitters.CODEC.optionalFieldOf("emitter",EmptyEmitter.INSTANCE).forGetter(EmitterRule::emitter)
        ).apply(
            i,
            EmitterRule::new
        )
    );

    public boolean matches(ParticleContext context) {
        for (EmitterCondition rule : conditions()) {
            if(!rule.matches(context)) continue;
            return true;
        }
        return false;
    }
}
