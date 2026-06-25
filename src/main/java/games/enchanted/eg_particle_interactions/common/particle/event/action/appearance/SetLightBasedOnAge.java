package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public class SetLightBasedOnAge extends EventAction {
    public static final MapCodec<SetLightBasedOnAge> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.FLOAT.optionalFieldOf("multiplier", 1f).forGetter(SetLightBasedOnAge::getMultiplier),
            Codec.BOOL.optionalFieldOf("use_initial_value", true).forGetter(SetLightBasedOnAge::useInitialValue),
            FloatRange.CODEC.fieldOf("lifetime_percentage_range").forGetter(SetLightBasedOnAge::getAgePercentageRange)
        ).apply(
            i,
            SetLightBasedOnAge::new
        )
    );

    final float multiplier;
    final boolean useInitialValue;
    final FloatRange agePercentageRange;

    SetLightBasedOnAge(float multiplier, boolean useInitialValue, FloatRange agePercentageRange) {
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
            percentageAlongRange *= particle.getInitialAppearanceLightEmission();
        }
        particle.setLightEmission((int) (percentageAlongRange * this.multiplier));
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return null;
    }
}
