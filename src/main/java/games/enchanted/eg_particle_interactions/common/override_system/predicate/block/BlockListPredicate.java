package games.enchanted.eg_particle_interactions.common.override_system.predicate.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.override_system.override.block.BlockList;
import games.enchanted.eg_particle_interactions.common.override_system.override.block.BlockListManager;
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

    final BlockList blockList;

    public BlockListPredicate(BlockList blockList) {
        this.blockList = blockList;
    }

    protected BlockList getBlockList() {
        return this.blockList;
    }

    @Override
    public MapCodec<? extends BlockPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(BlockState state) {
        return BlockOrTagLocation.doesListContainBlock(blockList.blocksAndTags(), state);
    }
}
