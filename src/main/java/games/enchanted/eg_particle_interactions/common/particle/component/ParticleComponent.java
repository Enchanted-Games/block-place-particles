package games.enchanted.eg_particle_interactions.common.particle.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface ParticleComponent<T> {
    Codec<T> codec();

    StreamCodec<? extends FriendlyByteBuf, T> streamCodec();

    static <T> ParticleComponent<T> create(Codec<T> codec, StreamCodec<? extends FriendlyByteBuf, T> streamCodec) {
        return new ParticleComponent<>() {
            final Codec<T> componentCodec = codec;
            final StreamCodec<? extends FriendlyByteBuf, T> componentStreamCodec = streamCodec;

            @Override
            public Codec<T> codec() {
                return this.componentCodec;
            }

            @Override
            public StreamCodec<? extends FriendlyByteBuf, T> streamCodec() {
                return this.componentStreamCodec;
            }
        };
    }
}
