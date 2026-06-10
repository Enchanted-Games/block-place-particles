package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicates;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ExitedFluidEventType extends ParticleEventType {
    public static final MapCodec<ExitedFluidEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            FluidPredicates.CODEC.optionalFieldOf("fluid_predicate").forGetter(inFluidEventType -> Optional.ofNullable(inFluidEventType.getFluidPredicate()))
        ).apply(
            i,
            (predicateOptional) -> new ExitedFluidEventType(predicateOptional.orElse(null))
        )
    );

    final @Nullable FluidPredicate fluidPredicate;

    ExitedFluidEventType(@Nullable FluidPredicate fluidPredicate) {
        this.fluidPredicate = fluidPredicate;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        if(fluidPredicate == null) {
            this.particleTickNoPredicate(particle);
        } else {
            this.particleTickPredicate(particle);
        }
    }

    protected void particleTickNoPredicate(ParticleInteractionsParticle particle) {
        if(particle.getInFluid().is(Fluids.EMPTY) && !particle.getInFluid().is(particle.getInFluidLastTick().getType())) {
            this.fire(particle);
        }
    }

    protected void particleTickPredicate(ParticleInteractionsParticle particle) {
        if(this.fluidPredicate == null) return;
        if(!particle.getInFluid().is(Fluids.EMPTY)) return;

        boolean matchesFluid = this.fluidPredicate.matches(particle.getInFluid());
        boolean matchesLastFluid = this.fluidPredicate.matches(particle.getInFluidLastTick());

        if(matchesFluid != matchesLastFluid) {
            this.fire(particle);
        }
    }

    protected @Nullable FluidPredicate getFluidPredicate() {
        return this.fluidPredicate;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
