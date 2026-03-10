package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import net.minecraft.resources.Identifier;

public class ParticleInteractionsMod {
    public static void startOfModLoading() {
        Logging.info("Mod is loading on a {} environment!", Constants.TARGET_PLATFORM);

        ParticleTypesRegistry.init();

        // register reload listeners here if fabric api is installed or if targeting neoforge
        // if no fabric api reload listeners are registered in Minecraft init
        //? if fabric {
        if (isFabricResourceLoaderPresent()) {
            registerResourceReloadListeners();
        }
        //?} else {
        /*registerResourceReloadListeners();
        *///? }
    }

    public static void endOfModLoading() {
        ConfigOptions.readConfig();
        Logging.info("Loaded Successfully!");
    }

    public static void registerResourceReloadListeners() {
        PlatformHelper.registerResourceReloadListener(ParticleOverrides.INSTANCE, ParticleInteractionsMod.id("particle_overrides"));
        PlatformHelper.registerResourceReloadListener(BlockOverrideManager.INSTANCE, ParticleInteractionsMod.id("block_override_rules"));
        PlatformHelper.registerResourceReloadListener(ParticleAppearanceManager.INSTANCE, ParticleInteractionsMod.id("texture_sources"));
    }

    public static boolean isFabricResourceLoaderPresent() {
        return PlatformHelper.isModLoaded(Constants.FABRIC_RESOURCE_LOADER_ID);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
