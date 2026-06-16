package games.enchanted.eg_particle_interactions.common.particle.options;

import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.ParticleInteractionsEmitter;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomIntProvider;

import java.util.function.Supplier;

public class DefaultParticles {
    public static final ParticleConfig LAVA_POP_CONFIG = new ParticleConfig(
        ParticleConfig.DEFAULT_GRAVITY,
        new RandomIntProvider(30, 36),
        ParticleConfig.DEFAULT_COLLISION_SIZE,
        0f
    );
}
