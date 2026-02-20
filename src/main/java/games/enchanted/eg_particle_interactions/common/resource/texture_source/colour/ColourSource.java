package games.enchanted.eg_particle_interactions.common.resource.texture_source.colour;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public interface ColourSource {
    int[] getARGB(ParticleContext context);
}
