package games.enchanted.eg_particle_interactions.common.particle.types.options;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleType;

public interface PIParticleOptions {
    PIParticleType<?> type();
    ParticleConfig config();
}
