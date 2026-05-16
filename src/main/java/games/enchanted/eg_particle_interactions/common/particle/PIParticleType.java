package games.enchanted.eg_particle_interactions.common.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;

public abstract class PIParticleType<T extends PIParticleOptions> {
    final ParticleComponentMap components;

    public PIParticleType(ParticleComponentMap components) {
        this.components = components;
    }

    public ParticleComponentMap components() {
        return this.components;
    }

    public abstract MapCodec<T> codec();
}
