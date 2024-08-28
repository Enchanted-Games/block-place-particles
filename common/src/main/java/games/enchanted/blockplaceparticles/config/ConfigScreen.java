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

                // block config info
                .group(OptionGroup.createBuilder()
                    .name( ConfigTranslation.getGroupName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY, "info").toComponent() )
                    .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.BLOCKS_CONFIG_CATEGORY, "info")) ))
                    .collapsed(true)
                    .option(LabelOption.createBuilder()

                    .build())
                .build())

                // snowflake particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Snowflake"),
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.snowflake_onPlace_DEFAULT, () -> ConfigHandler.snowflake_onPlace, newVal -> ConfigHandler.snowflake_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxSnowflakes_onPlace_DEFAULT, () -> ConfigHandler.maxSnowflakes_onPlace, newVal -> ConfigHandler.maxSnowflakes_onPlace = newVal),
                    Binding.generic(ConfigHandler.snowflake_onBreak_DEFAULT, () -> ConfigHandler.snowflake_onBreak, newVal -> ConfigHandler.snowflake_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxSnowflakes_onBreak_DEFAULT, () -> ConfigHandler.maxSnowflakes_onBreak, newVal -> ConfigHandler.maxSnowflakes_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Snowflake"),
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.snowflake_BlockItems_DEFAULT, () -> ConfigHandler.snowflake_BlockItems, newVal -> ConfigHandler.snowflake_BlockItems = newVal
                ))

                // cherry petal particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Cherry Petal"),
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.cherryPetal_onPlace_DEFAULT, () -> ConfigHandler.cherryPetal_onPlace, newVal -> ConfigHandler.cherryPetal_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxCherryPetals_onPlace_DEFAULT, () -> ConfigHandler.maxCherryPetals_onPlace, newVal -> ConfigHandler.maxCherryPetals_onPlace = newVal),
                    Binding.generic(ConfigHandler.cherryPetal_onBreak_DEFAULT, () -> ConfigHandler.cherryPetal_onBreak, newVal -> ConfigHandler.cherryPetal_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxCherryPetals_onBreak_DEFAULT, () -> ConfigHandler.maxCherryPetals_onBreak, newVal -> ConfigHandler.maxCherryPetals_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Cherry Petal"),
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.cherryPetal_BlockItems_DEFAULT, () -> ConfigHandler.cherryPetal_BlockItems, newVal -> ConfigHandler.cherryPetal_BlockItems = newVal
                ))

                // azalea leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Azalea Leaf"),
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.azaleaLeaf_onPlace_DEFAULT, () -> ConfigHandler.azaleaLeaf_onPlace, newVal -> ConfigHandler.azaleaLeaf_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxAzaleaLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxAzaleaLeaves_onPlace, newVal -> ConfigHandler.maxAzaleaLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.azaleaLeaf_onBreak_DEFAULT, () -> ConfigHandler.azaleaLeaf_onBreak, newVal -> ConfigHandler.azaleaLeaf_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxAzaleaLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxAzaleaLeaves_onBreak, newVal -> ConfigHandler.maxAzaleaLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Azalea Leaf"),
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.azaleaLeaf_BlockItems_DEFAULT, () -> ConfigHandler.azaleaLeaf_BlockItems, newVal -> ConfigHandler.azaleaLeaf_BlockItems = newVal
                ))

                // flowering azalea leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Flowering Azalea Leaf"),
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.floweringAzaleaLeaf_onPlace_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_onPlace, newVal -> ConfigHandler.floweringAzaleaLeaf_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxFloweringAzaleaLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace, newVal -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.floweringAzaleaLeaf_onBreak_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_onBreak, newVal -> ConfigHandler.floweringAzaleaLeaf_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxFloweringAzaleaLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak, newVal -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Flowering Azalea Leaf"),
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.floweringAzaleaLeaf_BlockItems_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_BlockItems, newVal -> ConfigHandler.floweringAzaleaLeaf_BlockItems = newVal
                ))

                // tinted leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Biome Coloured Leaf"),
                    "biome_leaf",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.tintedLeaves_onPlace_DEFAULT, () -> ConfigHandler.tintedLeaves_onPlace, newVal -> ConfigHandler.tintedLeaves_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxTintedLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxTintedLeaves_onPlace, newVal -> ConfigHandler.maxTintedLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.tintedLeaves_onBreak_DEFAULT, () -> ConfigHandler.tintedLeaves_onBreak, newVal -> ConfigHandler.tintedLeaves_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxTintedLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxTintedLeaves_onBreak, newVal -> ConfigHandler.maxTintedLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Biome Coloured Leaf"),
                    "biome_leaf_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.tintedLeaves_BlockItems_DEFAULT, () -> ConfigHandler.tintedLeaves_BlockItems, newVal -> ConfigHandler.tintedLeaves_BlockItems = newVal
                ))

                // vanilla block particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Vanilla Block"),
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.block_onPlace_DEFAULT, () -> ConfigHandler.block_onPlace, newVal -> ConfigHandler.block_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_OPTION, ConfigHandler.maxBlock_onPlace_DEFAULT, () -> ConfigHandler.maxBlock_onPlace, newVal -> ConfigHandler.maxBlock_onPlace = newVal),
                    Binding.generic(ConfigHandler.block_onBreak_DEFAULT, () -> ConfigHandler.block_onBreak, newVal -> ConfigHandler.block_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_OPTION, ConfigHandler.maxBlock_onBreak_DEFAULT, () -> ConfigHandler.maxBlock_onBreak, newVal -> ConfigHandler.maxBlock_onBreak = newVal)
                ))

            .build())

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
                    Component.literal("Biome Tinted Splash"),
                    "tinted_splash",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.tintedWaterSplash_onPlace_DEFAULT, () -> ConfigHandler.tintedWaterSplash_onPlace, newVal -> ConfigHandler.tintedWaterSplash_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE_OPTION, ConfigHandler.maxTintedWaterSplash_onPlace_DEFAULT, () -> ConfigHandler.maxTintedWaterSplash_onPlace, newVal -> ConfigHandler.maxTintedWaterSplash_onPlace = newVal)
                ))
                .group( createFluidListOption(
                    Component.literal("Biome Tinted Splash"),
                    "tinted_splash_fluids",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    ConfigHandler.tintedWaterSplash_fluids_DEFAULT, () -> ConfigHandler.tintedWaterSplash_fluids, newVal -> ConfigHandler.tintedWaterSplash_fluids = newVal
                ))

                // lava splash
                .group( createFluidParticleToggleAndMaxConfigGroup(
                    Component.literal("Lava Splash"),
                    "generic_particle_fluids",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.lavaSplash_onPlace_DEFAULT, () -> ConfigHandler.lavaSplash_onPlace, newVal -> ConfigHandler.lavaSplash_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE_OPTION, ConfigHandler.maxLavaSplash_onPlace_DEFAULT, () -> ConfigHandler.maxLavaSplash_onPlace, newVal -> ConfigHandler.maxLavaSplash_onPlace = newVal)
                ))
                .group( createFluidListOption(
                    Component.literal("Lava Splash"),
                    "generic_particle",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    ConfigHandler.lavaSplash_fluids_DEFAULT, () -> ConfigHandler.lavaSplash_fluids, newVal -> ConfigHandler.lavaSplash_fluids = newVal
                ))

                // generic splash
                .group( createFluidParticleToggleAndMaxConfigGroup(
                    Component.literal("Generic Splash"),
                    "generic_splash",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.genericSplash_onPlace_DEFAULT, () -> ConfigHandler.genericSplash_onPlace, newVal -> ConfigHandler.genericSplash_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE_OPTION, ConfigHandler.maxGenericSplash_onPlace_DEFAULT, () -> ConfigHandler.maxGenericSplash_onPlace, newVal -> ConfigHandler.maxGenericSplash_onPlace = newVal)
                ))
                .group( createFluidListOption(
                    Component.literal("Generic Splash"),
                    "generic_splash_fluids",
                    ConfigTranslation.FLUIDS_CONFIG_CATEGORY,
                    ConfigHandler.genericSplash_fluids_DEFAULT, () -> ConfigHandler.genericSplash_fluids, newVal -> ConfigHandler.genericSplash_fluids = newVal
                ))

            .build())

        .build()
        .generateScreen(parentScreen);
    }

    private static OptionGroup createFluidParticleToggleAndMaxConfigGroup(Component particleName, String groupName, String category, Binding<Boolean> spawnOnFluidPlaceBinding, Option<Integer> maxPlaceParticlesOption) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), particleName.getString()) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), particleName.getString()) ))
            .option(Option.<Boolean>createBuilder()
                .name( ConfigTranslation.getGlobalOption("spawn_fluid_particle_on_place").toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption("spawn_fluid_particle_on_place")), particleName.getString()) ))
                .binding(spawnOnFluidPlaceBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .option(maxPlaceParticlesOption)
            .build();
    }

    private static OptionGroup createParticleToggleAndMaxConfigGroup(Component particleName, String groupName, String category, Binding<Boolean> spawnOnBlockPlaceBinding, Option<Integer> maxPlaceParticlesOption, Binding<Boolean> spawnOnBlockBreakBinding, Option<Integer> maxBreakParticlesOption) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), particleName.getString()) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), particleName.getString()) ))
            .option(Option.<Boolean>createBuilder()
                .name( ConfigTranslation.getGlobalOption("spawn_block_particle_on_place").toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption("spawn_block_particle_on_place")), particleName.getString()) ))
                .binding(spawnOnBlockPlaceBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build())
            .option(maxPlaceParticlesOption)
            .option(Option.<Boolean>createBuilder()
                .name( ConfigTranslation.getGlobalOption("spawn_block_particle_on_break").toComponent() )
                .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption("spawn_block_particle_on_break")), particleName.getString()) ))
                .binding(spawnOnBlockBreakBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build())
            .option(maxBreakParticlesOption)
        .build();
    }

    private static Option<Integer> maxParticlesOnPlaceOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return createIntegerOption(maxParticlesDefault, getter, setter, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), 1, 16, 1);
    }
    private static Option<Integer> maxParticlesOnBreakOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return createIntegerOption(maxParticlesDefault, getter, setter, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), 2, 8, 1);
    }
    private static Option<Integer> createIntegerOption(int defaultValue, Supplier<Integer> getter, Consumer<Integer> setter, Component name, Component description, int min, int max, int step) {
        return Option.<Integer>createBuilder()
            .name(name)
            .description(OptionDescription.of(description))
            .binding(defaultValue, getter, setter)
            .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
        .build();
    }

    private static ListOption<BlockItem> createBlockItemListOption(Component particleName, String groupName, String category, List<BlockItem> defaultValue, Supplier<List<BlockItem>> getter, Consumer<List<BlockItem>> setter) {
        return createListOption((BlockItem) Items.STONE, BlockItemController::new, particleName, groupName, category, defaultValue, getter, setter);
    }
    private static ListOption<Fluid> createFluidListOption(Component particleName, String groupName, String category, List<Fluid> defaultValue, Supplier<List<Fluid>> getter, Consumer<List<Fluid>> setter) {
        return createListOption(Fluids.WATER, FluidController::new, particleName, groupName, category, defaultValue, getter, setter);
    }
    private static <T> ListOption<T> createListOption(T initial, Function<ListOptionEntry<T>, Controller<T>> controller, Component particleName, String groupName, String category, List<T> defaultValue, Supplier<List<T>> getter, Consumer<List<T>> setter) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return ListOption.<T>createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), particleName.getString()) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), particleName.getString()) ))
            .binding(defaultValue, getter, setter)
            .customController(controller)
            .initial(initial)
        .build();
    }

}
