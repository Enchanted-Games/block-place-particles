package games.enchanted.eg_particle_interactions.common.config2.screen;

import dev.isxander.yacl3.api.*;
import games.enchanted.eg_particle_interactions.common.config2.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config2.categories.BlockInteractionOptions;
import games.enchanted.eg_particle_interactions.common.config2.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import games.enchanted.eg_particle_interactions.common.particle_override.BlockParticleOverride;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen {
    public static Screen createConfigScreen(Screen parentScreen) {
        YetAnotherConfigLib.Builder yaclBuilder = YetAnotherConfigLib.createBuilder()
            .save(ConfigOptions::applyAndSaveConfig)
            .title(ConfigTranslation.getConfigTitle().toComponent());
        return createConfigCategories(yaclBuilder).generateScreen(parentScreen);
    }

    private static YetAnotherConfigLib createConfigCategories(YetAnotherConfigLib.Builder yaclBuilder) {
        // general category
        yaclBuilder.category( ConfigCategory.createBuilder()
            .name(ConfigTranslation.getCategoryName(ConfigTranslation.GENERAL_CATEGORY).toComponent())
            .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.GENERAL_CATEGORY)))

            // info (mod name and credits)
            .group(OptionGroup.createBuilder()
                .name( ConfigTranslation.MOD_CREDITS_KEY.toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.MOD_CREDITS_KEY) ))
                .collapsed(true)
                .option(LabelOption.createBuilder().line(Component.empty()).build())
                .build())

            // general
            .group( ConfigScreenHelper.createGenericConfigGroup(
                "general",
                ConfigTranslation.GENERAL_CATEGORY,
                false,
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.PIXEL_CONSISTENT_TERRAIN_PARTICLES,
                    GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.PARTICLE_ZFIGHTING_FIX,
                    GeneralOptions.PARTICLE_Z_FIGHTING_FIX
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.PROJECTILE_PARTICLE_VELOCITY_FIX,
                    GeneralOptions.PROJECTILE_BREAKING_PARTICLE_VELOCITY_FIX
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.AUTO_COLLAPSE_CONFIG_LISTS,
                    GeneralOptions.AUTO_COLLAPSE_CONFIG_LISTS
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.FIREFLY_FIXES,
                    GeneralOptions.FIREFLY_FIXES
                )
            ))

            // performance: general
            .group(  ConfigScreenHelper.createGenericConfigGroup(
                "performance_general",
                ConfigTranslation.GENERAL_CATEGORY,
                false,
                ConfigScreenHelper.integerSliderOption(
                    ConfigTranslation.RENDER_DISTANCE_INTERACTION,
                    GeneralOptions.INTERACTION_RENDER_DISTANCE,
                    1,
                    32,
                    1
                ),
                ConfigScreenHelper.integerSliderOption(
                    ConfigTranslation.RENDER_DISTANCE_BLOCK,
                    GeneralOptions.BLOCK_RENDER_DISTANCE,
                    1,
                    32,
                    1
                ),
                ConfigScreenHelper.integerSliderOption(
                    ConfigTranslation.RENDER_DISTANCE_AMBIENT,
                    GeneralOptions.AMBIENT_RENDER_DISTANCE,
                    1,
                    32,
                    1
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.PARTICLE_PHYSICS_ENABLED,
                    GeneralOptions.ADVANCED_PARTICLE_PHYSICS
                )
            ))

            .group( ConfigScreenHelper.createGenericConfigGroup(
                "performance_particles",
                ConfigTranslation.GENERAL_CATEGORY,
                false,
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.SPARKS_ADDITIONAL_FLASH_EFFECT,
                    GeneralOptions.ADDITIONAL_SPARK_FLASH_EFFECT
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.SPARKS_WATER_EVAPORATION,
                    GeneralOptions.WATER_EVAPORATION
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.DUST_ADDITIONAL_SPECKS,
                    GeneralOptions.DUST_SPECKS
                )
            ))

            // debug
            .group( ConfigScreenHelper.createGenericConfigGroup(
                "debug",
                ConfigTranslation.GENERAL_CATEGORY,
                true,
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.TOGGLE_INTERACTION_DEBUG_LOGS,
                    GeneralOptions.DEBUG_INTERACTION_LOGGING
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.TOGGLE_TEXTURE_DEBUG_LOGS,
                    GeneralOptions.DEBUG_TEXTURE_LOGGING
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.DEBUG_SHOW_EMITTER_BOUNDS,
                    GeneralOptions.DEBUG_EMITTER_BOUNDS
                )
            ))
        .build());


        // block override category
        yaclBuilder.category(ConfigScreenHelper.createBlockParticleOverrideConfigWidgets(
            ConfigCategory.createBuilder()
                .name(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY).toComponent())
                .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY)))

                // vanilla block particles
                .group(
                    ConfigScreenHelper.createOptionsForBlockOverride(BlockParticleOverride.VANILLA)
                )
                .group(
                    ConfigScreenHelper.createSeparator()
                )

                // block config info
                .group(OptionGroup.createBuilder()
                    .name( ConfigTranslation.getGroupName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY, "info").toComponent() )
                    .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY, "info")) ))
                    .collapsed(true)
                    .option(LabelOption.createBuilder().line(Component.empty()).build())
                .build())
            )
        .build());


        // block interaction/ambient category
        yaclBuilder.category( ConfigCategory.createBuilder()
            .name(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY).toComponent())
            .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY)))

            // category info
            .group(OptionGroup.createBuilder()
                .name( ConfigTranslation.getGroupName(ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY, "info").toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY, "info")) ))
                .collapsed(true)
                .option(LabelOption.createBuilder().line(Component.empty()).build())
                .build())

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // underwater bubbles
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "underwater_block_bubbles",
                "underwater_block_bubbles",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_PLACE, "underwater_block_bubbles", BlockInteractionOptions.UNDERWATER_BUBBLES_ON_PLACE_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE, "underwater_block_bubbles", BlockInteractionOptions.UNDERWATER_BUBBLES_MAX_ON_PLACE, 1, 50, 1),
                ConfigScreenHelper.booleanOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_BREAK, "underwater_block_bubbles", BlockInteractionOptions.UNDERWATER_BUBBLES_ON_BREAK_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK, "underwater_block_bubbles", BlockInteractionOptions.UNDERWATER_BUBBLES_MAX_ON_BREAK, 1, 50, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // block rustle particles
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "block_rustle",
                "block_rustle",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "block_rustle", BlockInteractionOptions.BLOCK_RUSTLE_ENABLED)
            ))
            .group(
                ConfigScreenHelper.createBlockLocationListOption(
                    "block_rustle",
                    "block_rustle_blocks",
                    ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                    BlockInteractionOptions.BLOCK_RUSTLE_BLOCKS
                )
            )

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // campfire ambient particles
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "campfire_particles",
                "campfire_particles",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "sparks", BlockInteractionOptions.CAMPFIRE_SPARK_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "sparks", BlockInteractionOptions.CAMPFIRE_SPARK_SPAWN_CHANCE, 1, 100, 1),
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "embers", BlockInteractionOptions.CAMPFIRE_EMBER_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "embers", BlockInteractionOptions.CAMPFIRE_EMBER_SPAWN_CHANCE, 1, 100, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // fire ambient particles
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "fire_particles",
                "fire_particles",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "sparks", BlockInteractionOptions.FIRE_SPARK_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "sparks", BlockInteractionOptions.FIRE_SPARK_SPAWN_CHANCE, 1, 100, 1),
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "embers", BlockInteractionOptions.FIRE_EMBER_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "embers", BlockInteractionOptions.FIRE_EMBER_SPAWN_CHANCE, 1, 100, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // anvil use spark
            .group( ConfigScreenHelper.createParticleToggleAndIntSliderConfigGroup(
                "anvil_craft_sparks",
                "anvil_craft_sparks",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                BlockInteractionOptions.ANVIL_USE_SPARKS_ENABLED,
                ConfigTranslation.IS_PARTICLE_ENABLED,
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_CRAFT, "anvil_craft_sparks", BlockInteractionOptions.ANVIL_USE_SPARKS_MAX_ON_USE, 1, 32, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // grindstone use spark
            .group( ConfigScreenHelper.createParticleToggleAndIntSliderConfigGroup(
                "grindstone_craft_sparks",
                "grindstone_craft_sparks",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                BlockInteractionOptions.GRINDSTONE_USE_SPARKS_ENABLED,
                ConfigTranslation.IS_PARTICLE_ENABLED,
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_CRAFT, "grindstone_craft_sparks", BlockInteractionOptions.GRINDSTONE_USE_SPARKS_MAX_ON_USE, 1, 32, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // falling block effect
            .group( ConfigScreenHelper.createParticleToggleAndIntSliderConfigGroup(
                "falling_block_effect",
                "falling_block_effect",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                BlockInteractionOptions.BLOCK_FALLING_EFFECT_ENABLED,
                ConfigTranslation.IS_PARTICLE_ENABLED,
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.PARTICLE_EFFECT_RENDER_DISTANCE, "falling_block_effect", BlockInteractionOptions.BLOCK_FALLING_EFFECT_RENDER_DISTANCE, 1, 512, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // redstone interaction dust
            .group( ConfigScreenHelper.createParticleToggleAndIntSliderConfigGroup(
                "redstone_interaction_dust",
                "redstone_interaction_dust",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                BlockInteractionOptions.REDSTONE_INTERACTION_DUST_ENABLED,
                ConfigTranslation.IS_PARTICLE_ENABLED,
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_INTERACT, "redstone_interaction_dust", BlockInteractionOptions.REDSTONE_INTERACTION_DUST_AMOUNT, 1, 32, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // smoker smoke
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "smoker_smoke",
                "smoker_smoke",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "smoker_smoke", BlockInteractionOptions.SMOKER_SMOKE_ENABLED)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // furnace embers
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "furnace_embers",
                "furnace_embers",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "furnace_embers", BlockInteractionOptions.FURNACE_EMBERS_ENABLED),
                ConfigScreenHelper.booleanOption(ConfigTranslation.ARE_VANILLA_FURNACE_PARTICLES_ENABLED, "furnace_embers", BlockInteractionOptions.VANILLA_FURNACE_PARTICLES_ENABLED)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // blast furnace sparks
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "blast_furnace_sparks",
                "blast_furnace_sparks",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "blast_furnace_sparks", BlockInteractionOptions.BLAST_FURNACE_SPARKS_ENABLED)
            ))
        .build());

        return yaclBuilder.build();
    }
}
