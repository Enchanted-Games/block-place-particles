package games.enchanted.eg_particle_interactions.common.particle.options;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;

public interface PIParticleOptions {
    PIParticleType<?> type();
    ParticleConfig config();
}
