package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.variable.ParticleVariable;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.ParticleField;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.ParticleFields;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberFloat.FloatFieldModifiers;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public class AgeBasedAlpha extends AgeBasedFieldSet<Float> {
    public static final MapCodec<AgeBasedAlpha> CODEC = AgeBasedFieldSet.createCodec(ParticleFields.ALPHA, FloatFieldModifiers.CODEC, AgeBasedAlpha::new);

    AgeBasedAlpha(ParticleVariable<Float> scaleFrom, ParticleVariable<Float> scaleTo, FloatRange agePercentageRange, ParticleField<Float> field) {
        super(scaleFrom, scaleTo, agePercentageRange, field);
    }

    @Override
    protected Float remapValue(ParticleInteractionsParticle particle) {
        float percentageAlongRange = this.agePercentageRange.remapValueToPercentageAlongRange(particle.getAgePercent());
        FloatRange scaleRange = new FloatRange(this.scaleFrom.getInitial(particle), this.scaleTo.getInitial(particle));

        return scaleRange.remapPercentageIntoRange(percentageAlongRange);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
