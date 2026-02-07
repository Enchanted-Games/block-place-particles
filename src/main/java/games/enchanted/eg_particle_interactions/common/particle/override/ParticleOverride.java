package games.enchanted.eg_particle_interactions.common.particle.override;

import games.enchanted.eg_particle_interactions.common.particle.overrides.ParticleOrigin;

import java.util.Map;

public class ParticleOverride {
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
}
