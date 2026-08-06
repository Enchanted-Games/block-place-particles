package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public class AgeBasedLightEmission extends AgeBasedFieldSet {
    public static MapCodec<AgeBasedLightEmission> CODEC = AgeBasedFieldSet.createCodec(AgeBasedLightEmission::new);

    AgeBasedLightEmission(float multiplier, boolean useInitialValue, FloatRange agePercentageRange) {
        super(multiplier, useInitialValue, agePercentageRange);
    }

    @Override
    protected float initialValue(ParticleInteractionsParticle particle) {
        return particle.getInitialAppearanceLightEmission();
    }

    @Override
    protected void setValue(ParticleInteractionsParticle particle, float value) {
        particle.setLightEmission((int) value);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
