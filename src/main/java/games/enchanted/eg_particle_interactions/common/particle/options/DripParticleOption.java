package games.enchanted.eg_particle_interactions.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

public class DripParticleOption implements PIParticleOptions {
    public static final int DEFAULT_START_FALLING_TICKS = 5;

    private final PIParticleType<DripParticleOption> type;
    private final ParticleConfig config;
    private final int startFallingTicks;

    public DripParticleOption(PIParticleType<DripParticleOption> type, ParticleConfig config, int fallTicks) {
        this.type = type;
        this.config = config;
        this.startFallingTicks = fallTicks;
    }

    private static Codec<DripParticleOption> createCodec(PIParticleType<DripParticleOption> type, ParticleConfig defaultConfig) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<DripParticleOption> instance) ->
            instance.group(
                ParticleConfig.createCodec(defaultConfig).forGetter(DripParticleOption::config),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("start_falling_ticks", DEFAULT_START_FALLING_TICKS).forGetter(DripParticleOption::getStartFallingTicks)
            ).apply(
                instance,
                (
                    config,
                    fallTicks
                ) -> new DripParticleOption(
                    type,
                    config,
                    fallTicks
                )
            )
        );
    }

    public static MapCodec<DripParticleOption> codec(PIParticleType<DripParticleOption> type, ParticleConfig defaultConfig) {
        return createCodec(type, defaultConfig).fieldOf("drip_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, DripParticleOption> streamCodec(PIParticleType<DripParticleOption> type, ParticleConfig defaultConfig) {
        return ByteBufCodecs.fromCodec(createCodec(type, defaultConfig));
    }

    @Override
    public @NotNull PIParticleType<?> type() {
        return type;
    }

    @Override
    public ParticleConfig config() {
        return this.config;
    }

    public static String idPrefix() {
        return "drip";
    }

    public int getStartFallingTicks() {
        return startFallingTicks;
    }
}
