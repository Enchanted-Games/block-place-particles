package games.enchanted.eg_particle_interactions.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class SparkParticleOptions implements PIParticleOptions {
    private final PIParticleType<SparkParticleOptions> type;
    private final ParticleConfig config;
    private final @Nullable Emitter speckEmitter;

    public SparkParticleOptions(PIParticleType<SparkParticleOptions> type, ParticleConfig config, @Nullable Emitter flashEmitter) {
        this.type = type;
        this.config = config;
        this.speckEmitter = flashEmitter;
    }

    private static Codec<SparkParticleOptions> createCodec(PIParticleType<SparkParticleOptions> type, ParticleConfig defaultConfig) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<SparkParticleOptions> instance) ->
            instance.group(
                ParticleConfig.createCodec(defaultConfig).forGetter(SparkParticleOptions::config),
                Emitters.CODEC.optionalFieldOf("flash_emitter").forGetter(dustParticleOptions -> Optional.ofNullable(dustParticleOptions.getFlashEmitter()))
            ).apply(
                instance,
                (
                    config,
                    emitter
                ) -> new SparkParticleOptions(
                    type,
                    config,
                    emitter.orElse(null)
                )
            )
        );
    }

    public static MapCodec<SparkParticleOptions> codec(PIParticleType<SparkParticleOptions> type, ParticleConfig defaultConfig) {
        return createCodec(type, defaultConfig).fieldOf("spark_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, SparkParticleOptions> streamCodec(PIParticleType<SparkParticleOptions> type, ParticleConfig defaultConfig) {
        return ByteBufCodecs.fromCodec(createCodec(type, defaultConfig));
    }

    @Override
    public @NotNull PIParticleType<?> type() {
        return this.type;
    }

    @Override
    public ParticleConfig config() {
        return this.config;
    }

    public static String idPrefix() {
        return "spark";
    }

    public @Nullable Emitter getFlashEmitter() {
        return this.speckEmitter;
    }
}
