package games.enchanted.eg_particle_interactions.common.registry;

import net.minecraft.IdentifierException;
import net.minecraft.client.Minecraft;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RegistryHelpers {
    @SuppressWarnings("unchecked")
    public static <R, T extends R> T register(ResourceKey<? extends Registry<R>> registryKey, Supplier<T> entry, Identifier key) {
        Registry<R> registry = Objects.requireNonNull( BuiltInRegistries.REGISTRY.getValue((ResourceKey) registryKey));
        return Registry.register(registry, key, entry.get());
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

    public static BlockOrTagLocation validateBlockOrTagLocationWithFallback(String location, BlockOrTagLocation fallback) {
        try {
            if(location.startsWith("#")) {
                return new BlockOrTagLocation(Identifier.parse(location.replace("#", "").toLowerCase()), true);
            }

            Identifier blockLocation = Identifier.parse(location.toLowerCase());
            Optional<Block> blockFromLoc = BuiltInRegistries.BLOCK.getOptional(blockLocation);
            if(blockFromLoc.isEmpty()) {
                return fallback;
            }
            if(blockFromLoc.get().defaultBlockState().isAir()) {
                return fallback;
            }
            return new BlockOrTagLocation(blockLocation);
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
    public static BlockOrTagLocation getBlockLocationFromBlock(Block block) {
        return new BlockOrTagLocation(getLocationFromBlock(block));
    }
    public static Block getBlockFromLocation(Identifier location) {
        return BuiltInRegistries.BLOCK.getValue(location);
    }
    public static Holder<Block> getBlockHolderFromLocation(Identifier location) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(getBlockFromLocation(location));
    }

    public static boolean isBlockInTag(Identifier blockLocation, TagKey<Block> tagKey) {
        Optional<HolderSet.Named<Block>> tagHolder = BuiltInRegistries.BLOCK.get(tagKey);
        if(tagHolder.isEmpty()) return false;

        Holder<Block> blockHolder = getBlockHolderFromLocation(blockLocation);
        return tagHolder.get().contains(blockHolder);
    }
    public static TagKey<Block> getBlockTagKey(Identifier tagLocation) {
        return TagKey.create(Registries.BLOCK, tagLocation);
    }

    public static List<Identifier> getLoadedBlockTags() {
        return BuiltInRegistries.BLOCK.getTags().map(t -> t.key().location()).toList();
    }


    public static @Nullable Registry<Biome> getBiomeRegistry() {
        if(Minecraft.getInstance().level == null) return null;
        return Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.BIOME);
    }
    public static @Nullable Biome getBiomeFromLocation(Identifier location) {
        Registry<Biome> biomeReg = getBiomeRegistry();
        if(biomeReg == null) return null;
        return biomeReg.getValue(location);
    }
    public static @Nullable Identifier getLocationFromBiome(Biome biome) {
        Registry<Biome> biomeReg = getBiomeRegistry();
        if(biomeReg == null) return null;
        return biomeReg.getKey(biome);
    }
    public static @Nullable Holder<Biome> getBiomeHolderFromLocation(Identifier location) {
        Registry<Biome> biomeReg = getBiomeRegistry();
        Biome biome = getBiomeFromLocation(location);
        if(biomeReg == null || biome == null) return null;
        return biomeReg.wrapAsHolder(biome);
    }
    public static boolean isBiomeInTag(Identifier biomeLocation, TagKey<Biome> tagKey) {
        if(Minecraft.getInstance().level == null) return false;
        Optional<HolderSet.Named<Biome>> tagHolder = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.BIOME).get(tagKey);
        if(tagHolder.isEmpty()) return false;

        Holder<Biome> biomeHolder = getBiomeHolderFromLocation(biomeLocation);
        if(biomeHolder == null) return false;
        return tagHolder.get().contains(biomeHolder);
    }
    public static TagKey<Biome> getBiomeTagKey(Identifier tagLocation) {
        return TagKey.create(Registries.BIOME, tagLocation);
    }

}
