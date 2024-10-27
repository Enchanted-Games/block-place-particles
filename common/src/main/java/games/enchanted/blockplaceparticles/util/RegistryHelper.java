package games.enchanted.blockplaceparticles.util;

import dev.isxander.yacl3.gui.controllers.dropdown.ItemController;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RegistryHelper {
    public static <T> Stream<ResourceLocation> getMatchingIdentifiers(String value, DefaultedRegistry<T> registryToSearch) {
        int sep = value.indexOf(58);
        Predicate<ResourceLocation> filterPredicate = getFilterPredicate(value, sep, registryToSearch);

        return registryToSearch.keySet().stream()
            .filter(filterPredicate)
            .sorted((id1, id2) -> {
                String path = (sep == -1 ? value : value.substring(sep + 1)).toLowerCase();
                boolean id1StartsWith = id1.getPath().toLowerCase().startsWith(path);
                boolean id2StartsWith = id2.getPath().toLowerCase().startsWith(path);
                if (id1StartsWith) {
                    return id2StartsWith ? id1.compareTo(id2) : -1;
                } else {
                    return id2StartsWith ? 1 : id1.compareTo(id2);
                }
            }
        );
    }

    private static @NotNull <T> Predicate<ResourceLocation> getFilterPredicate(String value, int separator, DefaultedRegistry<T> registryToSearch) {
        Predicate<ResourceLocation> filterPredicate;
        if (separator == -1) {
            filterPredicate = (ResourceLocation identifier) -> identifier.getPath().contains(value) || (registryToSearch.get(identifier)).toString().toLowerCase().contains(value.toLowerCase());
        } else {
            String namespace = value.substring(0, separator);
            String path = value.substring(separator + 1);
            filterPredicate = (ResourceLocation identifier) -> identifier.getNamespace().equals(namespace) && identifier.getPath().startsWith(path);
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

    public static ResourceLocation getLocationFromBlock(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
    public static Block getBlockFromLocation(ResourceLocation location) {
        return BuiltInRegistries.BLOCK.getValue(location);
    }
    public static Fluid getFluidFromLocation(ResourceLocation location) {
        return BuiltInRegistries.FLUID.getValue(location);
    }
}
