package games.enchanted.eg_particle_interactions.common.registry.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;

public abstract class PIParticleType<T extends PIParticleOptions> {
    protected PIParticleType() {
    }

    public abstract MapCodec<T> codec();

    public static class Simple extends PIParticleType<Simple> implements PIParticleOptions {
        protected Simple() {
        }

        @Override
        public PIParticleType<?> type() {
            return this;
        }

        @Override
        public MapCodec<Simple> codec() {
            return MapCodec.unit(Simple::new);
        }
    }
}
