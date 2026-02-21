package games.enchanted.eg_particle_interactions.common.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;

public abstract class PIParticleType<T extends PIParticleOptions> {
    public PIParticleType() {
    }

    public abstract MapCodec<T> codec();

    public static class Simple extends PIParticleType<Simple> implements PIParticleOptions {
        private final MapCodec<Simple> codec = MapCodec.unit(this::type);

        public Simple() {
        }

        @Override
        public Simple type() {
            return this;
        }

        @Override
        public MapCodec<Simple> codec() {
            return codec;
        }
    }
}
