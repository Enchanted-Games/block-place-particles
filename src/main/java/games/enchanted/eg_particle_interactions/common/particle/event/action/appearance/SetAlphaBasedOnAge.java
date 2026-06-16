package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public class SetAlphaBasedOnAge extends EventAction {
    public static final MapCodec<SetAlphaBasedOnAge> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.FLOAT.optionalFieldOf("multiplier", 1f).forGetter(SetAlphaBasedOnAge::getMultiplier),
            Codec.BOOL.optionalFieldOf("use_initial_value", true).forGetter(SetAlphaBasedOnAge::useInitialValue),
            FloatRange.CODEC.fieldOf("lifetime_percentage_range").forGetter(SetAlphaBasedOnAge::getAgePercentageRange)
        ).apply(
            i,
            SetAlphaBasedOnAge::new
        )
    );

    final float multiplier;
    final boolean useInitialValue;
    final FloatRange agePercentageRange;

    SetAlphaBasedOnAge(float multiplier, boolean useInitialValue, FloatRange agePercentageRange) {
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
            percentageAlongRange *= particle.getInitialAppearanceAlpha();
        }
        particle.setAlpha(percentageAlongRange * this.multiplier, false);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return null;
    }
}
