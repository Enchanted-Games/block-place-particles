package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigHandler;
import games.enchanted.eg_particle_interactions.common.particle_override.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

/**
 * This is the entry point for your mod's common side, called by each modloader specific side.
 */
public class ParticleInteractionsMod {
    public static ParticlePaletteAtlasManager particlePaletteAtlas;

    public static void startOfModLoading() {
        Logging.info("Mod is loading on a {} environment!", Constants.TARGET_PLATFORM);
    }

    public static void endOfModLoading() {
        ConfigHandler.load();
        ConfigHandler.save();
        BlockParticleOverrides.registerOverrides();
        Logging.info("Loaded Successfully!");
    }

    public static void registerAtlases(ReloadableResourceManager resourceManager, TextureManager textureManager) {
        particlePaletteAtlas = new ParticlePaletteAtlasManager(textureManager);
        resourceManager.registerReloadListener(particlePaletteAtlas);
    }
}
