package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.overrides.ParticleOrigin;

import java.util.Map;

public class ParticleOverride {
    public static final Codec<ParticleOverride> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Emitter.CODEC.fieldOf("default_emitter").forGetter(ParticleOverride::getDefaultEmitter),
            Codec.unboundedMap(ParticleOrigin.CODEC, Emitter.CODEC).optionalFieldOf("emitters", Map.of()).forGetter(ParticleOverride::getEmittersByOrigin)
        ).apply(
            instance,
            ParticleOverride::new
        )
    );

    private final Map<ParticleOrigin, Emitter> emitterByOrigin;
    private final Emitter defaultEmitter;

    public ParticleOverride(Emitter defaultEmitter, Map<ParticleOrigin, Emitter> emitterByOriginMap) {
        this.emitterByOrigin = emitterByOriginMap;
        this.defaultEmitter = defaultEmitter;
    }

    public Emitter getEmitter(ParticleOrigin origin) {
        if(!this.emitterByOrigin.containsKey(origin)) return this.defaultEmitter;
        return this.emitterByOrigin.get(origin);
    }

    public void spawnParticle(ParticleOrigin origin, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this.getEmitter(origin).spawnParticle(context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    protected Emitter getDefaultEmitter() {
        return this.defaultEmitter;
    }

    protected Map<ParticleOrigin, Emitter> getEmittersByOrigin() {
        return this.emitterByOrigin;
    }
}
