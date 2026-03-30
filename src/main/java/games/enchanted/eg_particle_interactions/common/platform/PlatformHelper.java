package games.enchanted.eg_particle_interactions.common.platform;

import games.enchanted.eg_particle_interactions.common.Constants;
//? if neoforge {
/*import games.enchanted.eg_particle_interactions.neoforge.NeoForgeEntry;
import games.enchanted.eg_particle_interactions.neoforge.registry.NeoReloadListenerRegistry;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;
*///?} else {
import games.enchanted.eg_particle_interactions.fabric.resource.FabricResourceLoaderRegisterer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
//?}
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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
        FabricResourceLoaderRegisterer.getInstance().registerResourceLoader(listener, id);
        //? } else {
        /*NeoReloadListenerRegistry.registerListener(listener, id);
        *///? }
    }

    /**
     * Get the path to something in the mod jar
     */
    public static Path getResourcePathFromModJar(String... strings) {
        //? if fabric {
        Optional<ModContainer> container = Objects.requireNonNull(
            FabricLoader.getInstance().getModContainer(Constants.MOD_ID),
            "Could not get mod container '" + Constants.MOD_ID + "'"
        );
        Optional<Path> path = container.flatMap(modContainer -> modContainer.findPath(String.join("/", strings)));
        return path.orElseThrow(() -> new NullPointerException("Could not find path in particle interactions mod jar '" + Arrays.toString(strings) + "'"));
        //? } else {
        /*throw new NotImplementedException("getResourcePathFromModJar not implemented on neoforge platform");
        *///? }
    }
}