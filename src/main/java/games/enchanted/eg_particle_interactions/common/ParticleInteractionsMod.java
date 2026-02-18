package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.override_system.override.rule.OverrideRuleFile;
import games.enchanted.eg_particle_interactions.common.particle.overrides.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import net.minecraft.resources.Identifier;

import java.util.List;

//? if minecraft: > 1.21.8 && fabric {
//?}
//? if minecraft: <= 1.21.8 && fabric {
/*import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
*///?}


/**
 * This is the entry point for your mod's common side, called by each modloader specific side.
 */
public class ParticleInteractionsMod {
    //? if minecraft: <= 1.21.8 {
    /*public static ParticlePaletteAtlasManager particlePaletteAtlas;
    *///?}

    public static void startOfModLoading() {
        Logging.info("Mod is loading on a {} environment!", Constants.TARGET_PLATFORM);

        PlatformHelper.registerResourceReloadListener(ParticleOverrides.INSTANCE, ParticleInteractionsMod.id("particle_overrides"));
        PlatformHelper.registerResourceReloadListener(BlockOverrideManager.INSTANCE, ParticleInteractionsMod.id("block_override_rules"));
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
//        snowOverrideTest.setOverrideId(id("test"));
//        ParticleOverrides.registerBlockStateOverrideRule(snowOverrideTest);
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
