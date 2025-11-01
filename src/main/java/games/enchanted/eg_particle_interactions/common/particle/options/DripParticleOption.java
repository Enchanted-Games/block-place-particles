package games.enchanted.eg_particle_interactions.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

public class DripParticleOption implements ParticleOptions {
    public static final int DEFAULT_START_FALLING_TICKS = 5;
    public static final float DEFAULT_GRAVITY = 0.05F;
    public static final float DEFAULT_GRAVITY_RANDOMNESS = 0.0F;

    public static final DripParticleOption FALLING_HONEY_DROP = new DripParticleOption(ModParticleTypes.HONEY_DROP, 0, 0.02f, 0.03f);
    public static final DripParticleOption HANGING_HONEY_DROP = new DripParticleOption(ModParticleTypes.HONEY_DROP, 30, 0.02f, 0.03f);

    private final ParticleType<DripParticleOption> type;
    private final int startFallingTicks;
    private final float gravity;
    private final float gravityRandomness;

    public DripParticleOption(ParticleType<DripParticleOption> type, int fallTicks, float gravity, float gravityRandomness) {
        this.type = type;
        this.startFallingTicks = fallTicks;
        this.gravity = gravity;
        this.gravityRandomness = gravityRandomness;
    }

    private static Codec<DripParticleOption> createCodec(ParticleType<DripParticleOption> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<DripParticleOption> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("start_falling_ticks", DEFAULT_START_FALLING_TICKS).forGetter(DripParticleOption::getStartFallingTicks),
                Codec.FLOAT.optionalFieldOf("gravity", DEFAULT_GRAVITY).forGetter(DripParticleOption::getGravity),
                Codec.FLOAT.optionalFieldOf("gravity_randomess", DEFAULT_GRAVITY_RANDOMNESS).forGetter(DripParticleOption::getGravity)
            ).apply(
                instance,
                (
                    Integer fallTicks,
                    Float gravity,
                    Float gravityRandomness
                ) -> new DripParticleOption(
                    type,
                    fallTicks,
                    gravity,
                    gravityRandomness
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

    public float getGravity() {
        return gravity;
    }

    public float getGravityRandomness() {
        return gravityRandomness;
    }
}
