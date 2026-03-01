package games.enchanted.eg_particle_interactions.common.particle.options.value;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public interface ValueProvider<T> {
    T getValue(ParticleContext context);
}
