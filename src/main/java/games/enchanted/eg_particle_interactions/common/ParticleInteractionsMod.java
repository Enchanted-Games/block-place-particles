package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.override_system.manager.ParticleOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.preset.unbaked.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.override_system.preset.unbaked.UnbakedPreset;
import games.enchanted.eg_particle_interactions.common.particle.overrides.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

//? if minecraft: > 1.21.8 && fabric {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
//?}
//? if minecraft: <= 1.21.8 && fabric {
/*import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
*///?}

import java.util.List;

/**
 * This is the entry point for your mod's common side, called by each modloader specific side.
 */
public class ParticleInteractionsMod {
    //? if minecraft: <= 1.21.8 {
    /*public static ParticlePaletteAtlasManager particlePaletteAtlas;
    *///?}

    public static void startOfModLoading() {
        Logging.info("Mod is loading on a {} environment!", Constants.TARGET_PLATFORM);
    }

    public static void endOfModLoading() {
        ConfigOptions.readConfig();
        BlockParticleOverrides.registerOverrides();
        ParticleOverrides.init();
//        ParticleOverrideManager.addBlockOverride(Blocks.GRASS_BLOCK.defaultBlockState(), OverridePreset.getOrCreate(List.of(
//            new OverridePreset.OverrideAndWeight(ParticleOverrides.SNOW_TEST, 1),
//            new OverridePreset.OverrideAndWeight(ParticleOverrides.SPARK_TEST, 20)
//        )));
//        ParticleOverrideManager.addBlockOverride(Blocks.GRASS_BLOCK.defaultBlockState().setValue(GrassBlock.SNOWY, true), OverridePreset.getOrCreate(List.of(
//            new OverridePreset.OverrideAndWeight(ParticleOverrides.SNOW_TEST, 20),
//            new OverridePreset.OverrideAndWeight(ParticleOverrides.SPARK_TEST, 1)
//        )));

        ParticleOverrideManager.registerUnbakedBlockPreset(new UnbakedPreset<>(
            List.of(
                new UnbakedPreset.AdditionsSection<>(
                    1,
                    ParticleOverrides.SNOW_TEST,
                    List.of(new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("grass_block"), false))
                )),
                new UnbakedPreset.AdditionsSection<>(
                    20,
                    ParticleOverrides.SPARK_TEST,
                    List.of(new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("grass_block"), false))
                )),
                new UnbakedPreset.AdditionsSection<>(
                    33,
                    ParticleOverrides.SNOW_TEST,
                    List.of(new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("stone"), false))
                )),
                new UnbakedPreset.AdditionsSection<>(
                    66,
                    ParticleOverrides.EMPTY,
                    List.of(new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("stone"), false))
                ))
            ),
            List.of()
        ));

        Logging.info("Loaded Successfully!");

        var t = Blocks.SANDSTONE_STAIRS.getStateDefinition();
        t = t;
    }

    public static void registerAtlases(TextureManager textureManager) {
        List<Pair<Identifier, PreparableReloadListener>> reloadListeners = createReloadListeners(textureManager);
        //? if fabric {
        reloadListeners.forEach(resourceLocationAndReloadListenerPair -> {
            //? if minecraft: > 1.21.8 {
            ResourceLoader.get(PackType.CLIENT_RESOURCES).
                //? if minecraft: < 26.1 {
                /*registerReloader
                *///? } else {
                registerReloadListener
                //?}
                (resourceLocationAndReloadListenerPair.key(), resourceLocationAndReloadListenerPair.value());
            //?} else {
            /*ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
                @Override
                public @NotNull CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, Executor backgroundExecutor, Executor gameExecutor) {
                    return resourceLocationAndReloadListenerPair.value().reload(barrier, manager, backgroundExecutor, gameExecutor);
                }

                @Override
                public Identifier getFabricId() {
                    return resourceLocationAndReloadListenerPair.key();
                }
            });
            *///?}
        });
        //?}
    }

    public static List<Pair<Identifier, PreparableReloadListener>> createReloadListeners(TextureManager textureManager) {
        //? if minecraft: <= 1.21.8 {
        /*particlePaletteAtlas = new ParticlePaletteAtlasManager(textureManager);
        return List.of(Pair.of(ParticlePaletteAtlasManager.ATLAS_LOCATION, particlePaletteAtlas));
        *///?} else {
        return List.of();
        //?}
    }
}
