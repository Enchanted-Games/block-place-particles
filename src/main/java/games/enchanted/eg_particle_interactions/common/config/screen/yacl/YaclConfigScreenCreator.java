package games.enchanted.eg_particle_interactions.common.config.screen.yacl;

import dev.isxander.yacl3.api.*;
import games.enchanted.eg_particle_interactions.common.config.compat.ConfigScreenCreator;
import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.categories.*;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import games.enchanted.eg_particle_interactions.common.particle.overrides.BlockParticleOverride;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YaclConfigScreenCreator implements ConfigScreenCreator {
    @Override
    public Screen createScreen(Screen parentScreen) {
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
                    GeneralOptions.SPARK_WATER_EVAPORATION
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
                    ConfigTranslation.DEBUG_SHOW_EMITTER_BOUNDS,
                    GeneralOptions.DEBUG_EMITTER_BOUNDS
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.DEBUG_PARTICLE_TICK_BOUNDING_BOXES,
                    GeneralOptions.DEBUG_PARTICLE_TICK_BOUNDING_BOXES
                ),
                ConfigScreenHelper.genericBooleanOption(
                    ConfigTranslation.DEBUG_PARTICLE_RENDER_BOUNDING_BOXES,
                    GeneralOptions.DEBUG_PARTICLE_RENDER_BOUNDING_BOXES
                )
            ))
        .build());


        // block override category
        yaclBuilder.category(ConfigScreenHelper.createBlockParticleOverrideConfigWidgets(
            ConfigCategory.createBuilder()
                .name(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY).toComponent())
                .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY)))

                .group(
                    OptionGroup.createBuilder()
                        .option(ConfigScreenHelper.booleanOption(
                            "disable_all_block_placing_particles",
                            "block_placing",
                            BlockOverrideOptions.DISABLE_ALL_PLACING_PARTICLES
                        ))
                        .option(ConfigScreenHelper.booleanOption(
                            "disable_all_block_breaking_particles",
                            "block_breaking",
                            BlockOverrideOptions.DISABLE_ALL_BREAKING_PARTICLES
                        ))
                    .build()
                )
                .group(
                    ConfigScreenHelper.createSeparator()
                )

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
        yaclBuilder.category( ConfigCategory.createBuilder()
            .name(ConfigTranslation.getCategoryName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY).toComponent())
            .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY)))

            // category info
            .group(OptionGroup.createBuilder()
                .name( ConfigTranslation.getGroupName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY, "info").toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY, "info")) ))
                .collapsed(true)
                .option(LabelOption.createBuilder().line(Component.empty()).build())
                .build())

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // minecart sparks at max speed
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "minecart_sparks",
                "minecart_sparks",
                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.SPAWN_PARTICLE_WHEN_MINECART_AT_MAX_SPEED, "minecart_sparks", EntityOptions.MINECART_SPARKS_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.MINECART_WHEEL_PARTICLE_AMOUNT, "minecart_sparks", EntityOptions.MINECART_SPARKS_SPAWN_CHANCE, 1, 100, 1),
                ConfigScreenHelper.booleanOption(ConfigTranslation.MINECART_ONLY_WITH_PASSENGER, "minecart_sparks", EntityOptions.MINECART_SPARKS_ONLY_WITH_PASSENGER)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // lightning strike effect
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "lightning_strike",
                "lightning_strike",
                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED, "lightning_strike", EntityOptions.LIGHTNING_STRIKE_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_LIGHTNING_STRIKE, "arcs", EntityOptions.LIGHTNING_STRIKE_AMOUNT_OF_ARCS, 0, 16, 1),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_LIGHTNING_STRIKE, "sparks", EntityOptions.LIGHTNING_STRIKE_AMOUNT_OF_SPARKS, 0, 32, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // blaze sparks
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "blaze_sparks",
                "blaze_sparks",
                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.ENTITY_AMBIENT_PARTICLE_SPAWN_CHANCE, "blaze_sparks", EntityOptions.BLAZE_SPARKS_SPAWN_CHANCE, 1, 100, 1),
                ConfigScreenHelper.booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ENTITY_HURT, "blaze_sparks", EntityOptions.BLAZE_SPARKS_SPAWN_ON_HURT),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_ENTITY_HURT, "blaze_sparks", EntityOptions.BLAZE_SPARKS_AMOUNT_ON_HURT, 1, 32, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // item frame interactions
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "item_frame_dust",
                "item_frame_dust",
                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "item_frame_dust", EntityOptions.ITEM_FRAME_INTERACTION_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_INTERACT, "item_frame_dust", EntityOptions.ITEM_FRAME_INTERACTION_AMOUNT, 1, 30, 1)
            ))
        .build());

        // fluid placement config category
        yaclBuilder.category( ConfigCategory.createBuilder()
            .name(ConfigTranslation.getCategoryName(ConfigTranslation.FLUIDS_CONFIG_CATEGORY).toComponent())
            .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.FLUIDS_CONFIG_CATEGORY)))

            // fluid placement config info
            .group(OptionGroup.createBuilder()
                .name( ConfigTranslation.getGroupName(ConfigTranslation.FLUIDS_CONFIG_CATEGORY, "info").toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.FLUIDS_CONFIG_CATEGORY, "info")) ))
                .collapsed(true)
                .option(LabelOption.createBuilder().line(Component.empty()).build())
                .build())

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // tinted water splash
            .group( ConfigScreenHelper.createFluidParticleToggleAndMaxConfigGroup(
                "tinted_splash",
                "tinted_splash",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                FluidPlacementOptions.WATER_SPLASH_ENABLED,
                ConfigScreenHelper.maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, FluidPlacementOptions.WATER_SPLASH_AMOUNT_ON_PLACE)
            ))
            .group( ConfigScreenHelper.createFluidListOption(
                "tinted_splash",
                "tinted_splash_fluids",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                FluidPlacementOptions.WATER_SPLASH_FLUIDS
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // lava splash
            .group( ConfigScreenHelper.createFluidParticleToggleAndMaxConfigGroup(
                "lava_splash",
                "generic_particle_fluids",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                FluidPlacementOptions.LAVA_SPLASH_ENABLED,
                ConfigScreenHelper.maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, FluidPlacementOptions.LAVA_SPLASH_AMOUNT_ON_PLACE)
            ))
            .group( ConfigScreenHelper.createFluidListOption(
                "lava_splash",
                "generic_particle",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                FluidPlacementOptions.LAVA_SPLASH_FLUIDS
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // generic splash
            .group( ConfigScreenHelper.createFluidParticleToggleAndMaxConfigGroup(
                "generic_splash",
                "generic_splash",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                FluidPlacementOptions.GENERIC_SPLASH_ENABLED,
                ConfigScreenHelper.maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, FluidPlacementOptions.GENERIC_SPLASH_AMOUNT_ON_PLACE)
            ))
            .group( ConfigScreenHelper.createFluidListOption(
                "generic_splash",
                "generic_splash_fluids",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                FluidPlacementOptions.GENERIC_SPLASH_FLUIDS
            ))
        .build());


        // fluid ambient config category
        yaclBuilder.category( ConfigCategory.createBuilder()
            .name(ConfigTranslation.getCategoryName(ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY).toComponent())
            .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY)))

            // fluid ambient config info
            .group(OptionGroup.createBuilder()
                .name( ConfigTranslation.getGroupName(ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY, "info").toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY, "info")) ))
                .collapsed(true)
                .option(LabelOption.createBuilder().line(Component.empty()).build())
                .build())

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // lava bubble pop
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "lava_bubble_pop",
                "lava_bubble_pop",
                ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "lava_bubble_pop", FluidAmbientOptions.LAVA_BUBBLE_POP_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "lava_bubble_pop", FluidAmbientOptions.LAVA_BUBBLE_POP_SPAWN_CHANCE, 1, 100, 1)
            ))

            .group(
                ConfigScreenHelper.createSeparator()
            )

            // underwater bubble streams
            .group( ConfigScreenHelper.createMultipleOptionsConfigGroup(
                "underwater_bubble_streams",
                "underwater_bubble_streams",
                ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY,
                ConfigScreenHelper.booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "underwater_bubble_streams", FluidAmbientOptions.UNDERWATER_BUBBLE_STREAM_ENABLED),
                ConfigScreenHelper.integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "underwater_bubble_streams", FluidAmbientOptions.UNDERWATER_BUBBLE_STREAM_SPAWN_CHANCE, 1, 100, 1)
            ))
            .group(
                ConfigScreenHelper.createBlockLocationListOption(
                    "underwater_bubble_streams",
                    "underwater_bubble_streams_blocks",
                    ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY,
                    FluidAmbientOptions.UNDERWATER_BUBBLE_STREAM_BLOCKS
                )
            )

        .build());
        
        return yaclBuilder.build();
    }
}
