package games.enchanted.eg_particle_interactions.common.particle.types.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSetManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ArcEmitterOptions implements PIParticleOptions {
    public static final int TICK_INTERVAL_DEFAULT = 1;
    public static final int REPEAT_DEFAULT = 5;

    private final PIParticleType<ArcEmitterOptions> type;
    private final int length;
    private final int splits;
    private final int angleVariance;
    private final int tickInterval;
    private final int repeat;
    @Nullable private final Integer initialAngleXDeg;
    @Nullable private final Integer initialAngleYDeg;
    private final EmitterRuleSet.Reference emitterRuleSet;

    public ArcEmitterOptions(PIParticleType<ArcEmitterOptions> type, int length, int splits, int angleVariance, int repeat, int tickInterval, @Nullable Integer initialAngleXDeg, @Nullable Integer initialAngleYDeg, EmitterRuleSet.Reference emitterRuleSet) {
        this.type = type;
        this.length = length;
        this.splits = splits;
        this.angleVariance = angleVariance;
        this.repeat = repeat;
        this.tickInterval = tickInterval;
        this.initialAngleXDeg = initialAngleXDeg;
        this.initialAngleYDeg = initialAngleYDeg;
        this.emitterRuleSet = emitterRuleSet;
    }

    public ArcEmitterOptions(PIParticleType<ArcEmitterOptions> type, int length, int splits, int angleVariance, int repeat, int tickInterval, @Nullable Integer initialAngleXDeg, @Nullable Integer initialAngleYDeg, EmitterRuleSet emitterRuleSet) {
        this(type, length, splits, angleVariance, repeat, tickInterval, initialAngleXDeg, initialAngleYDeg, new EmitterRuleSet.InlineRef(emitterRuleSet));
    }

    private static Codec<ArcEmitterOptions> createCodec(PIParticleType<ArcEmitterOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<ArcEmitterOptions> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("length", 5).forGetter(ArcEmitterOptions::getLength),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("splits", 2).forGetter(ArcEmitterOptions::getSplits),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("angle_variance", 45).forGetter(ArcEmitterOptions::getAngleVariance),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("repeat", REPEAT_DEFAULT).forGetter(ArcEmitterOptions::getRepeat),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("interval", TICK_INTERVAL_DEFAULT).forGetter(ArcEmitterOptions::getTickInterval),
                Codec.INT.optionalFieldOf("initial_x_angle").forGetter(ArcEmitterOptions::getInitialAngleXDeg),
                Codec.INT.optionalFieldOf("initial_y_angle").forGetter(ArcEmitterOptions::getInitialAngleYDeg),
                EmitterRuleSetManager.INLINE_OR_REFERENCE_CODEC.fieldOf("emitter").forGetter(ArcEmitterOptions::getEmitterRuleSet)
            ).apply(
                instance,
                (
                    Integer length,
                    Integer splits,
                    Integer angleVariance,
                    Integer repeat,
                    Integer tickInterval,
                    Optional<Integer> initialAngleXDeg,
                    Optional<Integer> initialAngleYDeg,
                    EmitterRuleSet.Reference emitterRuleSet
                ) -> new ArcEmitterOptions(
                    type,
                    length,
                    splits,
                    angleVariance,
                    repeat,
                    tickInterval,
                    initialAngleXDeg.orElse(null),
                    initialAngleYDeg.orElse(null),
                    emitterRuleSet
                )
            )
        );
    }

    public static MapCodec<ArcEmitterOptions> codec(PIParticleType<ArcEmitterOptions> type) {
        return createCodec(type).fieldOf("emitter_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, ArcEmitterOptions> streamCodec(PIParticleType<ArcEmitterOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull PIParticleType<ArcEmitterOptions> type() {
        return this.type;
    }

    public static String idPrefix() {
        return "arc_emitter";
    }

    public int getLength() {
        return this.length;
    }

    public int getSplits() {
        return this.splits;
    }

    public int getAngleVariance() {
        return this.angleVariance;
    }

    public int getTickInterval() {
        return this.tickInterval;
    }

    public int getRepeat() {
        return this.repeat;
    }

    public Optional<Integer> getInitialAngleXDeg() {
        return Optional.ofNullable(this.initialAngleXDeg);
    }

    public Optional<Integer> getInitialAngleYDeg() {
        return Optional.ofNullable(this.initialAngleYDeg);
    }

    public EmitterRuleSet.Reference getEmitterRuleSet() {
        return this.emitterRuleSet;
    }
}
