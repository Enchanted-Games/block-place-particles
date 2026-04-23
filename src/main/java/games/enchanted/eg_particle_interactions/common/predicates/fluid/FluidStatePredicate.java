package games.enchanted.eg_particle_interactions.common.predicates.fluid;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.List;

public class FluidStatePredicate extends FluidPredicate {
    public static final MapCodec<FluidStatePredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("block").forGetter(FluidStatePredicate::getFluid),
            StatePropertiesPredicate.CODEC.optionalFieldOf("properties", new StatePropertiesPredicate(List.of())).forGetter(FluidStatePredicate::getPropertiesPredicate)
        ).apply(
            i,
            FluidStatePredicate::new
        )
    );

    final Holder<Fluid> fluid;
    final StatePropertiesPredicate propertiesPredicate;

    public FluidStatePredicate(Holder<Fluid> fluid, StatePropertiesPredicate propertiesPredicate) {
        this.fluid = fluid;
        this.propertiesPredicate = propertiesPredicate;
    }

    protected Holder<Fluid> getFluid() {
        return this.fluid;
    }

    protected StatePropertiesPredicate getPropertiesPredicate() {
        return this.propertiesPredicate;
    }

    @Override
    public MapCodec<? extends FluidPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(FluidState state) {
        return this.propertiesPredicate.matches(state) && state.is(this.getFluid());
    }
}
