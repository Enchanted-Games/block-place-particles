package games.enchanted.eg_particle_interactions.common.predicates.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

//? if <= 26.1 {
/*import net.minecraft.advancements.criterion.StatePropertiesPredicate;
 *///? } else {
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
//? }

public class BlockStatePredicate extends BlockPredicate {
    public static final MapCodec<BlockStatePredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            BuiltInRegistries.BLOCK.holderByNameCodec().optionalFieldOf("block").forGetter(predicate -> Optional.ofNullable(predicate.getBlock())),
            StatePropertiesPredicate.CODEC.optionalFieldOf("properties", new StatePropertiesPredicate(List.of())).forGetter(BlockStatePredicate::getPropertiesPredicate)
        ).apply(
            i,
            (block, propertiesPredicate) -> {
                return new BlockStatePredicate(block.orElse(null), propertiesPredicate);
            }
        )
    );

    final @Nullable Holder<Block> block;
    final StatePropertiesPredicate propertiesPredicate;

    public BlockStatePredicate(@Nullable Holder<Block> block, StatePropertiesPredicate propertiesPredicate) {
        this.block = block;
        this.propertiesPredicate = propertiesPredicate;
    }

    protected @Nullable Holder<Block> getBlock() {
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
        if(this.getBlock() == null) {
            return this.propertiesPredicate.matches(state);
        }
        return this.propertiesPredicate.matches(state) && state.is(this.getBlock());
    }
}
