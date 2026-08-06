package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public class AgeBasedAlpha extends AgeBasedFieldSet {
    public static MapCodec<AgeBasedAlpha> CODEC = AgeBasedFieldSet.createCodec(AgeBasedAlpha::new);

    AgeBasedAlpha(float multiplier, boolean useInitialValue, FloatRange agePercentageRange) {
        super(multiplier, useInitialValue, agePercentageRange);
    }

    @Override
    protected float initialValue(ParticleInteractionsParticle particle) {
        return particle.getInitialAppearanceAlpha();
    }

    @Override
    protected void setValue(ParticleInteractionsParticle particle, float value) {
        particle.setAlpha(value, true);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
