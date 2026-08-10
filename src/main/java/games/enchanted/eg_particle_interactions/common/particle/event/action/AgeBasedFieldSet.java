package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public abstract class AgeBasedFieldSet extends EventAction {
    final float multiplier;
    final boolean useInitialValue;
    final FloatRange agePercentageRange;

    AgeBasedFieldSet(float multiplier, boolean useInitialValue, FloatRange agePercentageRange) {
        this.multiplier = multiplier;
        this.useInitialValue = useInitialValue;
        this.agePercentageRange = agePercentageRange;
    }

    protected float getMultiplier() {
        return this.multiplier;
    }

    protected boolean useInitialValue() {
        return this.useInitialValue;
    }

    protected FloatRange getAgePercentageRange() {
        return this.agePercentageRange;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        if(!this.agePercentageRange.inRange(particle.getAgePercent())) return;

        float percentageAlongRange = 1 - this.agePercentageRange.remapValueToPercentageAlongRange(particle.getAgePercent());
        if(this.useInitialValue) {
            percentageAlongRange *= this.initialValue(particle);
        }
        this.setValue(particle, percentageAlongRange * this.multiplier);
    }

    protected abstract float initialValue(ParticleInteractionsParticle particle);

    protected abstract void setValue(ParticleInteractionsParticle particle, float value);

    public static <T extends AgeBasedFieldSet> MapCodec<T> createCodec(Function3<Float, Boolean, FloatRange, T> ctor) {
        return RecordCodecBuilder.mapCodec(i -> i
            .group(
                Codec.FLOAT.optionalFieldOf("multiplier", 1f).forGetter(AgeBasedFieldSet::getMultiplier),
                Codec.BOOL.optionalFieldOf("use_initial_value", true).forGetter(AgeBasedFieldSet::useInitialValue),
                FloatRange.CODEC.fieldOf("lifetime_percentage_range").forGetter(AgeBasedFieldSet::getAgePercentageRange)
            ).apply(
                i,
                ctor
            )
        );
    }
}
