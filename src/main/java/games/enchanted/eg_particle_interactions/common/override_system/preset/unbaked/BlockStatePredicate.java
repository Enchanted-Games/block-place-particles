package games.enchanted.eg_particle_interactions.common.override_system.preset.unbaked;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import games.enchanted.eg_particle_interactions.common.registry.TagUtil;
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
        BlockOrTagLocation.CODEC.comapFlatMap(
            identifier -> DataResult.success(new BlockStatePredicate(identifier)),
            predicate -> Objects.requireNonNull(predicate.blockOrTagId, "Predicate could not be serialized as a block or tag id")
        )
    );

    @Nullable
    private final BlockOrTagLocation blockOrTagId;

    public BlockStatePredicate(BlockOrTagLocation blockOrTagId) {
        this.blockOrTagId = blockOrTagId;
    }

    public BlockStatePredicate() {
        this.blockOrTagId = null;
    }

    @Override
    public boolean matches(BlockState state) {
        if(this.blockOrTagId != null) {
            return TagUtil.doesListContainBlock(List.of(this.blockOrTagId), RegistryHelpers.getLocationFromBlock(state.getBlock()));
        }
        return false;
    }
}
