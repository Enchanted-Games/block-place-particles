package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public class AgeBasedScale extends AgeBasedFieldSet {
    public static MapCodec<AgeBasedScale> CODEC = AgeBasedFieldSet.createCodec(AgeBasedScale::new);

    AgeBasedScale(float multiplier, boolean useInitialValue, FloatRange agePercentageRange) {
        super(multiplier, useInitialValue, agePercentageRange);
    }

    @Override
    protected float initialValue(ParticleInteractionsParticle particle) {
        return particle.getInitialAppearanceScale();
    }

    @Override
    protected void setValue(ParticleInteractionsParticle particle, float value) {
        particle.setScale(value, true);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
