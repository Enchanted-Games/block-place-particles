package games.enchanted.eg_particle_interactions.common.particle.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RandomFloatProvider implements ValueProvider<Float> {
    private static final Codec<RandomFloatProvider> RANGE_OR_VALUES_CODEC = RecordCodecBuilder.create(
        i -> i.group(
            FloatRange.CODEC.optionalFieldOf("range").forGetter(
                provider -> Optional.ofNullable(provider.getRange())
            ),
            Codec.FLOAT.listOf(1, 1024).optionalFieldOf("values").forGetter(provider -> {
                if(provider.getFloats().length == 0) return Optional.empty();
                return Optional.of(Arrays.stream(provider.getFloats()).toList());
            })
        ).apply(
            i,
            (floatRange, floats) -> {
                if(floatRange.isEmpty() && floats.isEmpty()) {
                    throw new IllegalArgumentException("Float provider must have `range` or `values` fields, or be a single float");
                }
                return new RandomFloatProvider(floatRange.orElse(null), floats.orElse(List.of()));
            }
        )
    );

    public static Codec<RandomFloatProvider> CODEC = RANGE_OR_VALUES_CODEC.withAlternative(
        Codec.FLOAT.xmap(
            value -> new RandomFloatProvider(null, List.of(value)),
            provider -> provider.getFloats()[0]
        )
    );

    @Nullable final FloatRange range;
    final Float[] floats;

    public RandomFloatProvider(@Nullable FloatRange range, List<Float> floats) {
        this.range = range;
        this.floats = floats.toArray(new Float[0]);
    }

    public RandomFloatProvider(float min, float max) {
        this(new FloatRange(min, max), List.of());
    }

    public RandomFloatProvider(List<Float> values) {
        this(null, values);
    }

    @Override
    public Float getValue(ParticleContext context) {
        if(this.floats.length > 0) {
            return this.floats[context.level().getRandom().nextIntBetweenInclusive(0, this.floats.length - 1)];
        }
        if(this.range == null) {
            throw new IllegalStateException("Somehow got a float provider with no values or range");
        }
        final float min = this.range.min();
        final float max = this.range.max();
        if(min == max) return max;
        return (context.level().getRandom().nextFloat() * (max - min)) + min;
    }

    protected @Nullable FloatRange getRange() {
        return this.range;
    }

    protected Float[] getFloats() {
        return this.floats;
    }
}
