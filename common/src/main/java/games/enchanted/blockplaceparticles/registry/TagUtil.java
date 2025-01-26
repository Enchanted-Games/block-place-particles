package games.enchanted.blockplaceparticles.registry;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TagUtil {
    /**
     * Checks if a block resource location is present in a list that contains {@link BlockOrTagLocation}s.
     *
     * @param blockAndBlockTagLocationList {@link BlockOrTagLocation} list
     * @param blockLocation                block resource location to test if present the list
     */
    public static boolean doesListContainBlock(@NotNull List<BlockOrTagLocation> blockAndBlockTagLocationList, ResourceLocation blockLocation) {
        if(blockLocation == null) return false;

        boolean containsBlockDirectly = blockAndBlockTagLocationList.contains(new BlockOrTagLocation(blockLocation));
        if(containsBlockDirectly) return true;

        // otherwise check if the block is included in any tags
        List<BlockOrTagLocation> tagLocations = blockAndBlockTagLocationList.stream().filter(BlockOrTagLocation::isTag).toList();
        for (BlockOrTagLocation tagLocation : tagLocations) {
            if(RegistryHelpers.isBlockInTag(blockLocation, RegistryHelpers.getBlockTagKey(tagLocation.location()))) {
                return true;
            }
        }
        return false;
    }
}
