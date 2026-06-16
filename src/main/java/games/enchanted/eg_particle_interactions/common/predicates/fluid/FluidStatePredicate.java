package games.enchanted.eg_particle_interactions.common.predicates.fluid;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

//? if <= 26.1 {
/*import net.minecraft.advancements.criterion.StatePropertiesPredicate;
*///? } else {
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
//? }

public class FluidStatePredicate extends FluidPredicate {
    public static final MapCodec<FluidStatePredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            BuiltInRegistries.FLUID.holderByNameCodec().optionalFieldOf("block").forGetter(predicate -> Optional.ofNullable(predicate.getFluid())),
            StatePropertiesPredicate.CODEC.optionalFieldOf("properties", new StatePropertiesPredicate(List.of())).forGetter(FluidStatePredicate::getPropertiesPredicate)
        ).apply(
            i,
            (fluid, propertiesPredicate) -> {
                return new FluidStatePredicate(fluid.orElse(null), propertiesPredicate);
            }
        )
    );

    final @Nullable Holder<Fluid> fluid;
    final StatePropertiesPredicate propertiesPredicate;

    public FluidStatePredicate(@Nullable Holder<Fluid> fluid, StatePropertiesPredicate propertiesPredicate) {
        this.fluid = fluid;
        this.propertiesPredicate = propertiesPredicate;
    }

    protected @Nullable Holder<Fluid> getFluid() {
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
        if(this.getFluid() == null) {
            return this.propertiesPredicate.matches(state);
        }
        return this.propertiesPredicate.matches(state) && state.is(this.getFluid());
    }
}
