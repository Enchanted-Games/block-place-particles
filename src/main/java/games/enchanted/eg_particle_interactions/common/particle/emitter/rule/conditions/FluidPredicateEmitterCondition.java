package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicates;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import net.minecraft.core.Vec3i;

public class FluidPredicateEmitterCondition extends EmitterCondition {
    public static final MapCodec<FluidPredicateEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Vec3i.CODEC.optionalFieldOf(EmitterCondition.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(FluidPredicateEmitterCondition::getPosOffset),
            FluidPredicates.CODEC.fieldOf(EmitterCondition.PREDICATE_FIELD).forGetter(FluidPredicateEmitterCondition::getFluidPredicate)
        ).apply(
            i,
            FluidPredicateEmitterCondition::new
        )
    );

    final Vec3i posOffset;
    final FluidPredicate fluidPredicate;

    public FluidPredicateEmitterCondition(Vec3i posOffset, FluidPredicate fluidPredicate) {
        this.posOffset = posOffset;
        this.fluidPredicate = fluidPredicate;
    }

    protected Vec3i getPosOffset() {
        return this.posOffset;
    }

    protected FluidPredicate getFluidPredicate() {
        return this.fluidPredicate;
    }

    @Override
    public boolean matches(ParticleContext context) {
        if(context.fluidContext() == null) return false;
        if(this.posOffset.toMutable().equals(0, 0, 0)) {
            return this.fluidPredicate.matches(context.fluidContext().state());
        }
        return this.fluidPredicate.matches(context.level().getFluidState(
            context.pos().offset(this.posOffset)
        ));
    }

    @Override
    public MapCodec<? extends FluidPredicateEmitterCondition> codec() {
        return CODEC;
    }
}