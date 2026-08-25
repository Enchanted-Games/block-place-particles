package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.variable.ParticleVariable;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.ParticleField;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public abstract class AgeBasedFieldSet<T> extends EventAction {
    final ParticleVariable<T> scaleFrom;
    final ParticleVariable<T> scaleTo;
    final FloatRange agePercentageRange;
    final ParticleField<T> field;

    AgeBasedFieldSet(ParticleVariable<T> scaleFrom, ParticleVariable<T> scaleTo, FloatRange agePercentageRange, ParticleField<T> field) {
        this.scaleFrom = scaleFrom;
        this.scaleTo = scaleTo;
        this.agePercentageRange = agePercentageRange;
        this.field = field;
    }

    protected ParticleVariable<T> getScaleFrom() {
        return this.scaleFrom;
    }

    protected ParticleVariable<T> getScaleTo() {
        return this.scaleTo;
    }

    protected FloatRange getAgePercentageRange() {
        return this.agePercentageRange;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        if(!this.agePercentageRange.inRange(particle.getAgePercent())) return;
        this.field.set(particle, this.remapValue(particle));
    }

    protected abstract T remapValue(ParticleInteractionsParticle particle);

    public static <T, O extends AgeBasedFieldSet<T>> MapCodec<O> createCodec(ParticleField<T> field, Codec<FieldModifier<T>> fieldModifierCodec, Function4<ParticleVariable<T>, ParticleVariable<T>, FloatRange, ParticleField<T>, O> ctor) {
        return RecordCodecBuilder.mapCodec(i -> i
            .group(
                ParticleVariable.createCodec(field, fieldModifierCodec).fieldOf("remap_min").forGetter(AgeBasedFieldSet::getScaleFrom),
                ParticleVariable.createCodec(field, fieldModifierCodec).fieldOf("remap_max").forGetter(AgeBasedFieldSet::getScaleTo),
                FloatRange.CODEC.fieldOf("lifetime_percentage_range").forGetter(AgeBasedFieldSet::getAgePercentageRange)
            ).apply(
                i,
                (remapMin, remapMax, lifetimePercentageRange) -> {
                    return ctor.apply(remapMin, remapMax, lifetimePercentageRange, field);
                }
            )
        );
    }
}
