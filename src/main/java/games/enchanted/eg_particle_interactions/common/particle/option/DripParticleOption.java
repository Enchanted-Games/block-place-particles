package games.enchanted.eg_particle_interactions.common.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

public class DripParticleOption implements ParticleOptions {
    private final ParticleType<DripParticleOption> type;
    private final int startFallingTicks;

    public DripParticleOption(ParticleType<DripParticleOption> type, int fallTicks) {
        this.type = type;
        this.startFallingTicks = fallTicks;
    }

    private static Codec<DripParticleOption> createCodec(ParticleType<DripParticleOption> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<DripParticleOption> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("start_falling_ticks", 5).forGetter(DripParticleOption::getStartFallingTicks)
            ).apply(
                instance,
                (
                    Integer fallTicks
                ) -> new DripParticleOption(
                    type,
                    fallTicks
                )
            )
        );
    }

    public static MapCodec<DripParticleOption> codec(ParticleType<DripParticleOption> type) {
        return createCodec(type).fieldOf("drip_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, DripParticleOption> streamCodec(ParticleType<DripParticleOption> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return type;
    }

    public int getStartFallingTicks() {
        return startFallingTicks;
    }
}
