package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.particle.emitter.EmptyEmitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSetManager;

import java.util.Map;

public class ParticleOverride {
    public static final ParticleOverride EMPTY = new ParticleOverride(Map.of(ParticleOrigin.DEFAULT, Either.left(EmptyEmitter.INSTANCE)));

    public static final Codec<Either<Emitter, EmitterRuleSet>> EMITTER_CODEC = Codec.either(
        Emitters.CODEC,
        EmitterRuleSetManager.INLINE_OR_ID_CODEC
    );

    public static final Codec<ParticleOverride> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(ParticleOrigin.CODEC, EMITTER_CODEC).optionalFieldOf("emitters", Map.of()).forGetter(ParticleOverride::getEmittersByOrigin)
        ).apply(
            instance,
            ParticleOverride::new
        )
    );

    private final Map<ParticleOrigin, Either<Emitter, EmitterRuleSet>> emitterByOrigin;

    public ParticleOverride(Map<ParticleOrigin, Either<Emitter, EmitterRuleSet>> emitterByOriginMap) {
        this.emitterByOrigin = emitterByOriginMap;
    }

    public Emitter getEmitter(ParticleOrigin origin, ParticleContext context) {
        ParticleOrigin effectiveOrigin = this.emitterByOrigin.containsKey(origin) ? origin : ParticleOrigin.DEFAULT;
        if(!this.emitterByOrigin.containsKey(effectiveOrigin)) return EmptyEmitter.INSTANCE;
        return this.getEmitterFromEither(context, this.emitterByOrigin.get(effectiveOrigin));
    }

    public void spawnParticle(ParticleOrigin origin, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this.getEmitter(origin, context).spawnParticle(context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public boolean hasNoEmitter(ParticleOrigin origin, ParticleContext context) {
        return this.getEmitter(origin, context) instanceof EmptyEmitter;
    }

    protected Emitter getEmitterFromEither(ParticleContext context, Either<Emitter, EmitterRuleSet> either) {
        if(either.left().isPresent()) {
            return either.left().get();
        }
        if(either.right().isEmpty()) {
            throw new IllegalStateException("Cannot get emitter from empty either");
        }
        return either.right().get().getEmitter(context);
    }

    protected Map<ParticleOrigin, Either<Emitter, EmitterRuleSet>> getEmittersByOrigin() {
        return this.emitterByOrigin;
    }
}
