package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicates;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class InFluidEventType extends ParticleEventType {
    public static final MapCodec<InFluidEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.BOOL.optionalFieldOf("oneshot", true).forGetter(InFluidEventType::isOneShot),
            FluidPredicates.CODEC.optionalFieldOf("fluid_predicate").forGetter(inFluidEventType -> Optional.ofNullable(inFluidEventType.getFluidPredicate()))
        ).apply(
            i,
            (oneshot, predicateOptional) -> new InFluidEventType(oneshot, predicateOptional.orElse(null))
        )
    );

    final boolean oneshot;
    final @Nullable FluidPredicate fluidPredicate;

    InFluidEventType(boolean oneshot, @Nullable FluidPredicate fluidPredicate) {
        this.oneshot = oneshot;
        this.fluidPredicate = fluidPredicate;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
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

    protected @Nullable FluidPredicate getFluidPredicate() {
        return this.fluidPredicate;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
