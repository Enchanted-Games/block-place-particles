package games.enchanted.eg_particle_interactions.common.predicates.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockStatePredicate extends BlockPredicate {
    public static final MapCodec<BlockStatePredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter(BlockStatePredicate::getBlock),
            StatePropertiesPredicate.CODEC.optionalFieldOf("properties", new StatePropertiesPredicate(List.of())).forGetter(BlockStatePredicate::getPropertiesPredicate)
        ).apply(
            i,
            BlockStatePredicate::new
        )
    );

    final Holder<Block> block;
    final StatePropertiesPredicate propertiesPredicate;

    public BlockStatePredicate(Holder<Block> block, StatePropertiesPredicate propertiesPredicate) {
        this.block = block;
        this.propertiesPredicate = propertiesPredicate;
    }

    protected Holder<Block> getBlock() {
        return this.block;
    }

    protected StatePropertiesPredicate getPropertiesPredicate() {
        return this.propertiesPredicate;
    }

    @Override
    public MapCodec<? extends BlockPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(BlockState state) {
        return this.propertiesPredicate.matches(state) && state.is(this.getBlock());
    }
}
