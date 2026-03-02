package games.enchanted.eg_particle_interactions.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Stores a resource location for a block or block tag
 */
public record BlockOrTagLocation(Identifier location, boolean isTag) {
    public static Codec<BlockOrTagLocation> CODEC = Codec.STRING.comapFlatMap(
        string -> {
            Identifier parsedLocation = Identifier.parse(string.replace("#", ""));
            return DataResult.success(new BlockOrTagLocation(parsedLocation, string.startsWith("#")));
        },
        BlockOrTagLocation::toString
    );

    public BlockOrTagLocation(Identifier location) {
        this(location, false);
    }


    /**
     * Checks if a blockstate is present in a list of block and block tags.
     *
     * @param blocksAndTags  {@link BlockOrTagLocation} list
     * @param state          block to test if present the list
     */
    public static boolean doesListContainBlock(@NonNull List<BlockOrTagLocation> blocksAndTags, @NonNull BlockState state) {
        Identifier blockLocation = RegistryHelpers.getLocationFromBlock(state.getBlock());

        boolean containsBlockDirectly = blocksAndTags.contains(new BlockOrTagLocation(blockLocation));
        if(containsBlockDirectly) return true;

        // otherwise check if the block is included in any tags
        List<BlockOrTagLocation> tagLocations = blocksAndTags.stream().filter(BlockOrTagLocation::isTag).toList();
        for (BlockOrTagLocation tagLocation : tagLocations) {
            if(RegistryHelpers.isBlockInTag(blockLocation, RegistryHelpers.getBlockTagKey(tagLocation.location()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NonNull String toString() {
        return (this.isTag() ? "#" : "") + location.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BlockOrTagLocation castedObj)) return false;
        return this.location.equals(castedObj.location);
    }
}
