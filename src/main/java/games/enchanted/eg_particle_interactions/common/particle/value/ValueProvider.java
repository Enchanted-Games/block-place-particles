package games.enchanted.eg_particle_interactions.common.particle.value;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public interface ValueProvider<T> {
    T getValue(ParticleContext context);
}
