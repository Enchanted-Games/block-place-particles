package games.enchanted.eg_particle_interactions.common.config2.screen;

import dev.isxander.yacl3.api.*;
import games.enchanted.eg_particle_interactions.common.config.ConfigHandler;
import games.enchanted.eg_particle_interactions.common.config2.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config2.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen {
    public static Screen createConfigScreen(Screen parentScreen) {
        YetAnotherConfigLib.Builder yaclBuilder = YetAnotherConfigLib.createBuilder()
            .save(ConfigHandler::save)
            .title(ConfigTranslation.getConfigTitle().toComponent());
        return createConfigCategories(yaclBuilder).generateScreen(parentScreen);
    }

    private static YetAnotherConfigLib createConfigCategories(YetAnotherConfigLib.Builder yaclBuilder) {
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

        yaclBuilder.save(ConfigOptions::applyAndSaveConfig);
        return yaclBuilder.build();
    }
}
