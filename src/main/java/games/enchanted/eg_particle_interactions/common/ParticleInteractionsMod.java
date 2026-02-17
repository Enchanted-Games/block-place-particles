package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrideManager;
import games.enchanted.eg_particle_interactions.common.particle.overrides.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
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

/**
 * This is the entry point for your mod's common side, called by each modloader specific side.
 */
public class ParticleInteractionsMod {
    //? if minecraft: <= 1.21.8 {
    /*public static ParticlePaletteAtlasManager particlePaletteAtlas;
    *///?}

    public static void startOfModLoading() {
        Logging.info("Mod is loading on a {} environment!", Constants.TARGET_PLATFORM);

        PlatformHelper.registerResourceReloadListener(ParticleOverrideManager.INSTANCE, ParticleInteractionsMod.id("particle_overrides"));
    }

    public static void endOfModLoading() {
        ConfigOptions.readConfig();
        BlockParticleOverrides.registerOverrides();

//        var snowOverrideTest = new OverrideRuleFile<>(
//            List.of(
//                new OverrideRuleFile.AdditionsSection<>(
//                    1,
//                    List.of(
//                        new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("grass_block"), false))
//                    )
//                ),
//                new OverrideRuleFile.AdditionsSection<>(
//                    33,
//                    List.of(
//                        new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("stone"), false))
//                    )
//                )
//            ),
//            List.of()
//        );
//        snowOverrideTest.setOverrideId(ParticleOverrides.SNOW_TEST);
//        ParticleOverrideManager.registerBlockStateOverrideRule(snowOverrideTest);
//
//        var sparkOverrideTest = new OverrideRuleFile<>(
//            List.of(
//                new OverrideRuleFile.AdditionsSection<>(
//                    20,
//                    List.of(
//                        new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("stone"), false))
//                    )
//                ),
//                new OverrideRuleFile.AdditionsSection<>(
//                    2,
//                    List.of(
//                        new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("grass_block"), false))
//                    )
//                )
//            ),
//            List.of(
//                new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("stone"), false))
//            )
//        );
//        sparkOverrideTest.setOverrideId(ParticleOverrides.SPARK_TEST);
//        ParticleOverrideManager.registerBlockStateOverrideRule(sparkOverrideTest);
//
//
//        var emptyOverrideTest = new OverrideRuleFile<>(
//            List.of(
//                new OverrideRuleFile.AdditionsSection<>(
//                    66,
//                    List.of(
//                        new BlockStatePredicate(new BlockOrTagLocation(Identifier.withDefaultNamespace("stone"), false))
//                    )
//                )
//            ),
//            List.of()
//        );
//        emptyOverrideTest.setOverrideId(ParticleOverrides.EMPTY);
//        ParticleOverrideManager.registerBlockStateOverrideRule(emptyOverrideTest);


        Logging.info("Loaded Successfully!");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
