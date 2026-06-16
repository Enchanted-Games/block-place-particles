package games.enchanted.eg_particle_interactions.common.particle.types.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class SimpleParticleOptions implements PIParticleOptions {
    PIParticleType<SimpleParticleOptions> type;
    ParticleConfig config;

    public SimpleParticleOptions(PIParticleType<SimpleParticleOptions> type, ParticleConfig config) {
        this.type = type;
        this.config = config;
    }

    private static Codec<SimpleParticleOptions> createCodec(PIParticleType<SimpleParticleOptions> type, ParticleConfig defaultConfig) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<SimpleParticleOptions> instance) ->
            instance.group(
                ParticleConfig.createCodec(defaultConfig).forGetter(SimpleParticleOptions::config)
            ).apply(
                instance,
                (
                    baseConfig
                ) -> new SimpleParticleOptions(
                    type,
                    baseConfig
                )
            )
        );
    }

    public static MapCodec<SimpleParticleOptions> codec(PIParticleType<SimpleParticleOptions> type, ParticleConfig defaultConfig) {
        return createCodec(type, defaultConfig).optionalFieldOf("simple_options", new SimpleParticleOptions(type, defaultConfig));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, SimpleParticleOptions> streamCodec(PIParticleType<SimpleParticleOptions> type, ParticleConfig defaultConfig) {
        return ByteBufCodecs.fromCodec(createCodec(type, defaultConfig));
    }

    @Override
    public PIParticleType<?> type() {
        return this.type;
    }

    @Override
    public ParticleConfig config() {
        return this.config;
    }

    public static String idPrefix() {
        return "simple";
    }
}
