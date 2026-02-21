package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public interface ColourSource {
    int[] getARGB(ParticleContext context);
}
