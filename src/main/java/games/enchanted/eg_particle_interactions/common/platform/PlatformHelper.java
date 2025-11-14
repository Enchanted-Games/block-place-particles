package games.enchanted.eg_particle_interactions.common.platform;

import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
//? if neoforge {
/*import games.enchanted.eg_particle_interactions.neoforge.registry.NeoParticleProviderRegistry;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
*///?} else {
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
//?}
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

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
     * Registers a particle to a particle provider
     */
    public static <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> particleType, ModParticleTypes.SpriteProviderReg<T> particleProvider) {
        //? if fabric {
        ParticleFactoryRegistry.getInstance().register(particleType, particleProvider::create);
        //?} else {
        /*NeoParticleProviderRegistry.registerProviderWhenReady(particleType, particleProvider);
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
}