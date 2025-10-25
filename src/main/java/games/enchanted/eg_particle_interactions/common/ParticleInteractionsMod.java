package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigHandler;
import games.enchanted.eg_particle_interactions.common.particle_override.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

//? if minecraft: > 1.21.8 && fabric {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
//?}
//? if minecraft: <= 1.21.8 && fabric {
/*import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
*///?}

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * This is the entry point for your mod's common side, called by each modloader specific side.
 */
public class ParticleInteractionsMod {
    //? if minecraft: > 1.21.8 {
    @Deprecated
    //?}
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

    public static void registerAtlases(TextureManager textureManager) {
        List<Pair<ResourceLocation, PreparableReloadListener>> reloadListeners = createReloadListeners(textureManager);
        //? if fabric {
        reloadListeners.forEach(resourceLocationAndReloadListenerPair -> {
            //? if minecraft: > 1.21.8 {
            ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(resourceLocationAndReloadListenerPair.key(), resourceLocationAndReloadListenerPair.value());
            //?} else {
            /*ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
                @Override
                public @NotNull CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, Executor backgroundExecutor, Executor gameExecutor) {
                    return resourceLocationAndReloadListenerPair.value().reload(barrier, manager, backgroundExecutor, gameExecutor);
                }

                @Override
                public ResourceLocation getFabricId() {
                    return resourceLocationAndReloadListenerPair.key();
                }
            });
            *///?}
        });
        //?}
    }

    public static List<Pair<ResourceLocation, PreparableReloadListener>> createReloadListeners(TextureManager textureManager) {
        //? if minecraft: <= 1.21.8 {
        /*particlePaletteAtlas = new ParticlePaletteAtlasManager(textureManager);
        return List.of(Pair.of(ParticlePaletteAtlasManager.ATLAS_LOCATION, particlePaletteAtlas));
        *///?} else {
        return List.of();
        //?}
    }
}
