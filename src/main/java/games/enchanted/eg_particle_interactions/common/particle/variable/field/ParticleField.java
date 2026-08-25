package games.enchanted.eg_particle_interactions.common.particle.variable.field;

import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public interface ParticleField<T> {
    T getInitial(ParticleInteractionsParticle particle);

    T getCurrent(ParticleInteractionsParticle particle);

    void set(ParticleInteractionsParticle particle, T value);
}
