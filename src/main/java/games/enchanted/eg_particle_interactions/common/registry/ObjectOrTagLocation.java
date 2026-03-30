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
public record ObjectOrTagLocation(Identifier location, boolean isTag) {
    public static Codec<ObjectOrTagLocation> CODEC = Codec.STRING.comapFlatMap(
        string -> {
            Identifier parsedLocation = Identifier.parse(string.replace("#", ""));
            return DataResult.success(new ObjectOrTagLocation(parsedLocation, string.startsWith("#")));
        },
        ObjectOrTagLocation::toString
    );

    public ObjectOrTagLocation(Identifier location) {
        this(location, false);
    }


    /**
     * Checks if a blockstate is present in a list of block and block tags.
     *
     * @param blocksAndTags  {@link ObjectOrTagLocation} list
     * @param state          block to test if present the list
     */
    public static boolean doesListContainBlock(@NonNull List<ObjectOrTagLocation> blocksAndTags, @NonNull BlockState state) {
        Identifier blockLocation = RegistryHelpers.getLocationFromBlock(state.getBlock());

        boolean containsBlockDirectly = blocksAndTags.contains(new ObjectOrTagLocation(blockLocation));
        if(containsBlockDirectly) return true;

        // otherwise check if the block is included in any tags
        List<ObjectOrTagLocation> tagLocations = blocksAndTags.stream().filter(ObjectOrTagLocation::isTag).toList();
        for (ObjectOrTagLocation tagLocation : tagLocations) {
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
        if(!(obj instanceof ObjectOrTagLocation castedObj)) return false;
        return this.location.equals(castedObj.location);
    }
}
