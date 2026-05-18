package games.enchanted.eg_particle_interactions.common.predicates.fluid;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.list.FluidList;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.list.FluidListManager;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import net.minecraft.world.level.material.FluidState;

public class FluidListPredicate extends FluidPredicate {
    public static final MapCodec<FluidListPredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            FluidListManager.INLINE_OR_ID_CODEC.fieldOf("fluids").forGetter(FluidListPredicate::getFluidList)
        ).apply(
            i,
            FluidListPredicate::new
        )
    );

    final FluidList.Reference fluidList;

    public FluidListPredicate(FluidList.Reference fluidList) {
        this.fluidList = fluidList;
    }

    protected FluidList.Reference getFluidList() {
        return this.fluidList;
    }

    @Override
    public MapCodec<? extends FluidPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(FluidState state) {
        return ObjectOrTagLocation.doesListContainFluid(fluidList.get().blocksAndTags(), state) ||
            fluidList.get().statePredicates().stream().anyMatch(p -> p.matches(state));
    }
}
