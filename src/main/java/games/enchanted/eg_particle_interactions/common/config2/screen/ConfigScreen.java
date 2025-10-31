package games.enchanted.eg_particle_interactions.common.config2.screen;

import dev.isxander.yacl3.api.*;
import games.enchanted.eg_particle_interactions.common.config.ConfigHandler;
import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import games.enchanted.eg_particle_interactions.common.config2.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config2.categories.BlockInteractionOptions;
import games.enchanted.eg_particle_interactions.common.config2.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.config2.categories.ItemInteractionOptions;
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
        

        // item use category
        yaclBuilder.category( ConfigCategory.createBuilder()
            .name(ConfigTranslation.getCategoryName(ConfigTranslation.ITEMS_CONFIG_CATEGORY).toComponent())
            .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.ITEMS_CONFIG_CATEGORY)))

            // item config info
            .group(OptionGroup.createBuilder()
                .name( ConfigTranslation.getGroupName(ConfigTranslation.ITEMS_CONFIG_CATEGORY, "info").toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.ITEMS_CONFIG_CATEGORY, "info")) ))
                .collapsed(true)
                .option(LabelOption.createBuilder().line(Component.empty()).build())
                .build())

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // brushing particles
            .group( ConfigScreenHelper.createGenericConfigGroup(
                "brush_particles",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                false,
                ConfigScreenHelper.enumCycleOption(ConfigTranslation.BRUSH_PARTICLE_BEHAVIOUR, ItemInteractionOptions.BRUSH_PARTICLE_BEHAVIOUR, BrushParticleBehaviour.class)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // flint and steel spark
            .group( ConfigScreenHelper.createParticleToggleAndMaxAndIntensityConfigGroup(
                "flint_and_steel_sparks",
                "flint_and_steel_sparks",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_ENABLED,
                ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE,
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "flint_and_steel_sparks", ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_AMOUNT, 1, 32, 1),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.ITEM_USE_PARTICLE_INTENSITY, "flint_and_steel_sparks", ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_INTENSITY, 1, 8, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // fire charge smoke
            .group( ConfigScreenHelper.createParticleToggleAndMaxAndIntensityConfigGroup(
                "fire_charge_smoke",
                "fire_charge_smoke",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                ItemInteractionOptions.FIRE_CHARGE_PARTICLES_ENABLED,
                ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE,
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "fire_charge_smoke", ItemInteractionOptions.FIRE_CHARGE_PARTICLES_AMOUNT, 1, 32, 1),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.ITEM_USE_PARTICLE_INTENSITY, "fire_charge_smoke", ItemInteractionOptions.FIRE_CHARGE_PARTICLES_INTENSITY, 1, 8, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // axe strip
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "axe_strip_particles",
                "axe_strip_particles",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "axe_strip_particles", ItemInteractionOptions.AXE_STRIP_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "axe_strip_particles", ItemInteractionOptions.AXE_STRIP_AMOUNT, 1, 50, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // hoe till
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "hoe_till_particles",
                "hoe_till_particles",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "hoe_till_particles", ItemInteractionOptions.HOE_TILL_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "hoe_till_particles", ItemInteractionOptions.HOE_TILL_AMOUNT, 1, 50, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // shovel flatten
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "shovel_flatten_particles",
                "shovel_flatten_particles",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "shovel_flatten_particles", ItemInteractionOptions.SHOVEL_FLATTEN_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "shovel_flatten_particles", ItemInteractionOptions.SHOVEL_FLATTEN_AMOUNT, 1, 50, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // honey collection
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "honey_collection",
                "honey_collection",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.SPAWN_ON_HONEY_COLLECTED, "honey_collection", ItemInteractionOptions.HONEY_COLLECTION_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_HONEY_COLLECTED, "honey_collection", ItemInteractionOptions.HONEY_COLLECTION_AMOUNT, 1, 50, 1),
                ConfigScreenHelper.booleanOption(ConfigTranslation.REPLACE_VANILLA_PARTICLES, "honey_collection", ItemInteractionOptions.HONEY_COLLECTION_REPLACE_VANILLA)
            ))
        .build());

        // entity category
//        yaclBuilder.category( ConfigCategory.createBuilder()
//            .name(ConfigTranslation.getCategoryName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY).toComponent())
//            .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY)))
//
//            // category info
//            .group(OptionGroup.createBuilder()
//                .name( ConfigTranslation.getGroupName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY, "info").toComponent() )
//                .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY, "info")) ))
//                .collapsed(true)
//                .option(LabelOption.createBuilder().line(Component.empty()).build())
//                .build())
//
//            .group(
//                ConfigScreenHelper.createSeparator()
//            )
//
//            // minecart sparks at max speed
//            .group( createMultipleOptionsConfigGroup(
//                "minecart_sparks",
//                "minecart_sparks",
//                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
//                booleanOption(ConfigTranslation.SPAWN_PARTICLE_WHEN_MINECART_AT_MAX_SPEED, "minecart_sparks", Binding.generic(ConfigHandler.minecart_enabled_DEFAULT, () -> ConfigHandler.minecart_enabled, newVal -> ConfigHandler.minecart_enabled = newVal)),
//                integerSliderOption(ConfigTranslation.MINECART_WHEEL_PARTICLE_AMOUNT, "minecart_sparks", ConfigHandler.minecart_spawnChance_DEFAULT, () -> ConfigHandler.minecart_spawnChance, newVal -> ConfigHandler.minecart_spawnChance = newVal, 1, 100, 1),
//                booleanOption(ConfigTranslation.MINECART_ONLY_WITH_PASSENGER, "minecart_sparks", Binding.generic(ConfigHandler.minecart_onlyWithPassenger_DEFAULT, () -> ConfigHandler.minecart_onlyWithPassenger, newVal -> ConfigHandler.minecart_onlyWithPassenger = newVal))
//            ))
//
//            .group(
//                ConfigScreenHelper.createSeparator()
//            )
//
//            // lightning strike effect
//            .group( createMultipleOptionsConfigGroup(
//                "lightning_strike",
//                "lightning_strike",
//                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
//                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED, "lightning_strike", Binding.generic(ConfigHandler.lightningStrike_enabled_DEFAULT, () -> ConfigHandler.lightningStrike_enabled, newVal -> ConfigHandler.lightningStrike_enabled = newVal)),
//                integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_LIGHTNING_STRIKE, "arcs", ConfigHandler.lightningStrike_amountOfArcs_DEFAULT, () -> ConfigHandler.lightningStrike_amountOfArcs, newVal -> ConfigHandler.lightningStrike_amountOfArcs = newVal, 0, 16, 1),
//                integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_LIGHTNING_STRIKE, "sparks", ConfigHandler.lightningStrike_amountOfSparks_DEFAULT, () -> ConfigHandler.lightningStrike_amountOfSparks, newVal -> ConfigHandler.lightningStrike_amountOfSparks = newVal, 0, 32, 1)
//            ))
//
//            .group(
//                ConfigScreenHelper.createSeparator()
//            )
//
//            // blaze sparks
//            .group( createMultipleOptionsConfigGroup(
//                "blaze_sparks",
//                "blaze_sparks",
//                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
//                integerSliderOption(ConfigTranslation.ENTITY_AMBIENT_PARTICLE_SPAWN_CHANCE, "blaze_sparks", ConfigHandler.blaze_spawnChance_DEFAULT, () -> ConfigHandler.blaze_spawnChance, newVal -> ConfigHandler.blaze_spawnChance = newVal, 1, 100, 1),
//                booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ENTITY_HURT, "blaze_sparks", Binding.generic(ConfigHandler.blaze_spawnOnHurt_DEFAULT, () -> ConfigHandler.blaze_spawnOnHurt, newVal -> ConfigHandler.blaze_spawnOnHurt = newVal)),
//                integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_ENTITY_HURT, "blaze_sparks", ConfigHandler.blaze_amountToSpawnOnHurt_DEFAULT, () -> ConfigHandler.blaze_amountToSpawnOnHurt, newVal -> ConfigHandler.blaze_amountToSpawnOnHurt = newVal, 1, 32, 1)
//            ))
//
//            .group(
//                ConfigScreenHelper.createSeparator()
//            )
//
//            // item frame interactions
//            .group( createMultipleOptionsConfigGroup(
//                "item_frame_dust",
//                "item_frame_dust",
//                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
//                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "item_frame_dust", Binding.generic(ConfigHandler.itemFrame_enabled_DEFAULT, () -> ConfigHandler.itemFrame_enabled, newVal -> ConfigHandler.itemFrame_enabled = newVal)),
//                integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_INTERACT, "item_frame_dust", ConfigHandler.itemFrame_amount_DEFAULT, () -> ConfigHandler.itemFrame_amount, newVal -> ConfigHandler.itemFrame_amount = newVal, 1, 30, 1)
//            ))
//
//        .build());

        return yaclBuilder.build();
    }
}
