package games.enchanted.eg_particle_interactions.common.particle.options.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

import java.util.Arrays;
import java.util.List;

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

    private static final Codec<RandomFloatProvider> LIST_CODEC = Codec.FLOAT.listOf().xmap(
        RandomFloatProvider::new,
        provider -> {
            if(provider.getFloats().length == 0) {
                throw new IllegalArgumentException("Cannot serialize min-max float provider as list provider");
            }
            return Arrays.asList(provider.getFloats());
        }
    );

    public static Codec<RandomFloatProvider> CODEC = MIN_MAX_CODEC.withAlternative(
        Codec.FLOAT.xmap(
            value -> new RandomFloatProvider(value, value),
            RandomFloatProvider::getMax
        )
    ).withAlternative(LIST_CODEC);

    final float min;
    final float max;
    final Float[] floats;

    public RandomFloatProvider(float min, float max) {
        this.min = min;
        this.max = max;
        this.floats = new Float[0];
    }

    public RandomFloatProvider(List<Float> floats) {
        this.min = 0;
        this.max = 0;
        this.floats = floats.toArray(new Float[0]);
    }

    @Override
    public Float getValue(ParticleContext context) {
        if(this.floats.length > 0) {
            return this.floats[context.level().getRandom().nextIntBetweenInclusive(0, this.floats.length - 1)];
        }
        if(this.min == this.max) return this.max;
        return (context.level().getRandom().nextFloat() * (this.max - this.min)) + this.min;
    }

    protected float getMax() {
        return max;
    }

    protected float getMin() {
        return min;
    }

    protected Float[] getFloats() {
        return this.floats;
    }
}
