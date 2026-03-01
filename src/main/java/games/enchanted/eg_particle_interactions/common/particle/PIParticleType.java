package games.enchanted.eg_particle_interactions.common.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;

public abstract class PIParticleType<T extends PIParticleOptions> {
    public PIParticleType() {
    }

    public abstract MapCodec<T> codec();
}
