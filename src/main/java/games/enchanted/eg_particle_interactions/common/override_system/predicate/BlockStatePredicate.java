package games.enchanted.eg_particle_interactions.common.override_system.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import games.enchanted.eg_particle_interactions.common.registry.TagUtil;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class BlockStatePredicate implements ObjectPredicate<BlockState> {
    public static Codec<ObjectPredicate<BlockState>> CODEC = Codec.withAlternative(
        RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.INT.fieldOf("placeholder").forGetter(o -> 1)
            ).apply(
                instance,
                placeholder -> new BlockStatePredicate()
            )
        ),
        ExtraCodecs.compactListCodec(BlockOrTagLocation.CODEC).comapFlatMap(
            identifier -> DataResult.success(new BlockStatePredicate(identifier)),
            predicate -> Objects.requireNonNull(predicate.blockOrTagIds, "Predicate could not be serialized as a block or tag id")
        )
    );

    @Nullable
    private final List<BlockOrTagLocation> blockOrTagIds;

    public BlockStatePredicate(List<BlockOrTagLocation> blockOrTagIds) {
        this.blockOrTagIds = blockOrTagIds;
    }

    public BlockStatePredicate() {
        this.blockOrTagIds = null;
    }

    @Override
    public boolean matches(BlockState state) {
        if(this.blockOrTagIds != null) {
            return TagUtil.doesListContainBlock(this.blockOrTagIds, RegistryHelpers.getLocationFromBlock(state.getBlock()));
        }
        return false;
    }
}
