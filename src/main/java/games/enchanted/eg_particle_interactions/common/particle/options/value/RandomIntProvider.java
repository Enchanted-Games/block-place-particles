package games.enchanted.eg_particle_interactions.common.particle.options.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

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
    public static Codec<RandomIntProvider> CODEC = MIN_MAX_CODEC.withAlternative(Codec.INT.xmap(
        value -> new RandomIntProvider(value, value),
        RandomIntProvider::getMax
    ));

    final int min;
    final int max;

    public RandomIntProvider(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer getValue(ParticleContext context) {
        if(this.min == this.max) return this.max;
        return Math.round((context.level().getRandom().nextFloat() * (this.max - this.min)) + this.min);
    }

    protected int getMax() {
        return max;
    }

    protected int getMin() {
        return min;
    }
}
