package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.config.controller.BlockLocationController;
import games.enchanted.blockplaceparticles.config.controller.FluidLocationController;
import games.enchanted.blockplaceparticles.config.type.BrushParticleBehaviour;
import games.enchanted.blockplaceparticles.localisation.ConfigTranslation;
import games.enchanted.blockplaceparticles.particle_override.BlockParticleOverride;
import games.enchanted.blockplaceparticles.registry.BlockOrTagLocation;
import games.enchanted.blockplaceparticles.registry.RegistryHelpers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigScreen {
    public static Screen createConfigScreen(Screen parentScreen) {
        YetAnotherConfigLib.Builder yaclBuilder = YetAnotherConfigLib.createBuilder()
            .save(ConfigHandler::save)
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
            .group( createGenericConfigGroup(
                "general",
                ConfigTranslation.GENERAL_CATEGORY,
                false,
                genericBooleanOption(
                    ConfigTranslation.PIXEL_CONSISTENT_TERRAIN_PARTICLES,
                    Binding.generic(ConfigHandler.general_pixelConsistentTerrainParticles_DEFAULT, () -> ConfigHandler.general_pixelConsistentTerrainParticles, newVal -> ConfigHandler.general_pixelConsistentTerrainParticles = newVal)
                ),
                genericBooleanOption(
                    ConfigTranslation.PARTICLE_ZFIGHTING_FIX,
                    Binding.generic(ConfigHandler.general_particleZFightingFix_DEFAULT, () -> ConfigHandler.general_particleZFightingFix, newVal -> ConfigHandler.general_particleZFightingFix = newVal)
                ),
                genericBooleanOption(
                    ConfigTranslation.AUTO_COLLAPSE_CONFIG_LISTS,
                    Binding.generic(ConfigHandler.general_autoCollapseConfigLists_DEFAULT, () -> ConfigHandler.general_autoCollapseConfigLists, newVal -> ConfigHandler.general_autoCollapseConfigLists = newVal)
                )
            ))

            // performance
            .group( createGenericConfigGroup(
                "performance",
                ConfigTranslation.GENERAL_CATEGORY,
                false,
                integerSliderOption(
                    ConfigTranslation.RENDER_DISTANCE_INTERACTION,
                    ConfigHandler.general_interactionRenderDistance_DEFAULT,
                    () -> ConfigHandler.general_interactionRenderDistance,
                    newVal -> ConfigHandler.general_interactionRenderDistance = newVal,
                    1,
                    32,
                    1
                ),
                integerSliderOption(
                    ConfigTranslation.RENDER_DISTANCE_BLOCK,
                    ConfigHandler.general_blockRenderDistance_DEFAULT,
                    () -> ConfigHandler.general_blockRenderDistance,
                    newVal -> ConfigHandler.general_blockRenderDistance = newVal,
                    1,
                    32,
                    1
                ),
                integerSliderOption(
                    ConfigTranslation.RENDER_DISTANCE_AMBIENT,
                    ConfigHandler.general_ambientRenderDistance_DEFAULT,
                    () -> ConfigHandler.general_ambientRenderDistance,
                    newVal -> ConfigHandler.general_ambientRenderDistance = newVal,
                    1,
                    32,
                    1
                ),
                genericBooleanOption(
                    ConfigTranslation.PARTICLE_PHYSICS_ENABLED,
                    Binding.generic(ConfigHandler.general_extraParticlePhysicsEnabled_DEFAULT, () -> ConfigHandler.general_extraParticlePhysicsEnabled, newVal -> ConfigHandler.general_extraParticlePhysicsEnabled = newVal)
                ),
                genericBooleanOption(
                    ConfigTranslation.SPARKS_ADDITIONAL_FLASH_EFFECT,
                    Binding.generic(ConfigHandler.particle_sparks_additionalFlashEffects_DEFAULT, () -> ConfigHandler.particle_sparks_additionalFlashEffects, newVal -> ConfigHandler.particle_sparks_additionalFlashEffects = newVal)
                ),
                genericBooleanOption(
                    ConfigTranslation.SPARKS_WATER_EVAPORATION,
                    Binding.generic(ConfigHandler.particle_sparks_waterEvaporation_DEFAULT, () -> ConfigHandler.particle_sparks_waterEvaporation, newVal -> ConfigHandler.particle_sparks_waterEvaporation = newVal)
                ),
                genericBooleanOption(
                    ConfigTranslation.DUST_ADDITIONAL_SPECKS,
                    Binding.generic(ConfigHandler.particle_dust_additionalSpecks_DEFAULT, () -> ConfigHandler.particle_dust_additionalSpecks, newVal -> ConfigHandler.particle_dust_additionalSpecks = newVal)
                ),
                genericBooleanOption(
                    ConfigTranslation.DUST_ADDITIONAL_SPECKS,
                    Binding.generic(ConfigHandler.particle_dust_additionalSpecks_DEFAULT, () -> ConfigHandler.particle_dust_additionalSpecks, newVal -> ConfigHandler.particle_dust_additionalSpecks = newVal)
                )
            ))

            // debug
            .group( createGenericConfigGroup(
                "debug",
                ConfigTranslation.GENERAL_CATEGORY,
                true,
                ButtonOption.createBuilder()
                    .name( ConfigTranslation.getGlobalOption(ConfigTranslation.TOGGLE_INTERACTION_DEBUG_LOGS).toComponent() )
                    .description( OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.TOGGLE_INTERACTION_DEBUG_LOGS))) )
                    .action((yaclScreen, thisOption) -> ParticleInteractionsLogging.toggleInteractionDebugLogging())
                .build(),
                ButtonOption.createBuilder()
                    .name( ConfigTranslation.getGlobalOption(ConfigTranslation.TOGGLE_TEXTURE_DEBUG_LOGS).toComponent() )
                    .description( OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.TOGGLE_TEXTURE_DEBUG_LOGS))) )
                    .action((yaclScreen, thisOption) -> ParticleInteractionsLogging.toggleTextureDebugLogging())
                .build(),
                genericBooleanOption(
                    ConfigTranslation.DEBUG_SHOW_EMITTER_BOUNDS,
                    Binding.generic(ConfigHandler.debug_showEmitterBounds_DEFAULT, () -> ConfigHandler.debug_showEmitterBounds, newVal -> ConfigHandler.debug_showEmitterBounds = newVal)
                )
            ))
        .build());

        // block override category
        yaclBuilder.category( ConfigScreenHelper.createBlockParticleOverrideConfigWidgets(
            ConfigCategory.createBuilder()
                .name(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY).toComponent())
                .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY)))

                // vanilla block particles
                .group(
                    ConfigScreenHelper.createOptionsForBlockOverride(BlockParticleOverride.VANILLA)
                )
                .group(
                    createSeparator()
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

        // block ambient category
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
                ConfigScreen.createSeparator()
            )

            // underwater bubbles
            .group( createMultipleOptionsConfigGroup(
                "underwater_block_bubbles",
                "underwater_block_bubbles",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_PLACE, "underwater_block_bubbles", Binding.generic(ConfigHandler.underwaterBubbles_onPlace_DEFAULT, () -> ConfigHandler.underwaterBubbles_onPlace, newVal -> ConfigHandler.underwaterBubbles_onPlace = newVal)),
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE, "underwater_block_bubbles", ConfigHandler.maxUnderwaterBubbles_onPlace_DEFAULT, () -> ConfigHandler.maxUnderwaterBubbles_onPlace, newVal -> ConfigHandler.maxUnderwaterBubbles_onPlace = newVal, 1, 50, 1),
                booleanOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_BREAK, "underwater_block_bubbles", Binding.generic(ConfigHandler.underwaterBubbles_onBreak_DEFAULT, () -> ConfigHandler.underwaterBubbles_onBreak, newVal -> ConfigHandler.underwaterBubbles_onBreak = newVal)),
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK, "underwater_block_bubbles", ConfigHandler.maxUnderwaterBubbles_onBreak_DEFAULT, () -> ConfigHandler.maxUnderwaterBubbles_onBreak, newVal -> ConfigHandler.maxUnderwaterBubbles_onBreak = newVal, 1, 50, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // block rustle particles
            .group( createMultipleOptionsConfigGroup(
                "block_rustle",
                "block_rustle",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "block_rustle", Binding.generic(ConfigHandler.blockRustle_enabled_DEFAULT, () -> ConfigHandler.blockRustle_enabled, newVal -> ConfigHandler.blockRustle_enabled = newVal))
            ))
            .group(
                createBlockLocationListOption(
                    "block_rustle",
                    "block_rustle_blocks",
                    ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                    ConfigHandler.blockRustle_Blocks_DEFAULT,
                    () -> ConfigHandler.blockRustle_Blocks,
                    newVal -> ConfigHandler.blockRustle_Blocks = newVal
                )
            )

            .group(
                ConfigScreen.createSeparator()
            )

            // campfire ambient particles
            .group( createMultipleOptionsConfigGroup(
                "campfire_particles",
                "campfire_particles",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "sparks", Binding.generic(ConfigHandler.campfireSpark_enabled_DEFAULT, () -> ConfigHandler.campfireSpark_enabled, newVal -> ConfigHandler.campfireSpark_enabled = newVal)),
                integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "sparks", ConfigHandler.campfireSpark_spawnChance_DEFAULT, () -> ConfigHandler.campfireSpark_spawnChance, newVal -> ConfigHandler.campfireSpark_spawnChance = newVal, 1, 100, 1),
                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "embers", Binding.generic(ConfigHandler.campfireEmber_enabled_DEFAULT, () -> ConfigHandler.campfireEmber_enabled, newVal -> ConfigHandler.campfireEmber_enabled = newVal)),
                integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "embers", ConfigHandler.campfireEmber_spawnChance_DEFAULT, () -> ConfigHandler.campfireEmber_spawnChance, newVal -> ConfigHandler.campfireEmber_spawnChance = newVal, 1, 100, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // fire ambient particles
            .group( createMultipleOptionsConfigGroup(
                "fire_particles",
                "fire_particles",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "sparks", Binding.generic(ConfigHandler.fireSpark_enabled_DEFAULT, () -> ConfigHandler.fireSpark_enabled, newVal -> ConfigHandler.fireSpark_enabled = newVal)),
                integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "sparks", ConfigHandler.fireSpark_spawnChance_DEFAULT, () -> ConfigHandler.fireSpark_spawnChance, newVal -> ConfigHandler.fireSpark_spawnChance = newVal, 1, 100, 1),
                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "embers", Binding.generic(ConfigHandler.fireEmber_enabled_DEFAULT, () -> ConfigHandler.fireEmber_enabled, newVal -> ConfigHandler.fireEmber_enabled = newVal)),
                integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "embers", ConfigHandler.fireEmber_spawnChance_DEFAULT, () -> ConfigHandler.fireEmber_spawnChance, newVal -> ConfigHandler.fireEmber_spawnChance = newVal, 1, 100, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // anvil use spark
            .group( createParticleToggleAndIntSliderConfigGroup(
                "anvil_craft_sparks",
                "anvil_craft_sparks",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.anvilUseSparks_enabled_DEFAULT, () -> ConfigHandler.anvilUseSparks_enabled, newVal -> ConfigHandler.anvilUseSparks_enabled = newVal),
                ConfigTranslation.IS_PARTICLE_ENABLED,
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_CRAFT, "anvil_craft_sparks", ConfigHandler.maxAnvilUseSparks_onUse_DEFAULT, () -> ConfigHandler.maxAnvilUseSparks_onUse, newVal -> ConfigHandler.maxAnvilUseSparks_onUse = newVal, 1, 32, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // grindstone use spark
            .group( createParticleToggleAndIntSliderConfigGroup(
                "grindstone_craft_sparks",
                "grindstone_craft_sparks",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.grindstoneUseSparks_enabled_DEFAULT, () -> ConfigHandler.grindstoneUseSparks_enabled, newVal -> ConfigHandler.grindstoneUseSparks_enabled = newVal),
                ConfigTranslation.IS_PARTICLE_ENABLED,
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_CRAFT, "grindstone_craft_sparks", ConfigHandler.maxGrindstoneUseSparks_onUse_DEFAULT, () -> ConfigHandler.maxGrindstoneUseSparks_onUse, newVal -> ConfigHandler.maxGrindstoneUseSparks_onUse = newVal, 1, 32, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // falling block effect
            .group( createParticleToggleAndIntSliderConfigGroup(
                "falling_block_effect",
                "falling_block_effect",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.fallingBlockEffect_enabled_DEFAULT, () -> ConfigHandler.fallingBlockEffect_enabled, newVal -> ConfigHandler.fallingBlockEffect_enabled = newVal),
                ConfigTranslation.IS_PARTICLE_ENABLED,
                integerSliderOption(ConfigTranslation.PARTICLE_EFFECT_RENDER_DISTANCE, "falling_block_effect", ConfigHandler.fallingBlockEffect_renderDistance_DEFAULT, () -> ConfigHandler.fallingBlockEffect_renderDistance, newVal -> ConfigHandler.fallingBlockEffect_renderDistance = newVal, 1, 512, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // redstone interaction dust
            .group( createParticleToggleAndIntSliderConfigGroup(
                "redstone_interaction_dust",
                "redstone_interaction_dust",
                ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.redstoneInteractionDust_enabled_DEFAULT, () -> ConfigHandler.redstoneInteractionDust_enabled, newVal -> ConfigHandler.redstoneInteractionDust_enabled = newVal),
                ConfigTranslation.IS_PARTICLE_ENABLED,
                integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_INTERACT, "redstone_interaction_dust", ConfigHandler.redstoneInteractionDust_amount_DEFAULT, () -> ConfigHandler.redstoneInteractionDust_amount, newVal -> ConfigHandler.redstoneInteractionDust_amount = newVal, 1, 32, 1)
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
                ConfigScreen.createSeparator()
            )

            // brushing particles
            .group( createGenericConfigGroup(
                "brush_particles",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                false,
                enumCycleOption(ConfigTranslation.BRUSH_PARTICLE_BEHAVIOUR, Binding.generic(ConfigHandler.brushParticleBehaviour_DEFAULT, () -> ConfigHandler.brushParticleBehaviour, newVal -> ConfigHandler.brushParticleBehaviour = newVal), BrushParticleBehaviour.class)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // flint and steel spark
            .group( createParticleToggleAndMaxAndIntensityConfigGroup(
                "flint_and_steel_sparks",
                "flint_and_steel_sparks",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.flintAndSteelSpark_onUse_DEFAULT, () -> ConfigHandler.flintAndSteelSpark_onUse, newVal -> ConfigHandler.flintAndSteelSpark_onUse = newVal),
                ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE,
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "flint_and_steel_sparks", ConfigHandler.maxFlintAndSteelSpark_onUse_DEFAULT, () -> ConfigHandler.maxFlintAndSteelSpark_onUse, newVal -> ConfigHandler.maxFlintAndSteelSpark_onUse = newVal, 1, 32, 1),
                integerSliderOption(ConfigTranslation.ITEM_USE_PARTICLE_INTENSITY, "flint_and_steel_sparks", ConfigHandler.flintAndSteelSpark_intensity_DEFAULT, () -> ConfigHandler.flintAndSteelSpark_intensity, newVal -> ConfigHandler.flintAndSteelSpark_intensity = newVal, 1, 8, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // fire charge smoke
            .group( createParticleToggleAndMaxAndIntensityConfigGroup(
                "fire_charge_smoke",
                "fire_charge_smoke",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.fireCharge_onUse_DEFAULT, () -> ConfigHandler.fireCharge_onUse, newVal -> ConfigHandler.fireCharge_onUse = newVal),
                ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE,
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "fire_charge_smoke", ConfigHandler.maxFireCharge_onUse_DEFAULT, () -> ConfigHandler.maxFireCharge_onUse, newVal -> ConfigHandler.maxFireCharge_onUse = newVal, 1, 32, 1),
                integerSliderOption(ConfigTranslation.ITEM_USE_PARTICLE_INTENSITY, "fire_charge_smoke", ConfigHandler.fireCharge_intensity_DEFAULT, () -> ConfigHandler.fireCharge_intensity, newVal -> ConfigHandler.fireCharge_intensity = newVal, 1, 8, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // axe strip
            .group( createMultipleOptionsConfigGroup(
                "axe_strip_particles",
                "axe_strip_particles",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "axe_strip_particles", Binding.generic(ConfigHandler.axeStrip_onUse_DEFAULT, () -> ConfigHandler.axeStrip_onUse, newVal -> ConfigHandler.axeStrip_onUse = newVal)),
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "axe_strip_particles", ConfigHandler.maxAxeStrip_onUse_DEFAULT, () -> ConfigHandler.maxAxeStrip_onUse, newVal -> ConfigHandler.maxAxeStrip_onUse = newVal, 1, 50, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // hoe till
            .group( createMultipleOptionsConfigGroup(
                "hoe_till_particles",
                "hoe_till_particles",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "hoe_till_particles", Binding.generic(ConfigHandler.hoeTill_onUse_DEFAULT, () -> ConfigHandler.hoeTill_onUse, newVal -> ConfigHandler.hoeTill_onUse = newVal)),
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "hoe_till_particles", ConfigHandler.maxHoeTill_onUse_DEFAULT, () -> ConfigHandler.maxHoeTill_onUse, newVal -> ConfigHandler.maxHoeTill_onUse = newVal, 1, 50, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // shovel flatten
            .group( createMultipleOptionsConfigGroup(
                "shovel_flatten_particles",
                "shovel_flatten_particles",
                ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "shovel_flatten_particles", Binding.generic(ConfigHandler.shovelFlatten_onUse_DEFAULT, () -> ConfigHandler.shovelFlatten_onUse, newVal -> ConfigHandler.shovelFlatten_onUse = newVal)),
                integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "shovel_flatten_particles", ConfigHandler.maxShovelFlatten_onUse_DEFAULT, () -> ConfigHandler.maxShovelFlatten_onUse, newVal -> ConfigHandler.maxShovelFlatten_onUse = newVal, 1, 50, 1)
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
                ConfigScreen.createSeparator()
            )

            // minecart sparks at max speed
            .group( createMultipleOptionsConfigGroup(
                "minecart_sparks",
                "minecart_sparks",
                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.SPAWN_PARTICLE_WHEN_MINECART_AT_MAX_SPEED, "minecart_sparks", Binding.generic(ConfigHandler.minecart_enabled_DEFAULT, () -> ConfigHandler.minecart_enabled, newVal -> ConfigHandler.minecart_enabled = newVal)),
                integerSliderOption(ConfigTranslation.MINECART_WHEEL_PARTICLE_AMOUNT, "minecart_sparks", ConfigHandler.minecart_spawnChance_DEFAULT, () -> ConfigHandler.minecart_spawnChance, newVal -> ConfigHandler.minecart_spawnChance = newVal, 1, 100, 1),
                booleanOption(ConfigTranslation.MINECART_ONLY_WITH_PASSENGER, "minecart_sparks", Binding.generic(ConfigHandler.minecart_onlyWithPassenger_DEFAULT, () -> ConfigHandler.minecart_onlyWithPassenger, newVal -> ConfigHandler.minecart_onlyWithPassenger = newVal))
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // blaze sparks
            .group( createMultipleOptionsConfigGroup(
                "blaze_sparks",
                "blaze_sparks",
                ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
                integerSliderOption(ConfigTranslation.ENTITY_AMBIENT_PARTICLE_SPAWN_CHANCE, "blaze_sparks", ConfigHandler.blaze_spawnChance_DEFAULT, () -> ConfigHandler.blaze_spawnChance, newVal -> ConfigHandler.blaze_spawnChance = newVal, 1, 100, 1),
                booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ENTITY_HURT, "blaze_sparks", Binding.generic(ConfigHandler.blaze_spawnOnHurt_DEFAULT, () -> ConfigHandler.blaze_spawnOnHurt, newVal -> ConfigHandler.blaze_spawnOnHurt = newVal)),
                integerSliderOption(ConfigTranslation.AMOUNT_TO_SPAWN_ON_ENTITY_HURT, "blaze_sparks", ConfigHandler.blaze_amountToSpawnOnHurt_DEFAULT, () -> ConfigHandler.blaze_amountToSpawnOnHurt, newVal -> ConfigHandler.blaze_amountToSpawnOnHurt = newVal, 1, 32, 1)
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
                ConfigScreen.createSeparator()
            )

            // tinted water splash
            .group( createFluidParticleToggleAndMaxConfigGroup(
                "tinted_splash",
                "tinted_splash",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.tintedWaterSplash_onPlace_DEFAULT, () -> ConfigHandler.tintedWaterSplash_onPlace, newVal -> ConfigHandler.tintedWaterSplash_onPlace = newVal),
                maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, ConfigHandler.maxTintedWaterSplash_onPlace_DEFAULT, () -> ConfigHandler.maxTintedWaterSplash_onPlace, newVal -> ConfigHandler.maxTintedWaterSplash_onPlace = newVal)
            ))
            .group( createFluidListOption(
                "tinted_splash",
                "tinted_splash_fluids",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                ConfigHandler.tintedWaterSplash_fluids_DEFAULT, () -> ConfigHandler.tintedWaterSplash_fluids, newVal -> ConfigHandler.tintedWaterSplash_fluids = newVal
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // lava splash
            .group( createFluidParticleToggleAndMaxConfigGroup(
                "lava_splash",
                "generic_particle_fluids",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.lavaSplash_onPlace_DEFAULT, () -> ConfigHandler.lavaSplash_onPlace, newVal -> ConfigHandler.lavaSplash_onPlace = newVal),
                maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, ConfigHandler.maxLavaSplash_onPlace_DEFAULT, () -> ConfigHandler.maxLavaSplash_onPlace, newVal -> ConfigHandler.maxLavaSplash_onPlace = newVal)
            ))
            .group( createFluidListOption(
                "lava_splash",
                "generic_particle",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                ConfigHandler.lavaSplash_fluids_DEFAULT, () -> ConfigHandler.lavaSplash_fluids, newVal -> ConfigHandler.lavaSplash_fluids = newVal
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // generic splash
            .group( createFluidParticleToggleAndMaxConfigGroup(
                "generic_splash",
                "generic_splash",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                Binding.generic(ConfigHandler.genericSplash_onPlace_DEFAULT, () -> ConfigHandler.genericSplash_onPlace, newVal -> ConfigHandler.genericSplash_onPlace = newVal),
                maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, ConfigHandler.maxGenericSplash_onPlace_DEFAULT, () -> ConfigHandler.maxGenericSplash_onPlace, newVal -> ConfigHandler.maxGenericSplash_onPlace = newVal)
            ))
            .group( createFluidListOption(
                "generic_splash",
                "generic_splash_fluids",
                ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                ConfigHandler.genericSplash_fluids_DEFAULT, () -> ConfigHandler.genericSplash_fluids, newVal -> ConfigHandler.genericSplash_fluids = newVal
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
                ConfigScreen.createSeparator()
            )

            // lava bubble pop
            .group( createMultipleOptionsConfigGroup(
                "lava_bubble_pop",
                "lava_bubble_pop",
                ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "lava_bubble_pop", Binding.generic(ConfigHandler.lavaBubblePop_enabled_DEFAULT, () -> ConfigHandler.lavaBubblePop_enabled, newVal -> ConfigHandler.lavaBubblePop_enabled = newVal)),
                integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "lava_bubble_pop", ConfigHandler.lavaBubblePop_spawnChance_DEFAULT, () -> ConfigHandler.lavaBubblePop_spawnChance, newVal -> ConfigHandler.lavaBubblePop_spawnChance = newVal, 1, 100, 1)
            ))

            .group(
                ConfigScreen.createSeparator()
            )

            // underwater bubble streams
            .group( createMultipleOptionsConfigGroup(
                "underwater_bubble_streams",
                "underwater_bubble_streams",
                ConfigTranslation.FLUID_AMBIENT_CONFIG_CATEGORY,
                booleanOption(ConfigTranslation.IS_PARTICLE_ENABLED_WITH_TYPE, "underwater_bubble_streams", Binding.generic(ConfigHandler.underwaterBubbleStreams_enabled_DEFAULT, () -> ConfigHandler.underwaterBubbleStreams_enabled, newVal -> ConfigHandler.underwaterBubbleStreams_enabled = newVal)),
                integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE_WITH_TYPE, "underwater_bubble_streams", ConfigHandler.underwaterBubbleStreams_spawnChance_DEFAULT, () -> ConfigHandler.underwaterBubbleStreams_spawnChance, newVal -> ConfigHandler.underwaterBubbleStreams_spawnChance = newVal, 1, 100, 1)
            ))

        .build());

        return yaclBuilder.build();
    }

    public static OptionGroup createSeparator() {
        return OptionGroup.createBuilder()
            .name(Component.translatable("eg_particle_interactions.config.category_separator").withColor(0xff6c6c6c))
            .collapsed(true)
            .option(LabelOption.createBuilder().line(Component.empty()).build())
        .build();
    }

    private static OptionGroup createFluidParticleToggleAndMaxConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> spawnOnFluidPlaceBinding, Option<Integer> maxPlaceParticlesOption) {
        Option<Boolean> onFluidPlaceOption = Option.<Boolean>createBuilder()
            .name( ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_FLUID_PARTICLE_ON_PLACE).toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_FLUID_PARTICLE_ON_PLACE)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(spawnOnFluidPlaceBinding)
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
        .build();

        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, onFluidPlaceOption, maxPlaceParticlesOption);
    }

    private static OptionGroup createParticleToggleAndMaxAndIntensityConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> particleEnabledBinding, String particleEnabledTranslationOption, Option<Integer> maxParticlesOnUseOption, Option<Integer> particleIntensityOption) {
        Option<Boolean> particleToggleOption = booleanOption(particleEnabledTranslationOption, particleTypeKey, particleEnabledBinding);
        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, particleToggleOption, maxParticlesOnUseOption, particleIntensityOption);
    }

    private static OptionGroup createParticleToggleAndIntSliderConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> particleEnabledBinding, String particleEnabledTranslationOption, Option<Integer> intSlider) {
        Option<Boolean> particleToggleOption = booleanOption(particleEnabledTranslationOption, particleTypeKey, particleEnabledBinding);
        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, particleToggleOption, intSlider);
    }

    public static OptionGroup createMultipleOptionsConfigGroup(String particleTypeKey, String groupName, String category, Option<?> ...options) {
        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, false, options);
    }
    public static OptionGroup createMultipleOptionsConfigGroup(String particleTypeKey, String groupName, String category, boolean collapseByDefault, Option<?> ...options) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        OptionGroup.Builder optionGroupBuilder = OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ));
        for (Option<?> option : options) {
            optionGroupBuilder.option(option);
        }
        return optionGroupBuilder.collapsed(collapseByDefault).build();
    }

    public static OptionGroup createGenericConfigGroup(String groupName, String category, boolean collapseByDefault, Option<?> ...options) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        OptionGroup.Builder optionGroupBuilder = OptionGroup.createBuilder()
            .name( groupNameKey.toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createDesc(groupNameKey) ));
        for (Option<?> option : options) {
            optionGroupBuilder.option(option);
        }
        return optionGroupBuilder.collapsed(collapseByDefault).build();
    }

    private static Option<Boolean> booleanOption(String booleanOptionLabelText, String particleTypeKey, Binding<Boolean> binding) {
        return Option.<Boolean>createBuilder()
            .name( ConfigTranslation.createPlaceholder(ConfigTranslation.getGlobalOption(booleanOptionLabelText).toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(booleanOptionLabelText)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(binding)
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
        .build();
    }
    private static Option<Boolean> genericBooleanOption(String optionName, Binding<Boolean> binding) {
        ConfigTranslation.TranslationKey translationKey = ConfigTranslation.getGlobalOption(optionName);
        return Option.<Boolean>createBuilder()
            .name( translationKey.toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createDesc(translationKey) ))
            .binding(binding)
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
        .build();
    }
    @SuppressWarnings("unchecked")
    private static <T extends Enum> Option<T> enumCycleOption(String optionName, Binding<T> binding, Class<T> enumClass) {
        ConfigTranslation.TranslationKey translationKey = ConfigTranslation.getGlobalOption(optionName);
        return Option.<T>createBuilder()
            .name( translationKey.toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createDesc(translationKey) ))
            .binding(binding)
            .controller(
                opt -> EnumControllerBuilder.<T>create(opt)
                    .enumClass(enumClass)
            )
        .build();
    }

    public static Option<Integer> maxParticlesOnPlaceOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return integerSliderOption(optionName, maxParticlesDefault, getter, setter, 0, 16, 1);
    }
    public static Option<Integer> maxParticlesOnBreakOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return integerSliderOption(optionName, maxParticlesDefault, getter, setter, 0, 8, 1);
    }

    private static Option<Integer> integerSliderOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step) {
        return createIntegerOption(maxParticlesDefault, getter, setter, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), min, max, step);
    }
    private static Option<Integer> integerSliderOption(String optionName, String particleTypeKey, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step) {
        return createIntegerOption(maxParticlesDefault, getter, setter, ConfigTranslation.createPlaceholder(ConfigTranslation.getGlobalOption(optionName).toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ), ConfigTranslation.createPlaceholder( ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ), min, max, step);
    }

    private static Option<Integer> createIntegerOption(int defaultValue, Supplier<Integer> getter, Consumer<Integer> setter, Component name, Component description, int min, int max, int step) {
        return Option.<Integer>createBuilder()
            .name(name)
            .description(OptionDescription.of(description))
            .binding(defaultValue, getter, setter)
            .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
        .build();
    }

    public static ListOption<BlockOrTagLocation> createBlockLocationListOption(String particleTypeKey, String groupName, String category, List<BlockOrTagLocation> defaultValue, Supplier<List<BlockOrTagLocation>> getter, Consumer<List<BlockOrTagLocation>> setter) {
        return createListOption(new BlockOrTagLocation(RegistryHelpers.getLocationFromBlock(Blocks.STONE)), BlockLocationController::new, particleTypeKey, groupName, category, ConfigHandler.general_autoCollapseConfigLists, defaultValue, getter, setter);
    }
    private static ListOption<ResourceLocation> createFluidListOption(String particleTypeKey, String groupName, String category, List<ResourceLocation> defaultValue, Supplier<List<ResourceLocation>> getter, Consumer<List<ResourceLocation>> setter) {
        return createListOption(RegistryHelpers.getLocationFromFluid(Fluids.WATER), FluidLocationController::new, particleTypeKey, groupName, category, ConfigHandler.general_autoCollapseConfigLists, defaultValue, getter, setter);
    }
    private static <T> ListOption<T> createListOption(T initial, Function<ListOptionEntry<T>, Controller<T>> controller, String particleTypeKey, String groupName, String category, boolean collapsedByDefault, List<T> defaultValue, Supplier<List<T>> getter, Consumer<List<T>> setter) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return ListOption.<T>createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(defaultValue, getter, setter)
            .customController(controller)
            .collapsed(collapsedByDefault)
            .initial(initial)
        .build();
    }

}
