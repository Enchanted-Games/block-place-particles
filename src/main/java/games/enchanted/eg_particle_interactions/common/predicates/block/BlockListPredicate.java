package games.enchanted.eg_particle_interactions.common.predicates.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.block.list.BlockList;
import games.enchanted.eg_particle_interactions.common.predicates.block.list.BlockListManager;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BlockListPredicate extends BlockPredicate {
    public static final MapCodec<BlockListPredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            BlockListManager.INLINE_OR_ID_CODEC.fieldOf("blocks").forGetter(BlockListPredicate::getBlockList)
        ).apply(
            i,
            BlockListPredicate::new
        )
    );

    final BlockList.Reference blockList;

    public BlockListPredicate(BlockList.Reference blockList) {
        this.blockList = blockList;
    }

    protected BlockList.Reference getBlockList() {
        return this.blockList;
    }

    @Override
    public MapCodec<? extends BlockPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(BlockState state) {
        return ObjectOrTagLocation.doesListContainBlock(blockList.get().blocksAndTags(), state) ||
            blockList.get().statePredicates().stream().anyMatch(p -> p.matches(state));
    }
}
