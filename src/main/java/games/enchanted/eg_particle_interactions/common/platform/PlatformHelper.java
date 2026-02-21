package games.enchanted.eg_particle_interactions.common.platform;

import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
//? if neoforge {
/*import games.enchanted.eg_particle_interactions.neoforge.registry.NeoParticleProviderRegistry;
import games.enchanted.eg_particle_interactions.neoforge.registry.NeoReloadListenerRegistry;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;
*///?} else {
//? if minecraft: < 26.1 {
/*import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
*///? } else {
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
//? }
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.PackType;
//?}
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.nio.file.Path;

public class PlatformHelper {
    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    public static String getPlatformName() {
        //? if fabric {
        return "Fabric";
        //?} else {
        /*return "NeoForge";
        *///?}
    }

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    public static boolean isModLoaded(String modId) {
        //? if fabric {
        return FabricLoader.getInstance().isModLoaded(modId);
        //?} else {
        /*return ModList.get().isLoaded(modId);
        *///?}
    }

    /**
     * Checks if a mod with the given id is loaded / going to be loaded. Safe for early loading such as mixin config plugins
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    public static boolean isModLoadedEarly(String modId) {
        //? if fabric {
        return FabricLoader.getInstance().isModLoaded(modId);
        //?} else {
        /*LoadingModList modList = FMLLoader.getCurrent().getLoadingModList();
        return modList.getModFiles().contains(modList.getModFileById(modId));
        *///?}
    }

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    public static boolean isDevelopmentEnvironment() {
        //? if fabric {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
        //?} else {
        /*//? if minecraft: <= 1.21.8 {
        /^return !FMLLoader.isProduction();
        ^///?} else {
        return !FMLLoader.getCurrent().isProduction();
        //?}
         *///?}
    }

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    public static String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Creates and returns a new instance of net.minecraft.core.particles.SimpleParticleType
     */
    public static SimpleParticleType createNewSimpleParticle(boolean alwaysShow) {
        //? if fabric {
        return FabricParticleTypes.simple(alwaysShow);
        //?} else {
        /*return new SimpleParticleType(alwaysShow);
         *///?}
    }

    /**
     * Returns the path where configuration files are stored within the .minecraft directory
     */
    public static Path getConfigPath() {
        //? if fabric {
        return FabricLoader.getInstance().getConfigDir();
        //?} else {
        /*return FMLPaths.CONFIGDIR.get();
         *///?}
    }

    /**
     * Register a resource reload listener
     */
    public static void registerResourceReloadListener(PreparableReloadListener listener, Identifier id) {
        //? if fabric {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).
            //? if minecraft: < 26.1 {
            /*registerReloader
             *///? } else {
            registerReloadListener
            //?}
            (id, listener);
        //? } else {
        /*NeoReloadListenerRegistry.registerListener(listener, id);
        *///? }
    }
}