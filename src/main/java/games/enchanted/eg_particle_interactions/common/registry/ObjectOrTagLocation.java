package games.enchanted.eg_particle_interactions.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

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
        return doesListContainObject(blocksAndTags, state.typeHolder(), BuiltInRegistries.BLOCK);
    }

    /**
     * Checks if a fluidstate is present in a list of fluid and fluid tags.
     *
     * @param fluidsAndTags  {@link ObjectOrTagLocation} list
     * @param state          fluid to test if present the list
     */
    public static boolean doesListContainFluid(@NonNull List<ObjectOrTagLocation> fluidsAndTags, @NonNull FluidState state) {
        return doesListContainObject(fluidsAndTags, state.typeHolder(), BuiltInRegistries.FLUID);
    }

    /**
     * Checks if an object is present in a list of objects and object tags.
     *
     * @param objectsAndTags  {@link ObjectOrTagLocation} list
     * @param object          object to test if present the list
     * @param registry        registry for checking tag contents
     */
    public static <T> boolean doesListContainObject(List<ObjectOrTagLocation> objectsAndTags, Holder<T> object, Registry<T> registry) {
        Identifier id = RegistryHelpers.getIdFromHolder(object, registry);

        boolean containsDirect = objectsAndTags.contains(new ObjectOrTagLocation(id));
        if(containsDirect) return true;

        List<ObjectOrTagLocation> tagLocations = objectsAndTags.stream().filter(ObjectOrTagLocation::isTag).toList();
        for (ObjectOrTagLocation tagLocation : tagLocations) {
            if(RegistryHelpers.isObjectInTag(
                id,
                RegistryHelpers.createTagKey(tagLocation.location(), registry.key()),
                registry
            )) {
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
