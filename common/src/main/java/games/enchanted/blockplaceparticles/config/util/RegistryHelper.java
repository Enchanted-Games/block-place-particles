package games.enchanted.blockplaceparticles.config.util;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class RegistryHelper {
    public static <T> Stream<ResourceLocation> getMatchingIdentifiers(String value, DefaultedRegistry<T> registryToSearch) {
        int sep = value.indexOf(58);
        Predicate<ResourceLocation> filterPredicate = getFilterPredicate(value, sep, registryToSearch);

        return registryToSearch.holders().map((holder) -> holder.key().location()).filter(filterPredicate).sorted((id1, id2) -> {
            String path = (sep == -1 ? value : value.substring(sep + 1)).toLowerCase();
            boolean id1StartsWith = id1.getPath().toLowerCase().startsWith(path);
            boolean id2StartsWith = id2.getPath().toLowerCase().startsWith(path);
            if (id1StartsWith) {
                return id2StartsWith ? id1.compareTo(id2) : -1;
            } else {
                return id2StartsWith ? 1 : id1.compareTo(id2);
            }
        });
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

    public static BlockItem getDefaultedBlockItem(String location, BlockItem defaultItem) {
        try {
            ResourceLocation itemLocation = ResourceLocation.parse(location.toLowerCase());
            if (BuiltInRegistries.ITEM.containsKey(itemLocation)) {
                Item item = BuiltInRegistries.ITEM.get(itemLocation);
                if(item instanceof BlockItem) return (BlockItem) item;
            }
        } catch (ResourceLocationException ignored) {}
        return defaultItem;
    }

    public static Fluid getDefaultedFluid(String location, Fluid defaultItem) {
        try {
            ResourceLocation itemLocation = ResourceLocation.parse(location.toLowerCase());
            if (BuiltInRegistries.FLUID.containsKey(itemLocation)) {
                return BuiltInRegistries.FLUID.get(itemLocation);
            }
        } catch (ResourceLocationException ignored) {}
        return defaultItem;
    }
}
