package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigHandler;
import games.enchanted.eg_particle_interactions.common.particle_override.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

import java.util.List;

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
        List<Pair<ResourceLocation, PreparableReloadListener>> reloadListeners = createReloadListeners(textureManager);
        //? if fabric {
        resourceManager.registerReloadListener(particlePaletteAtlas);
        reloadListeners.forEach(resourceLocationAndReloadListenerPair -> {
            resourceManager.registerReloadListener(resourceLocationAndReloadListenerPair.value());
        });
        //?}
    }

    public static List<Pair<ResourceLocation, PreparableReloadListener>> createReloadListeners(TextureManager textureManager) {
        particlePaletteAtlas = new ParticlePaletteAtlasManager(textureManager);
        return List.of(Pair.of(ParticlePaletteAtlasManager.ATLAS_LOCATION, particlePaletteAtlas));
    }
}
