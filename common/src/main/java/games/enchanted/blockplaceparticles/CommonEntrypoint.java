package games.enchanted.blockplaceparticles;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Supplier;

public class CommonEntrypoint {
    public static void initBeforeRegistration() {
    }

    @SuppressWarnings("unchecked")
    public static <R, T extends R> T register(ResourceKey<? extends Registry<R>> registryKey, Supplier<T> entry, ResourceLocation key) {
        Registry<R> registry = Objects.requireNonNull( BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey) );
        return Registry.register(registry, key, entry.get() );
    }
}