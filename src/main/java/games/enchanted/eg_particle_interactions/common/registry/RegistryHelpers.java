package games.enchanted.eg_particle_interactions.common.registry;

import net.minecraft.IdentifierException;
import net.minecraft.client.Minecraft;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RegistryHelpers {
    public static <R, T extends R> T register(Registry<R> registry, T entry, Identifier key) {
        return Registry.register(registry, key, entry);
    }

    public static <T> Stream<Identifier> getMatchingLocations(String search, DefaultedRegistry<T> registryToSearch) {
        int separatorIndex = search.indexOf(':');
        String unspacedSearch = search.replace(' ', '_');
        Predicate<Identifier> filterPredicate = getFilterPredicate(unspacedSearch, separatorIndex, registryToSearch);

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

    public static <T> Stream<Identifier> getMatchingTagLocations(String search, DefaultedRegistry<T> registryToSearch) {
        int separatorIndex = search.indexOf(':');
        String unspacedSearch = search.replace(' ', '_');
        Predicate<Identifier> filterPredicate = getFilterPredicate(unspacedSearch, separatorIndex, registryToSearch);

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

    private static @NotNull <T> Predicate<Identifier> getFilterPredicate(String search, int separatorIndex, DefaultedRegistry<T> registryToSearch) {
        Predicate<Identifier> filterPredicate;
        if (separatorIndex == -1) {
            filterPredicate = (Identifier location) -> location.getPath().contains(search) || (registryToSearch.get(location)).toString().toLowerCase().contains(search.toLowerCase());
        } else {
            String namespace = search.substring(0, separatorIndex);
            String path = search.substring(separatorIndex + 1);
            filterPredicate = (Identifier location) -> location.getNamespace().equals(namespace) && location.getPath().startsWith(path);
        }
        return filterPredicate;
    }

    public static Fluid getDefaultedFluid(String location, Fluid fallback) {
        try {
            Identifier fluidLocation = Identifier.parse(location.toLowerCase());
            Optional<Fluid> fluidFromLoc = BuiltInRegistries.FLUID.getOptional(fluidLocation);
            if(fluidFromLoc.isEmpty()) {
                return fallback;
            }
            if(fluidFromLoc.get() == Fluids.EMPTY) {
                return fallback;
            }
            return fluidFromLoc.get();
        } catch (IdentifierException ignored) {}
        return fallback;
    }

    public static Identifier validateBlockLocationWithFallback(String location, Identifier fallback) {
        try {
            Identifier blockLocation = Identifier.parse(location.toLowerCase());
            Optional<Block> blockFromLoc = BuiltInRegistries.BLOCK.getOptional(blockLocation);
            if(blockFromLoc.isEmpty()) {
                return fallback;
            }
            if(blockFromLoc.get().defaultBlockState().isAir()) {
                return fallback;
            }
            return blockLocation;
        } catch (IdentifierException ignored) {}
        return fallback;
    }

    public static ObjectOrTagLocation validateBlockOrTagLocationWithFallback(String location, ObjectOrTagLocation fallback) {
        try {
            if(location.startsWith("#")) {
                return new ObjectOrTagLocation(Identifier.parse(location.replace("#", "").toLowerCase()), true);
            }

            Identifier blockLocation = Identifier.parse(location.toLowerCase());
            Optional<Block> blockFromLoc = BuiltInRegistries.BLOCK.getOptional(blockLocation);
            if(blockFromLoc.isEmpty()) {
                return fallback;
            }
            if(blockFromLoc.get().defaultBlockState().isAir()) {
                return fallback;
            }
            return new ObjectOrTagLocation(blockLocation);
        } catch (IdentifierException ignored) {}

        return fallback;
    }

    public static Identifier validateFluidLocationWithFallback(String location, Identifier fallback) {
        try {
            Identifier fluidLocation = Identifier.parse(location.toLowerCase());
            Optional<Fluid> blockFromLoc = BuiltInRegistries.FLUID.getOptional(fluidLocation);
            if(blockFromLoc.isEmpty()) {
                return fallback;
            }
            if(blockFromLoc.get().defaultFluidState().createLegacyBlock().isAir()) {
                return fallback;
            }
            return fluidLocation;
        } catch (IdentifierException ignored) {}
        return fallback;
    }


    public static Identifier getLocationFromFluid(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid);
    }
    public static Fluid getFluidFromLocation(Identifier location) {
        return BuiltInRegistries.FLUID.getValue(location);
    }

    public static Identifier getLocationFromBlock(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
    public static ObjectOrTagLocation getBlockLocationFromBlock(Block block) {
        return new ObjectOrTagLocation(getLocationFromBlock(block));
    }
    public static Block getBlockFromLocation(Identifier location) {
        return BuiltInRegistries.BLOCK.getValue(location);
    }


    public static <T> Identifier getIdFromHolder(Holder<T> holder, Registry<T> registry) {
        return registry.getKey(holder.value());
    }
    public static <T> T lookupObject(Identifier id, Registry<T> registry) {
        return registry.getValue(id);
    }
    public static <T> Holder<T> createHolder(Identifier id, Registry<T> registry) {
        return registry.wrapAsHolder(lookupObject(id, registry));
    }

    public static <T> boolean isObjectInTag(Identifier objectId, TagKey<T> tagKey, Registry<T> registry) {
        Optional<HolderSet.Named<T>> tagHolder = registry.get(tagKey);
        if(tagHolder.isEmpty()) return false;

        Holder<T> holder = createHolder(objectId, registry);
        return tagHolder.get().contains(holder);
    }
    public static <T> TagKey<T> createTagKey(Identifier tagId, ResourceKey<? extends Registry<T>> registryKey) {
        return TagKey.create(registryKey, tagId);
    }


    public static Registry<Biome> getBiomeRegistry(Level level) {
        return level.registryAccess().lookupOrThrow(Registries.BIOME);
    }
}
