package games.enchanted.eg_particle_interactions.common.particle.options.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public class RandomFloatProvider implements ValueProvider<Float> {
    private static final Codec<RandomFloatProvider> MIN_MAX_CODEC = RecordCodecBuilder.create(
        i -> i.group(
            Codec.FLOAT.fieldOf("min").forGetter(RandomFloatProvider::getMin),
            Codec.FLOAT.fieldOf("max").forGetter(RandomFloatProvider::getMax)
        ).apply(
            i,
            RandomFloatProvider::new
        )
    );
    public static Codec<RandomFloatProvider> CODEC = MIN_MAX_CODEC.withAlternative(Codec.FLOAT.xmap(
        value -> new RandomFloatProvider(value, value),
        RandomFloatProvider::getMax
    ));

    final float min;
    final float max;

    public RandomFloatProvider(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Float getValue(ParticleContext context) {
        if(this.min == this.max) return this.max;
        return (context.level().getRandom().nextFloat() * (this.max - this.min)) + this.min;
    }

    protected float getMax() {
        return max;
    }

    protected float getMin() {
        return min;
    }
}
