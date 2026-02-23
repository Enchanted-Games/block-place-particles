package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.EmptyEmitter;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.overrides.ParticleOrigin;

import java.util.Map;

public class ParticleOverride {
    public static final Codec<ParticleOverride> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(ParticleOrigin.CODEC, Emitters.CODEC).optionalFieldOf("emitters", Map.of()).forGetter(ParticleOverride::getEmittersByOrigin)
        ).apply(
            instance,
            ParticleOverride::new
        )
    );

    private final Map<ParticleOrigin, Emitter> emitterByOrigin;

    public ParticleOverride(Map<ParticleOrigin, Emitter> emitterByOriginMap) {
        this.emitterByOrigin = emitterByOriginMap;
    }

    public Emitter getEmitter(ParticleOrigin origin) {
        ParticleOrigin effectiveOrigin = this.emitterByOrigin.containsKey(origin) ? origin : ParticleOrigin.DEFAULT;
        if(!this.emitterByOrigin.containsKey(effectiveOrigin)) return EmptyEmitter.INSTANCE;
        return this.emitterByOrigin.get(effectiveOrigin);
    }

    public void spawnParticle(ParticleOrigin origin, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this.getEmitter(origin).spawnParticle(context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    protected Map<ParticleOrigin, Emitter> getEmittersByOrigin() {
        return this.emitterByOrigin;
    }
}
