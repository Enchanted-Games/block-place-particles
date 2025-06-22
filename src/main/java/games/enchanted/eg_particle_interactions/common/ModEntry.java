package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigHandler;
import games.enchanted.eg_particle_interactions.common.particle_override.BlockParticleOverrides;

/**
 * This is the entry point for your mod's common side, called by each modloader specific side.
 */
public class ModEntry {
    public static void startOfModLoading() {
        Logging.info("Mod is loading on a {} environment!", ModConstants.TARGET_PLATFORM);
    }

    public static void endOfModLoading() {
        ConfigHandler.load();
        ConfigHandler.save();
        BlockParticleOverrides.registerOverrides();
        Logging.info("Loaded Successfully!");
    }
}
