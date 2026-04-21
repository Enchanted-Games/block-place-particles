package games.enchanted.eg_particle_interactions.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class DustParticleOptions implements PIParticleOptions {
    private final PIParticleType<DustParticleOptions> type;
    private final ParticleConfig baseConfig;
    private final @Nullable Emitter speckEmitter;

    public DustParticleOptions(PIParticleType<DustParticleOptions> type, ParticleConfig baseConfig, @Nullable Emitter speckEmitter) {
        this.type = type;
        this.baseConfig = baseConfig;
        this.speckEmitter = speckEmitter;
    }

    private static Codec<DustParticleOptions> createCodec(PIParticleType<DustParticleOptions> type, ParticleConfig defaultConfig) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<DustParticleOptions> instance) ->
            instance.group(
                ParticleConfig.createCodec(defaultConfig).forGetter(DustParticleOptions::config),
                Emitters.CODEC.optionalFieldOf("speck_emitter").forGetter(dustParticleOptions -> Optional.ofNullable(dustParticleOptions.getSpeckEmitter()))
            ).apply(
                instance,
                (
                    baseConfig,
                    emitter
                ) -> new DustParticleOptions(
                    type,
                    baseConfig,
                    emitter.orElse(null)
                )
            )
        );
    }

    public static MapCodec<DustParticleOptions> codec(PIParticleType<DustParticleOptions> type, ParticleConfig defaultConfig) {
        return createCodec(type, defaultConfig).fieldOf("dust_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, DustParticleOptions> streamCodec(PIParticleType<DustParticleOptions> type, ParticleConfig defaultConfig) {
        return ByteBufCodecs.fromCodec(createCodec(type, defaultConfig));
    }

    @Override
    public @NotNull PIParticleType<?> type() {
        return this.type;
    }

    public @Nullable Emitter getSpeckEmitter() {
        return this.speckEmitter;
    }

    @Override
    public ParticleConfig config() {
        return this.baseConfig;
    }

    public static String idPrefix() {
        return "dust";
    }
}
