package games.enchanted.eg_particle_interactions.common.predicates.fluid;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectPredicate;
import net.minecraft.world.level.material.FluidState;

public abstract class FluidPredicate implements ObjectPredicate<FluidState> {
    public abstract MapCodec<? extends FluidPredicate> codec();
}