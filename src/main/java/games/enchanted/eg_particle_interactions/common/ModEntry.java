package games.enchanted.eg_particle_interactions.common;

/**
 * This is the entry point for your mod's common side, called by each modloader specific side.
 */
public class ModEntry {
    public static void startOfModLoading() {
        Logging.info("Mod is loading on a {} environment!", ModConstants.TARGET_PLATFORM);
    }

    public static void endOfModLoading() {
        games.enchanted.eg_particle_interactions.common.config.ConfigHandler.load();
        games.enchanted.eg_particle_interactions.common.config.ConfigHandler.save();
        games.enchanted.eg_particle_interactions.common.particle_override.BlockParticleOverrides.registerOverrides();
        Logging.info("Loaded Successfully!");
    }
}
