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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ArcEmitterOptions implements ParticleOptions {
    public static final int TICK_INTERVAL_DEFAULT = 1;
    public static final int REPEAT_DEFAULT = 5;

    private final ParticleType<ArcEmitterOptions> type;
    private final int length;
    private final int splits;
    private final int angleVariance;
    private final int tickInterval;
    private final int repeat;
    @Nullable private final Integer initialAngleXDeg;
    @Nullable private final Integer initialAngleYDeg;

    public ArcEmitterOptions(ParticleType<ArcEmitterOptions> type, int length, int splits, int angleVariance) {
        this(type, length, splits, angleVariance, REPEAT_DEFAULT, TICK_INTERVAL_DEFAULT, null, null);
    }

    public ArcEmitterOptions(ParticleType<ArcEmitterOptions> type, int length, int splits, int angleVariance, int repeat, int tickInterval, @Nullable Integer initialAngleXDeg, @Nullable Integer initialAngleYDeg) {
        this.type = type;
        this.length = length;
        this.splits = splits;
        this.angleVariance = angleVariance;
        this.repeat = repeat;
        this.tickInterval = tickInterval;
        this.initialAngleXDeg = initialAngleXDeg;
        this.initialAngleYDeg = initialAngleYDeg;
    }

    private static Codec<ArcEmitterOptions> createCodec(ParticleType<ArcEmitterOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<ArcEmitterOptions> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("length", 5).forGetter(ArcEmitterOptions::getLength),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("splits", 2).forGetter(ArcEmitterOptions::getSplits),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("angle_variance", 45).forGetter(ArcEmitterOptions::getAngleVariance),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("repeat", REPEAT_DEFAULT).forGetter(ArcEmitterOptions::getRepeat),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("interval", TICK_INTERVAL_DEFAULT).forGetter(ArcEmitterOptions::getTickInterval),
                Codec.INT.optionalFieldOf("initial_x_angle").forGetter(ArcEmitterOptions::getInitialAngleXDeg),
                Codec.INT.optionalFieldOf("initial_y_angle").forGetter(ArcEmitterOptions::getInitialAngleYDeg)
            ).apply(
                instance,
                (
                    Integer length,
                    Integer splits,
                    Integer angleVariance,
                    Integer repeat,
                    Integer tickInterval,
                    Optional<Integer> initialAngleXDeg,
                    Optional<Integer> initialAngleYDeg
                ) -> new ArcEmitterOptions(
                    type,
                    length,
                    splits,
                    angleVariance,
                    repeat,
                    tickInterval,
                    initialAngleXDeg.orElse(null),
                    initialAngleYDeg.orElse(null)
                )
            )
        );
    }

    public static MapCodec<ArcEmitterOptions> codec(ParticleType<ArcEmitterOptions> type) {
        return createCodec(type).fieldOf("emitter_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, ArcEmitterOptions> streamCodec(ParticleType<ArcEmitterOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull ParticleType<ArcEmitterOptions> getType() {
        return this.type;
    }

    public int getLength() {
        return length;
    }

    public int getSplits() {
        return splits;
    }

    public int getAngleVariance() {
        return angleVariance;
    }

    public int getTickInterval() {
        return tickInterval;
    }

    public int getRepeat() {
        return repeat;
    }

    public Optional<Integer> getInitialAngleXDeg() {
        return Optional.ofNullable(initialAngleXDeg);
    }

    public Optional<Integer> getInitialAngleYDeg() {
        return Optional.ofNullable(initialAngleYDeg);
    }
}
