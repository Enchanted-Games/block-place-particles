package games.enchanted.blockplaceparticles;

import games.enchanted.blockplaceparticles.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Supplier;

public class ParticleInteractionsMod {
    public static final String MOD_ID = "eg_block_place_particles";
    public static final String MOD_NAME = "Block Place Particles";

    public static void startOfModLoading() {
        ParticleInteractionsLogging.message("Mod is loading on a {} environment", Services.PLATFORM.getPlatformName());
    }

    public static void endOfModLoading() {
        ParticleInteractionsLogging.message("Loaded Successfully!");
    }

    @SuppressWarnings("unchecked")
    public static <R, T extends R> T register(ResourceKey<? extends Registry<R>> registryKey, Supplier<T> entry, ResourceLocation key) {
        Registry<R> registry = Objects.requireNonNull( BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey) );
        return Registry.register(registry, key, entry.get() );
    }
}