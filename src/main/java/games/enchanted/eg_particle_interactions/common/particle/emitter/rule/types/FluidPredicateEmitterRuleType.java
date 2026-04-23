package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicates;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import net.minecraft.core.Vec3i;

public class FluidPredicateEmitterRuleType extends EmitterRuleType {
    public static final MapCodec<FluidPredicateEmitterRuleType> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Emitters.CODEC.fieldOf(EmitterRuleType.EMITTER_FIELD).forGetter(EmitterRuleType::getEmitter),
            Vec3i.CODEC.optionalFieldOf(EmitterRuleType.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(FluidPredicateEmitterRuleType::getPosOffset),
            FluidPredicates.CODEC.fieldOf(EmitterRuleType.PREDICATE_FIELD).forGetter(FluidPredicateEmitterRuleType::getFluidPredicate)
        ).apply(
            i,
            FluidPredicateEmitterRuleType::new
        )
    );

    final Vec3i posOffset;
    final FluidPredicate fluidPredicate;

    public FluidPredicateEmitterRuleType(Emitter emitter, Vec3i posOffset, FluidPredicate fluidPredicate) {
        super(emitter);
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
    public MapCodec<? extends FluidPredicateEmitterRuleType> codec() {
        return CODEC;
    }
}