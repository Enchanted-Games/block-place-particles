package games.enchanted.eg_particle_interactions.common.particle.types.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class SimpleParticleOptions implements PIParticleOptions {
    PIParticleType<SimpleParticleOptions> type;

    public SimpleParticleOptions(PIParticleType<SimpleParticleOptions> type) {
        this.type = type;
    }

    private static Codec<SimpleParticleOptions> createCodec(PIParticleType<SimpleParticleOptions> type) {
        return MapCodec.unit(new SimpleParticleOptions(type)).codec();
    }

    public static MapCodec<SimpleParticleOptions> codec(PIParticleType<SimpleParticleOptions> type) {
        return createCodec(type).optionalFieldOf("simple_options", new SimpleParticleOptions(type));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, SimpleParticleOptions> streamCodec(PIParticleType<SimpleParticleOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public PIParticleType<?> type() {
        return this.type;
    }
}
