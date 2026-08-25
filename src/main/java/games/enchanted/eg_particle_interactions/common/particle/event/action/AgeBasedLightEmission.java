package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.variable.ParticleVariable;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.ParticleField;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.ParticleFields;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberInt.IntFieldModifiers;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;
import games.enchanted.eg_particle_interactions.common.util.math.range.IntRange;

public class AgeBasedLightEmission extends AgeBasedFieldSet<Integer> {
    public static final MapCodec<AgeBasedLightEmission> CODEC = AgeBasedFieldSet.createCodec(ParticleFields.LIGHT_EMISSION, IntFieldModifiers.CODEC, AgeBasedLightEmission::new);

    AgeBasedLightEmission(ParticleVariable<Integer> scaleFrom, ParticleVariable<Integer> scaleTo, FloatRange agePercentageRange, ParticleField<Integer> field) {
        super(scaleFrom, scaleTo, agePercentageRange, field);
    }

    @Override
    protected Integer remapValue(ParticleInteractionsParticle particle) {
        float percentageAlongRange = this.agePercentageRange.remapValueToPercentageAlongRange(particle.getAgePercent());
        IntRange scaleRange = new IntRange(this.scaleFrom.getInitial(particle), this.scaleTo.getInitial(particle));

        return scaleRange.remapPercentageIntoRange(percentageAlongRange);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
