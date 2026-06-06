package games.enchanted.eg_particle_interactions.common.particle.options.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.math.range.IntRange;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RandomIntProvider implements ValueProvider<Integer> {
    private static final Codec<RandomIntProvider> RANGE_OR_VALUES_CODEC = RecordCodecBuilder.create(
        i -> i.group(
            IntRange.CODEC.optionalFieldOf("range").forGetter(
                provider -> Optional.ofNullable(provider.getRange())
            ),
            Codec.INT.listOf(1, 1024).optionalFieldOf("values").forGetter(provider -> {
                if(provider.getInts().length == 0) return Optional.empty();
                return Optional.of(Arrays.stream(provider.getInts()).toList());
            })
        ).apply(
            i,
            (intRange, ints) -> {
                if(intRange.isEmpty() && ints.isEmpty()) {
                    throw new IllegalArgumentException("Integer provider must have `range` or `values` fields, or be a single integer");
                }
                return new RandomIntProvider(intRange.orElse(null), ints.orElse(List.of()));
            }
        )
    );

    public static Codec<RandomIntProvider> CODEC = RANGE_OR_VALUES_CODEC.withAlternative(
        Codec.INT.xmap(
            value -> new RandomIntProvider(null, List.of(value)),
            provider -> provider.getInts()[0]
        )
    );

    @Nullable
    final IntRange range;
    final Integer[] ints;

    public RandomIntProvider(@Nullable IntRange range, List<Integer> ints) {
        this.range = range;
        this.ints = ints.toArray(new Integer[0]);
    }

    public RandomIntProvider(int min, int max) {
        this(new IntRange(min, max), List.of());
    }

    public RandomIntProvider(List<Integer> values) {
        this(null, values);
    }

    @Override
    public Integer getValue(ParticleContext context) {
        if(this.ints.length > 0) {
            return this.ints[context.level().getRandom().nextIntBetweenInclusive(0, this.ints.length - 1)];
        }
        if(this.range == null) {
            throw new IllegalStateException("Somehow got an integer provider with no values or range");
        }
        final int min = this.range.min();
        final int max = this.range.max();
        if(min == max) return max;
        return Math.round((context.level().getRandom().nextFloat() * (max - min)) + min);
    }

    protected @Nullable IntRange getRange() {
        return this.range;
    }

    protected Integer[] getInts() {
        return this.ints;
    }
}
