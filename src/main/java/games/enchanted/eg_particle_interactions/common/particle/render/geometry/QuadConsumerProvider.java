package games.enchanted.eg_particle_interactions.common.particle.render.geometry;

import games.enchanted.eg_particle_interactions.common.particle.render.layer.ParticleLayer;

public interface QuadConsumerProvider {
    QuadConsumer getConsumer(ParticleLayer layer);
}
