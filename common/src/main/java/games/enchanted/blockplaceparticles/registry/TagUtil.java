package games.enchanted.blockplaceparticles.registry;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TagUtil {
    /**
     * Checks if a block resource location is present in a list that contains {@link BlockLocation}s.
     *
     * @param blockAndBlockTagLocationList {@link BlockLocation} list
     * @param blockLocation                block resource location to test if present the list
     */
    public static boolean doesListContainBlock(@NotNull List<BlockLocation> blockAndBlockTagLocationList, ResourceLocation blockLocation) {
        if(blockLocation == null) return false;

        boolean containsBlockDirectly = blockAndBlockTagLocationList.contains(new BlockLocation(blockLocation));
        if(containsBlockDirectly) return true;

        // otherwise check if the block is included in any tags
        List<BlockLocation> tagLocations = blockAndBlockTagLocationList.stream().filter(BlockLocation::isTag).toList();
        for (BlockLocation tagLocation : tagLocations) {
            if(RegistryHelpers.isBlockInTag(blockLocation, RegistryHelpers.getBlockTagKey(tagLocation.location()))) {
                return true;
            }
        }
        return false;
    }
}
