package games.enchanted.eg_particle_interactions.common.override_system.predicate.block;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.ObjectPredicate;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockPredicate implements ObjectPredicate<BlockState> {
    public abstract MapCodec<? extends BlockPredicate> codec();
}
