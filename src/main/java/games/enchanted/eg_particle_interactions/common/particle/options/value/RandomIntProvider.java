package games.enchanted.eg_particle_interactions.common.particle.options.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

import java.util.Arrays;
import java.util.List;

public class RandomIntProvider implements ValueProvider<Integer> {
    private static final Codec<RandomIntProvider> MIN_MAX_CODEC = RecordCodecBuilder.create(
        i -> i.group(
            Codec.INT.fieldOf("min").forGetter(RandomIntProvider::getMin),
            Codec.INT.fieldOf("max").forGetter(RandomIntProvider::getMax)
        ).apply(
            i,
            RandomIntProvider::new
        )
    );

    private static final Codec<RandomIntProvider> LIST_CODEC = Codec.INT.listOf().xmap(
        RandomIntProvider::new,
        provider -> {
            if(provider.getInts().length == 0) {
                throw new IllegalArgumentException("Cannot serialize min-max int provider as list provider");
            }
            return Arrays.asList(provider.getInts());
        }
    );

    public static Codec<RandomIntProvider> CODEC = MIN_MAX_CODEC.withAlternative(
        Codec.INT.xmap(
            value -> new RandomIntProvider(value, value),
            RandomIntProvider::getMax
        )
    ).withAlternative(LIST_CODEC);

    final int min;
    final int max;
    final Integer[] ints;

    public RandomIntProvider(int min, int max) {
        this.min = min;
        this.max = max;
        this.ints = new Integer[0];
    }

    public RandomIntProvider(List<Integer> ints) {
        this.min = 0;
        this.max = 0;
        this.ints = ints.toArray(new Integer[0]);
    }

    @Override
    public Integer getValue(ParticleContext context) {
        if(this.ints.length > 0) {
            return this.ints[context.level().getRandom().nextIntBetweenInclusive(0, this.ints.length - 1)];
        }
        if(this.min == this.max) return this.max;
        return Math.round((context.level().getRandom().nextFloat() * (this.max - this.min)) + this.min);
    }

    protected int getMax() {
        return max;
    }

    protected int getMin() {
        return min;
    }

    protected Integer[] getInts() {
        return this.ints;
    }
}
