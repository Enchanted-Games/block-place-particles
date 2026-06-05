package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.FloatRange;

public class SetScaleBasedOnAge extends EventAction {
    public static final MapCodec<SetScaleBasedOnAge> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.FLOAT.optionalFieldOf("multiplier", 1f).forGetter(SetScaleBasedOnAge::getMultiplier),
            Codec.BOOL.optionalFieldOf("use_initial_value", true).forGetter(SetScaleBasedOnAge::useInitialValue),
            FloatRange.CODEC.fieldOf("lifetime_percentage_range").forGetter(SetScaleBasedOnAge::getAgePercentageRange)
        ).apply(
            i,
            SetScaleBasedOnAge::new
        )
    );

    final float multiplier;
    final boolean useInitialValue;
    final FloatRange agePercentageRange;

    SetScaleBasedOnAge(float multiplier, boolean useInitialValue, FloatRange agePercentageRange) {
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
            percentageAlongRange *= particle.getInitialAppearanceScale();
        }
        particle.setScale(percentageAlongRange * this.multiplier, true);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return null;
    }
}
