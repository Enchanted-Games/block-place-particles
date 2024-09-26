package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import games.enchanted.blockplaceparticles.config.controller.BlockItemController;
import games.enchanted.blockplaceparticles.config.controller.FluidController;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigScreen {
    public static Screen createConfigScreen(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
            .save(ConfigHandler::save)
            .title(ConfigTranslation.getConfigTitle().toComponent())

            .category(ConfigCategory.createBuilder()
                .name(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY).toComponent())
                .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY)))

                // underwater bubbles
                .group( createParticleToggleAndIntSliderConfigGroup(
                    "underwater_placement_bubbles",
                    "underwater_placement_bubbles",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.underwaterBubbles_enabled_DEFAULT, () -> ConfigHandler.underwaterBubbles_enabled, newVal -> ConfigHandler.underwaterBubbles_enabled = newVal),
                    ConfigTranslation.ARE_PARTICLES_ENABLED,
                    integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, "underwater_placement_bubbles", ConfigHandler.maxUnderwaterBubbles_onPlace_DEFAULT, () -> ConfigHandler.maxUnderwaterBubbles_onPlace, newVal -> ConfigHandler.maxUnderwaterBubbles_onPlace = newVal, 1, 100, 1)
                ))

                .group(OptionGroup.createBuilder()
                    .name( Component.literal("--------------") )
                    .collapsed(true)
                    .option(LabelOption.createBuilder()
                    .build())
                .build())

                // block config info
                .group(OptionGroup.createBuilder()
                    .name( ConfigTranslation.getGroupName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY, "info").toComponent() )
                    .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY, "info")) ))
                    .collapsed(true)
                    .option(LabelOption.createBuilder()

                    .build())
                .build())

                .group(OptionGroup.createBuilder()
                    .name( Component.literal("--------------") )
                    .collapsed(true)
                    .option(LabelOption.createBuilder()
                    .build())
                .build())

                // snowflake particles
                .group( createParticleToggleAndMaxConfigGroup(
                    "snowflake",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.snowflake_onPlace_DEFAULT, () -> ConfigHandler.snowflake_onPlace, newVal -> ConfigHandler.snowflake_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxSnowflakes_onPlace_DEFAULT, () -> ConfigHandler.maxSnowflakes_onPlace, newVal -> ConfigHandler.maxSnowflakes_onPlace = newVal),
                    Binding.generic(ConfigHandler.snowflake_onBreak_DEFAULT, () -> ConfigHandler.snowflake_onBreak, newVal -> ConfigHandler.snowflake_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxSnowflakes_onBreak_DEFAULT, () -> ConfigHandler.maxSnowflakes_onBreak, newVal -> ConfigHandler.maxSnowflakes_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    "snowflake",
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.snowflake_BlockItems_DEFAULT, () -> ConfigHandler.snowflake_BlockItems, newVal -> ConfigHandler.snowflake_BlockItems = newVal
                ))

                // cherry petal particles
                .group( createParticleToggleAndMaxConfigGroup(
                    "cherry_petal",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.cherryPetal_onPlace_DEFAULT, () -> ConfigHandler.cherryPetal_onPlace, newVal -> ConfigHandler.cherryPetal_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxCherryPetals_onPlace_DEFAULT, () -> ConfigHandler.maxCherryPetals_onPlace, newVal -> ConfigHandler.maxCherryPetals_onPlace = newVal),
                    Binding.generic(ConfigHandler.cherryPetal_onBreak_DEFAULT, () -> ConfigHandler.cherryPetal_onBreak, newVal -> ConfigHandler.cherryPetal_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxCherryPetals_onBreak_DEFAULT, () -> ConfigHandler.maxCherryPetals_onBreak, newVal -> ConfigHandler.maxCherryPetals_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    "cherry_petal",
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.cherryPetal_BlockItems_DEFAULT, () -> ConfigHandler.cherryPetal_BlockItems, newVal -> ConfigHandler.cherryPetal_BlockItems = newVal
                ))

                // azalea leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    "azalea_leaf",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.azaleaLeaf_onPlace_DEFAULT, () -> ConfigHandler.azaleaLeaf_onPlace, newVal -> ConfigHandler.azaleaLeaf_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxAzaleaLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxAzaleaLeaves_onPlace, newVal -> ConfigHandler.maxAzaleaLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.azaleaLeaf_onBreak_DEFAULT, () -> ConfigHandler.azaleaLeaf_onBreak, newVal -> ConfigHandler.azaleaLeaf_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxAzaleaLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxAzaleaLeaves_onBreak, newVal -> ConfigHandler.maxAzaleaLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    "azalea_leaf",
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.azaleaLeaf_BlockItems_DEFAULT, () -> ConfigHandler.azaleaLeaf_BlockItems, newVal -> ConfigHandler.azaleaLeaf_BlockItems = newVal
                ))

                // flowering azalea leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    "flowering_azalea_leaf",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.floweringAzaleaLeaf_onPlace_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_onPlace, newVal -> ConfigHandler.floweringAzaleaLeaf_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxFloweringAzaleaLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace, newVal -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.floweringAzaleaLeaf_onBreak_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_onBreak, newVal -> ConfigHandler.floweringAzaleaLeaf_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxFloweringAzaleaLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak, newVal -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    "flowering_azalea_leaf",
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.floweringAzaleaLeaf_BlockItems_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_BlockItems, newVal -> ConfigHandler.floweringAzaleaLeaf_BlockItems = newVal
                ))

                // tinted leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    "biome_leaf",
                    "biome_leaf",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.tintedLeaves_onPlace_DEFAULT, () -> ConfigHandler.tintedLeaves_onPlace, newVal -> ConfigHandler.tintedLeaves_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxTintedLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxTintedLeaves_onPlace, newVal -> ConfigHandler.maxTintedLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.tintedLeaves_onBreak_DEFAULT, () -> ConfigHandler.tintedLeaves_onBreak, newVal -> ConfigHandler.tintedLeaves_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxTintedLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxTintedLeaves_onBreak, newVal -> ConfigHandler.maxTintedLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    "biome_leaf",
                    "biome_leaf_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.tintedLeaves_BlockItems_DEFAULT, () -> ConfigHandler.tintedLeaves_BlockItems, newVal -> ConfigHandler.tintedLeaves_BlockItems = newVal
                ))

                // vanilla block particles
                .group( createParticleToggleAndMaxConfigGroup(
                    "vanilla_particle",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.block_onPlace_DEFAULT, () -> ConfigHandler.block_onPlace, newVal -> ConfigHandler.block_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxBlock_onPlace_DEFAULT, () -> ConfigHandler.maxBlock_onPlace, newVal -> ConfigHandler.maxBlock_onPlace = newVal),
                    Binding.generic(ConfigHandler.block_onBreak_DEFAULT, () -> ConfigHandler.block_onBreak, newVal -> ConfigHandler.block_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxBlock_onBreak_DEFAULT, () -> ConfigHandler.maxBlock_onBreak, newVal -> ConfigHandler.maxBlock_onBreak = newVal)
                ))

            .build())

            // block ambient category
            .category(ConfigCategory.createBuilder()
                .name(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY).toComponent())
                .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY)))

                // category info
                .group(OptionGroup.createBuilder()
                    .name( ConfigTranslation.getGroupName(ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY, "info").toComponent() )
                    .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY, "info")) ))
                    .collapsed(true)
                    .option(LabelOption.createBuilder()

                    .build())
                .build())

                // campfire spark
                .group( createParticleToggleAndIntSliderConfigGroup(
                    "campfire_sparks",
                    "campfire_sparks",
                    ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.campfireSpark_enabled_DEFAULT, () -> ConfigHandler.campfireSpark_enabled, newVal -> ConfigHandler.campfireSpark_enabled = newVal),
                    ConfigTranslation.ARE_PARTICLES_ENABLED,
                    integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE, "campfire_sparks", ConfigHandler.campfireSpark_spawnChance_DEFAULT, () -> ConfigHandler.campfireSpark_spawnChance, newVal -> ConfigHandler.campfireSpark_spawnChance = newVal, 1, 100, 1)
                ))

                // fire spark
                .group( createParticleToggleAndIntSliderConfigGroup(
                    "fire_sparks",
                    "fire_sparks",
                    ConfigTranslation.BLOCK_AMBIENT_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.fireSpark_enabled_DEFAULT, () -> ConfigHandler.fireSpark_enabled, newVal -> ConfigHandler.fireSpark_enabled = newVal),
                    ConfigTranslation.ARE_PARTICLES_ENABLED,
                    integerSliderOption(ConfigTranslation.PARTICLE_SPAWN_CHANCE, "fire_sparks", ConfigHandler.fireSpark_spawnChance_DEFAULT, () -> ConfigHandler.fireSpark_spawnChance, newVal -> ConfigHandler.fireSpark_spawnChance = newVal, 1, 100, 1)
                ))

            .build())

            // item use category
            .category(ConfigCategory.createBuilder()
                .name(ConfigTranslation.getCategoryName(ConfigTranslation.ITEMS_CONFIG_CATEGORY).toComponent())
                .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.ITEMS_CONFIG_CATEGORY)))

                // item config info
                .group(OptionGroup.createBuilder()
                    .name( ConfigTranslation.getGroupName(ConfigTranslation.ITEMS_CONFIG_CATEGORY, "info").toComponent() )
                    .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.ITEMS_CONFIG_CATEGORY, "info")) ))
                    .collapsed(true)
                    .option(LabelOption.createBuilder()

                    .build())
                .build())

                // flint and steel spark
                .group( createParticleToggleAndMaxAndIntensityConfigGroup(
                    "flint_and_steel_sparks",
                    "flint_and_steel_sparks",
                    ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.flintAndSteelSpark_onUse_DEFAULT, () -> ConfigHandler.flintAndSteelSpark_onUse, newVal -> ConfigHandler.flintAndSteelSpark_onUse = newVal),
                    ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE,
                    integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE_OPTION, "flint_and_steel_sparks", ConfigHandler.maxFlintAndSteelSpark_onUse_DEFAULT, () -> ConfigHandler.maxFlintAndSteelSpark_onUse, newVal -> ConfigHandler.maxFlintAndSteelSpark_onUse = newVal, 1, 16, 1),
                    integerSliderOption(ConfigTranslation.ITEM_USE_PARTICLE_INTENSITY, "flint_and_steel_sparks", ConfigHandler.flintAndSteelSpark_intensity_DEFAULT, () -> ConfigHandler.flintAndSteelSpark_intensity, newVal -> ConfigHandler.flintAndSteelSpark_intensity = newVal, 1, 8, 1)
                ))

            .build())


            // fluid config category
            .category(ConfigCategory.createBuilder()
                .name(ConfigTranslation.getCategoryName(ConfigTranslation.FLUIDS_CONFIG_CATEGORY).toComponent())
                .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.FLUIDS_CONFIG_CATEGORY)))

                // fluid config info
                .group(OptionGroup.createBuilder()
                    .name( ConfigTranslation.getGroupName(ConfigTranslation.FLUIDS_CONFIG_CATEGORY, "info").toComponent() )
                    .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.FLUIDS_CONFIG_CATEGORY, "info")) ))
                    .collapsed(true)
                    .option(LabelOption.createBuilder()

                    .build())
                .build())

                // tinted water splash
                .group( createFluidParticleToggleAndMaxConfigGroup(
                    "tinted_splash",
                    "tinted_splash",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.tintedWaterSplash_onPlace_DEFAULT, () -> ConfigHandler.tintedWaterSplash_onPlace, newVal -> ConfigHandler.tintedWaterSplash_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE_OPTION, ConfigHandler.maxTintedWaterSplash_onPlace_DEFAULT, () -> ConfigHandler.maxTintedWaterSplash_onPlace, newVal -> ConfigHandler.maxTintedWaterSplash_onPlace = newVal)
                ))
                .group( createFluidListOption(
                    "tinted_splash",
                    "tinted_splash_fluids",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    ConfigHandler.tintedWaterSplash_fluids_DEFAULT, () -> ConfigHandler.tintedWaterSplash_fluids, newVal -> ConfigHandler.tintedWaterSplash_fluids = newVal
                ))

                // lava splash
                .group( createFluidParticleToggleAndMaxConfigGroup(
                    "lava_splash",
                    "generic_particle_fluids",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.lavaSplash_onPlace_DEFAULT, () -> ConfigHandler.lavaSplash_onPlace, newVal -> ConfigHandler.lavaSplash_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE_OPTION, ConfigHandler.maxLavaSplash_onPlace_DEFAULT, () -> ConfigHandler.maxLavaSplash_onPlace, newVal -> ConfigHandler.maxLavaSplash_onPlace = newVal)
                ))
                .group( createFluidListOption(
                    "lava_splash",
                    "generic_particle",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    ConfigHandler.lavaSplash_fluids_DEFAULT, () -> ConfigHandler.lavaSplash_fluids, newVal -> ConfigHandler.lavaSplash_fluids = newVal
                ))

                // generic splash
                .group( createFluidParticleToggleAndMaxConfigGroup(
                    "generic_splash",
                    "generic_splash",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.genericSplash_onPlace_DEFAULT, () -> ConfigHandler.genericSplash_onPlace, newVal -> ConfigHandler.genericSplash_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE_OPTION, ConfigHandler.maxGenericSplash_onPlace_DEFAULT, () -> ConfigHandler.maxGenericSplash_onPlace, newVal -> ConfigHandler.maxGenericSplash_onPlace = newVal)
                ))
                .group( createFluidListOption(
                    "generic_splash",
                    "generic_splash_fluids",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    ConfigHandler.genericSplash_fluids_DEFAULT, () -> ConfigHandler.genericSplash_fluids, newVal -> ConfigHandler.genericSplash_fluids = newVal
                ))

            .build())


            .build()
        .generateScreen(parentScreen);
    }

    private static OptionGroup createFluidParticleToggleAndMaxConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> spawnOnFluidPlaceBinding, Option<Integer> maxPlaceParticlesOption) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .option(Option.<Boolean>createBuilder()
                .name( ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_FLUID_PARTICLE_ON_PLACE).toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_FLUID_PARTICLE_ON_PLACE)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
                .binding(spawnOnFluidPlaceBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .option(maxPlaceParticlesOption)
            .build();
    }

    private static OptionGroup createParticleToggleAndMaxConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> spawnOnBlockPlaceBinding, Option<Integer> maxPlaceParticlesOption, Binding<Boolean> spawnOnBlockBreakBinding, Option<Integer> maxBreakParticlesOption) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .option(Option.<Boolean>createBuilder()
                .name( ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_PLACE).toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_PLACE)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
                .binding(spawnOnBlockPlaceBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build())
            .option(maxPlaceParticlesOption)
            .option(Option.<Boolean>createBuilder()
                .name( ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_BREAK).toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_BREAK)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
                .binding(spawnOnBlockBreakBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build())
            .option(maxBreakParticlesOption)
        .build();
    }

    private static OptionGroup createParticleToggleAndMaxAndIntensityConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> particleEnabledBinding, String particleEnabledTranslationOption, Option<Integer> maxParticlesOnUseOption, Option<Integer> particleIntensityOption) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .option(Option.<Boolean>createBuilder()
                .name( ConfigTranslation.getGlobalOption(particleEnabledTranslationOption).toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(particleEnabledTranslationOption)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
                .binding(particleEnabledBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build())
            .option(maxParticlesOnUseOption)
            .option(particleIntensityOption)
        .build();
    }

    private static OptionGroup createParticleToggleAndIntSliderConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> particleEnabledBinding, String particleEnabledTranslationOption, Option<Integer> intSlider) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .option(Option.<Boolean>createBuilder()
                .name( ConfigTranslation.getGlobalOption(particleEnabledTranslationOption).toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(particleEnabledTranslationOption)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
                .binding(particleEnabledBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build())
            .option(intSlider)
        .build();
    }

    private static Option<Integer> integerSliderOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step) {
        return createIntegerOption(maxParticlesDefault, getter, setter, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), min, max, step);
    }
    private static Option<Integer> integerSliderOption(String optionName, String particleTypeKey, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step) {
        return createIntegerOption(maxParticlesDefault, getter, setter, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createPlaceholder( ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ), min, max, step);
    }
    private static Option<Integer> maxParticlesOnPlaceOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return integerSliderOption(optionName, maxParticlesDefault, getter, setter, 1, 16, 1);
    }
    private static Option<Integer> maxParticlesOnBreakOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return integerSliderOption(optionName, maxParticlesDefault, getter, setter, 2, 8, 1);
    }

    private static Option<Integer> createIntegerOption(int defaultValue, Supplier<Integer> getter, Consumer<Integer> setter, Component name, Component description, int min, int max, int step) {
        return Option.<Integer>createBuilder()
            .name(name)
            .description(OptionDescription.of(description))
            .binding(defaultValue, getter, setter)
            .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
        .build();
    }

    private static ListOption<BlockItem> createBlockItemListOption(String particleTypeKey, String groupName, String category, List<BlockItem> defaultValue, Supplier<List<BlockItem>> getter, Consumer<List<BlockItem>> setter) {
        return createListOption((BlockItem) Items.STONE, BlockItemController::new, particleTypeKey, groupName, category, defaultValue, getter, setter);
    }
    private static ListOption<Fluid> createFluidListOption(String particleTypeKey, String groupName, String category, List<Fluid> defaultValue, Supplier<List<Fluid>> getter, Consumer<List<Fluid>> setter) {
        return createListOption(Fluids.WATER, FluidController::new, particleTypeKey, groupName, category, defaultValue, getter, setter);
    }
    private static <T> ListOption<T> createListOption(T initial, Function<ListOptionEntry<T>, Controller<T>> controller, String particleTypeKey, String groupName, String category, List<T> defaultValue, Supplier<List<T>> getter, Consumer<List<T>> setter) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return ListOption.<T>createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(defaultValue, getter, setter)
            .customController(controller)
            .initial(initial)
        .build();
    }

}
