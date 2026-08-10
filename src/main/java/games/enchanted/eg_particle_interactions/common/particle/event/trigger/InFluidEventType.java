package games.enchanted.eg_particle_interactions.common.particle.event.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicates;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class InFluidEventType extends ParticleEventType {
    public static final MapCodec<InFluidEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.BOOL.optionalFieldOf("oneshot", true).forGetter(InFluidEventType::isOneShot),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("minimum_particle_age", 0).forGetter(InFluidEventType::minAge),
            FluidPredicates.CODEC.optionalFieldOf("fluid_predicate").forGetter(inFluidEventType -> Optional.ofNullable(inFluidEventType.getFluidPredicate()))
        ).apply(
            i,
            (oneshot, tickDelay, predicateOptional) -> new InFluidEventType(oneshot, tickDelay, predicateOptional.orElse(null))
        )
    );

    final boolean oneshot;
    final int minAge;
    final @Nullable FluidPredicate fluidPredicate;

    InFluidEventType(boolean oneshot, int minAge, @Nullable FluidPredicate fluidPredicate) {
        this.oneshot = oneshot;
        this.minAge = minAge;
        this.fluidPredicate = fluidPredicate;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        if(particle.getAge() < this.minAge) return;
        if(fluidPredicate == null && !particle.getInFluid().is(Fluids.EMPTY)) {
            this.particleTickNoPredicate(particle);
        } else {
            this.particleTickPredicate(particle);
        }
    }

    protected void particleTickNoPredicate(ParticleInteractionsParticle particle) {
        if(this.oneshot && !particle.getInFluid().is(particle.getInFluidLastTick().getType())) {
            this.fire(particle);
        } else if(!this.oneshot) {
            this.fire(particle);
        }
    }

    protected void particleTickPredicate(ParticleInteractionsParticle particle) {
        if(this.fluidPredicate == null) return;
        boolean matchesFluid = this.fluidPredicate.matches(particle.getInFluid());
        boolean matchesLastFluid = this.fluidPredicate.matches(particle.getInFluidLastTick());

        if(this.oneshot && matchesFluid != matchesLastFluid) {
            this.fire(particle);
        } else if(!this.oneshot && matchesFluid) {
            this.fire(particle);
        }
    }

    protected boolean isOneShot() {
        return this.oneshot;
    }

    protected int minAge() {
        return this.minAge;
    }

    protected @Nullable FluidPredicate getFluidPredicate() {
        return this.fluidPredicate;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
