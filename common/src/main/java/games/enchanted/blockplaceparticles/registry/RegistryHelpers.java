package games.enchanted.blockplaceparticles.registry;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RegistryHelpers {
    public static <T> Stream<ResourceLocation> getMatchingLocations(String search, DefaultedRegistry<T> registryToSearch) {
        int separatorIndex = search.indexOf(':');
        String unspacedSearch = search.replace(' ', '_');
        Predicate<ResourceLocation> filterPredicate = getFilterPredicate(unspacedSearch, separatorIndex, registryToSearch);

        return registryToSearch.keySet().stream()
            .filter(filterPredicate)
            .sorted((location1, location2) -> {
                String path = (separatorIndex == -1 ? unspacedSearch : unspacedSearch.substring(separatorIndex + 1)).toLowerCase();
                boolean location1StartsWith = location1.getPath().toLowerCase().startsWith(path);
                boolean location2StartsWith = location2.getPath().toLowerCase().startsWith(path);
                if (location1StartsWith) {
                    return location2StartsWith ? location1.compareTo(location2) : -1;
                } else {
                    return location2StartsWith ? 1 : location1.compareTo(location2);
                }
            }
        );
    }

    public static <T> Stream<ResourceLocation> getMatchingTagLocations(String search, DefaultedRegistry<T> registryToSearch) {
        int separatorIndex = search.indexOf(':');
        String unspacedSearch = search.replace(' ', '_');
        Predicate<ResourceLocation> filterPredicate = getFilterPredicate(unspacedSearch, separatorIndex, registryToSearch);

        return registryToSearch.getTags()
            .map(tag -> tag.key().location())
            .filter(filterPredicate)
            .sorted((location1, location2) -> {
                    String path = (separatorIndex == -1 ? unspacedSearch : unspacedSearch.substring(separatorIndex + 1)).toLowerCase();
                    boolean location1StartsWith = location1.getPath().toLowerCase().startsWith(path);
                    boolean location2StartsWith = location2.getPath().toLowerCase().startsWith(path);
                    if (location1StartsWith) {
                        return location2StartsWith ? location1.compareTo(location2) : -1;
                    } else {
                        return location2StartsWith ? 1 : location1.compareTo(location2);
                    }
                }
            );
    }

    private static @NotNull <T> Predicate<ResourceLocation> getFilterPredicate(String search, int separatorIndex, DefaultedRegistry<T> registryToSearch) {
        Predicate<ResourceLocation> filterPredicate;
        if (separatorIndex == -1) {
            filterPredicate = (ResourceLocation location) -> location.getPath().contains(search) || (registryToSearch.get(location)).toString().toLowerCase().contains(search.toLowerCase());
        } else {
            String namespace = search.substring(0, separatorIndex);
            String path = search.substring(separatorIndex + 1);
            filterPredicate = (ResourceLocation location) -> location.getNamespace().equals(namespace) && location.getPath().startsWith(path);
        }
        return filterPredicate;
    }

    public static Fluid getDefaultedFluid(String location, Fluid fallback) {
        try {
            ResourceLocation fluidLocation = ResourceLocation.parse(location.toLowerCase());
            Optional<Fluid> fluidFromLoc = BuiltInRegistries.FLUID.getOptional(fluidLocation);
            if(fluidFromLoc.isEmpty()) {
                return fallback;
            }
            if(fluidFromLoc.get() == Fluids.EMPTY) {
                return fallback;
            }
            return fluidFromLoc.get();
        } catch (ResourceLocationException ignored) {}
        return fallback;
    }

    public static ResourceLocation validateBlockLocationWithFallback(String location, ResourceLocation fallback) {
        try {
            ResourceLocation blockLocation = ResourceLocation.parse(location.toLowerCase());
            Optional<Block> blockFromLoc = BuiltInRegistries.BLOCK.getOptional(blockLocation);
            if(blockFromLoc.isEmpty()) {
                return fallback;
            }
            if(blockFromLoc.get().defaultBlockState().isAir()) {
                return fallback;
            }
            return blockLocation;
        } catch (ResourceLocationException ignored) {}
        return fallback;
    }

    public static BlockLocation validateBlockOrTagLocationWithFallback(String location, BlockLocation fallback) {
        try {
            if(location.startsWith("#")) {
                return new BlockLocation(ResourceLocation.parse(location.replace("#", "").toLowerCase()), true);
            }

            ResourceLocation blockLocation = ResourceLocation.parse(location.toLowerCase());
            Optional<Block> blockFromLoc = BuiltInRegistries.BLOCK.getOptional(blockLocation);
            if(blockFromLoc.isEmpty()) {
                return fallback;
            }
            if(blockFromLoc.get().defaultBlockState().isAir()) {
                return fallback;
            }
            return new BlockLocation(blockLocation);
        } catch (ResourceLocationException ignored) {}

        return fallback;
    }

    public static ResourceLocation validateFluidLocationWithFallback(String location, ResourceLocation fallback) {
        try {
            ResourceLocation fluidLocation = ResourceLocation.parse(location.toLowerCase());
            Optional<Fluid> blockFromLoc = BuiltInRegistries.FLUID.getOptional(fluidLocation);
            if(blockFromLoc.isEmpty()) {
                return fallback;
            }
            if(blockFromLoc.get().defaultFluidState().createLegacyBlock().isAir()) {
                return fallback;
            }
            return fluidLocation;
        } catch (ResourceLocationException ignored) {}
        return fallback;
    }


    public static ResourceLocation getLocationFromFluid(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid);
    }
    public static Fluid getFluidFromLocation(ResourceLocation location) {
        return BuiltInRegistries.FLUID.getValue(location);
    }

    public static ResourceLocation getLocationFromBlock(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
    public static BlockLocation getBlockLocationFromBlock(Block block) {
        return new BlockLocation(getLocationFromBlock(block));
    }
    public static Block getBlockFromLocation(ResourceLocation location) {
        return BuiltInRegistries.BLOCK.getValue(location);
    }
    public static Holder<Block> getBlockHolderFromLocation(ResourceLocation location) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(getBlockFromLocation(location));
    }


    public static boolean isBlockInTag(ResourceLocation blockLocation, TagKey<Block> tagKey) {
        Optional<HolderSet.Named<Block>> tagHolder = BuiltInRegistries.BLOCK.get(tagKey);
        if(tagHolder.isEmpty()) return false;

        Holder<Block> blockHolder = getBlockHolderFromLocation(blockLocation);
        return tagHolder.get().contains(blockHolder);
    }

    public static TagKey<Block> getBlockTagKey(ResourceLocation tagLocation) {
        return TagKey.create(Registries.BLOCK, tagLocation);
    }

    public static List<ResourceLocation> getLoadedBlockTags() {
        return BuiltInRegistries.BLOCK.getTags().map(t -> t.key().location()).toList();
    }
}
