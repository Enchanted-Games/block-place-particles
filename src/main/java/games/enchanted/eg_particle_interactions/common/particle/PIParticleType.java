package games.enchanted.eg_particle_interactions.common.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;

public abstract class PIParticleType<T extends PIParticleOptions> {
    final ParticleComponentMap defaultComponents;

    public PIParticleType(ParticleComponentMap defaultComponents) {
        this.defaultComponents = defaultComponents;
    }

    public ParticleComponentMap defaultComponents() {
        return this.defaultComponents;
    }

    public abstract MapCodec<T> codec();
}
