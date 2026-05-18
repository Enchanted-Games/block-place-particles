package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.particle.emitter.EmptyEmitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSetManager;

import java.util.List;
import java.util.Map;

public class ParticleOverride {
    public static final ParticleOverride EMPTY = new ParticleOverride(Map.of(ParticleOrigin.DEFAULT, EmitterRuleSet.EMPTY));

    public static final Codec<EmitterRuleSet> EMITTER_CODEC = EmitterRuleSetManager.INLINE_OR_ID_CODEC.withAlternative(
        Emitters.CODEC.xmap(
            emitter -> new EmitterRuleSet(List.of(), emitter),
            emitterRuleSet -> {
                if(emitterRuleSet.rules().isEmpty()) {
                    throw new IllegalArgumentException("Cannot convert emitter rule set with rules to emitter");
                }
                return emitterRuleSet.fallbackEmitter();
            }
        )
    );

    public static final Codec<ParticleOverride> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(ParticleOrigin.CODEC, EMITTER_CODEC).optionalFieldOf("emitters", Map.of()).forGetter(ParticleOverride::getEmittersByOrigin)
        ).apply(
            instance,
            ParticleOverride::new
        )
    );

    private final Map<ParticleOrigin, EmitterRuleSet> emitterByOrigin;

    public ParticleOverride(Map<ParticleOrigin, EmitterRuleSet> emitterByOriginMap) {
        this.emitterByOrigin = emitterByOriginMap;
    }

    public Emitter getEmitter(ParticleOrigin origin, ParticleContext context) {
        ParticleOrigin effectiveOrigin = this.emitterByOrigin.containsKey(origin) ? origin : ParticleOrigin.DEFAULT;
        if(!this.emitterByOrigin.containsKey(effectiveOrigin)) return EmptyEmitter.INSTANCE;
        return this.emitterByOrigin.get(effectiveOrigin).getEmitter(context);
    }

    public void spawnParticle(ParticleOrigin origin, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this.getEmitter(origin, context).spawnParticle(context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public boolean hasNoEmitter(ParticleOrigin origin, ParticleContext context) {
        return this.getEmitter(origin, context) instanceof EmptyEmitter;
    }

    protected Map<ParticleOrigin, EmitterRuleSet> getEmittersByOrigin() {
        return this.emitterByOrigin;
    }
}
