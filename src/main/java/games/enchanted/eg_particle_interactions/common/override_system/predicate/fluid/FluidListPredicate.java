package games.enchanted.eg_particle_interactions.common.override_system.predicate.fluid;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.fluid.list.FluidList;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.fluid.list.FluidListManager;
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

    final FluidList fluidList;

    public FluidListPredicate(FluidList fluidList) {
        this.fluidList = fluidList;
    }

    protected FluidList getFluidList() {
        return this.fluidList;
    }

    @Override
    public MapCodec<? extends FluidPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(FluidState state) {
        return ObjectOrTagLocation.doesListContainBlock(fluidList.blocksAndTags(), state.createLegacyBlock()) ||
            fluidList.statePredicates().stream().anyMatch(p -> p.matches(state));
    }
}
