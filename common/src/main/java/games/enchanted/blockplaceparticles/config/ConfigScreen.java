package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.gui.controllers.dropdown.ItemController;
import games.enchanted.blockplaceparticles.config.controller.BlockItemController;
import games.enchanted.blockplaceparticles.config.controller.FluidController;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
            .title(Component.literal("placeholder config title"))
            .category(ConfigCategory.createBuilder()
                .name(Component.literal("Block Particles"))
                .tooltip(Component.literal("Configure how particles are emitted when placing or breaking blocks"))

                // testing
                .group(OptionGroup.createBuilder()
                    .name(Component.literal("test group"))
                    .description(OptionDescription.of(Component.literal("This text will appear when you hover over the name or focus on the collapse button with Tab.")))
                    .option(Option.<BlockItem>createBuilder()
                        .name(Component.literal("Block Item"))
                        .description(OptionDescription.of(Component.literal("Choose a block or an item that can be used to place a block (eg. seeds, signs, saplings)")))
                        .binding((BlockItem) Items.SNOW, () -> ConfigHandler.testValue, newVal -> ConfigHandler.testValue = newVal)
                        .customController(BlockItemController::new)
                    .build())
                    .option(Option.<Item>createBuilder()
                        .name(Component.literal("Item"))
                        .description(OptionDescription.of(Component.literal("Choose a block or an item that can be used to place a block (eg. seeds, signs, saplings)")))
                        .binding(Items.SNOW_BLOCK, () -> ConfigHandler.testValueItem, newVal -> ConfigHandler.testValueItem = newVal)
                        .customController(ItemController::new)
                    .build())
                .build())

                // config info
                .group(OptionGroup.createBuilder()
                    .name(Component.literal("-- Information --"))
                    .description(OptionDescription.of(Component.literal("infotext")))
                    .collapsed(true)
                    .option(LabelOption.createBuilder()

                    .build())
                .build())

                // snowflake particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Snowflake"),
                    Binding.generic(ConfigHandler.snowflake_onPlace_DEFAULT, () -> ConfigHandler.snowflake_onPlace, newVal -> ConfigHandler.snowflake_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigHandler.maxSnowflakes_onPlace_DEFAULT, () -> ConfigHandler.maxSnowflakes_onPlace, newVal -> ConfigHandler.maxSnowflakes_onPlace = newVal),
                    Binding.generic(ConfigHandler.snowflake_onBreak_DEFAULT, () -> ConfigHandler.snowflake_onBreak, newVal -> ConfigHandler.snowflake_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigHandler.maxSnowflakes_onBreak_DEFAULT, () -> ConfigHandler.maxSnowflakes_onBreak, newVal -> ConfigHandler.maxSnowflakes_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Snowflake"),
                    ConfigHandler.snowflake_BlockItems_DEFAULT, () -> ConfigHandler.snowflake_BlockItems, newVal -> ConfigHandler.snowflake_BlockItems = newVal
                ))

                // cherry petal particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Cherry Petal"),
                    Binding.generic(ConfigHandler.cherryPetal_onPlace_DEFAULT, () -> ConfigHandler.cherryPetal_onPlace, newVal -> ConfigHandler.cherryPetal_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigHandler.maxCherryPetals_onPlace_DEFAULT, () -> ConfigHandler.maxCherryPetals_onPlace, newVal -> ConfigHandler.maxCherryPetals_onPlace = newVal),
                    Binding.generic(ConfigHandler.cherryPetal_onBreak_DEFAULT, () -> ConfigHandler.cherryPetal_onBreak, newVal -> ConfigHandler.cherryPetal_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigHandler.maxCherryPetals_onBreak_DEFAULT, () -> ConfigHandler.maxCherryPetals_onBreak, newVal -> ConfigHandler.maxCherryPetals_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Cherry Petal"),
                    ConfigHandler.cherryPetal_BlockItems_DEFAULT, () -> ConfigHandler.cherryPetal_BlockItems, newVal -> ConfigHandler.cherryPetal_BlockItems = newVal
                ))

                // azalea leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Azalea Leaf"),
                    Binding.generic(ConfigHandler.azaleaLeaf_onPlace_DEFAULT, () -> ConfigHandler.azaleaLeaf_onPlace, newVal -> ConfigHandler.azaleaLeaf_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigHandler.maxAzaleaLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxAzaleaLeaves_onPlace, newVal -> ConfigHandler.maxAzaleaLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.azaleaLeaf_onBreak_DEFAULT, () -> ConfigHandler.azaleaLeaf_onBreak, newVal -> ConfigHandler.azaleaLeaf_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigHandler.maxAzaleaLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxAzaleaLeaves_onBreak, newVal -> ConfigHandler.maxAzaleaLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Azalea Leaf"),
                    ConfigHandler.azaleaLeaf_BlockItems_DEFAULT, () -> ConfigHandler.azaleaLeaf_BlockItems, newVal -> ConfigHandler.azaleaLeaf_BlockItems = newVal
                ))

                // flowering azalea leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Flowering Azalea Leaf"),
                    Binding.generic(ConfigHandler.floweringAzaleaLeaf_onPlace_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_onPlace, newVal -> ConfigHandler.floweringAzaleaLeaf_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigHandler.maxFloweringAzaleaLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace, newVal -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.floweringAzaleaLeaf_onBreak_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_onBreak, newVal -> ConfigHandler.floweringAzaleaLeaf_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigHandler.maxFloweringAzaleaLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak, newVal -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Flowering Azalea Leaf"),
                    ConfigHandler.floweringAzaleaLeaf_BlockItems_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_BlockItems, newVal -> ConfigHandler.floweringAzaleaLeaf_BlockItems = newVal
                ))

                // tinted leaf particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Biome Coloured Leaf"),
                    Binding.generic(ConfigHandler.tintedLeaves_onPlace_DEFAULT, () -> ConfigHandler.tintedLeaves_onPlace, newVal -> ConfigHandler.tintedLeaves_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigHandler.maxTintedLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxTintedLeaves_onPlace, newVal -> ConfigHandler.maxTintedLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.tintedLeaves_onBreak_DEFAULT, () -> ConfigHandler.tintedLeaves_onBreak, newVal -> ConfigHandler.tintedLeaves_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigHandler.maxTintedLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxTintedLeaves_onBreak, newVal -> ConfigHandler.maxTintedLeaves_onBreak = newVal)
                ))
                .group( createBlockItemListOption(
                    Component.literal("Biome Coloured Leaf"),
                    ConfigHandler.tintedLeaves_BlockItems_DEFAULT, () -> ConfigHandler.tintedLeaves_BlockItems, newVal -> ConfigHandler.tintedLeaves_BlockItems = newVal
                ))

                // vanilla block particles
                .group( createParticleToggleAndMaxConfigGroup(
                    Component.literal("Vanilla Block"),
                    Binding.generic(ConfigHandler.block_onPlace_DEFAULT, () -> ConfigHandler.block_onPlace, newVal -> ConfigHandler.block_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigHandler.maxBlock_onPlace_DEFAULT, () -> ConfigHandler.maxBlock_onPlace, newVal -> ConfigHandler.maxBlock_onPlace = newVal),
                    Binding.generic(ConfigHandler.block_onBreak_DEFAULT, () -> ConfigHandler.block_onBreak, newVal -> ConfigHandler.block_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigHandler.maxBlock_onBreak_DEFAULT, () -> ConfigHandler.maxBlock_onBreak, newVal -> ConfigHandler.maxBlock_onBreak = newVal)
                ))

            .build())

            .category(ConfigCategory.createBuilder()
                .name(Component.literal("Fluid Particles"))
                .tooltip(Component.literal("Configure how particles are emitted when placing fluids"))

                .group(OptionGroup.createBuilder()
                    .name(Component.literal("-- Information --"))
                    .description(OptionDescription.of(Component.literal("infotext")))
                    .collapsed(true)
                    .option(LabelOption.createBuilder()

                    .build())
                .build())

                // water splash
                .group( createFluidParticleToggleAndMaxConfigGroup(
                    Component.literal("Water Splash"),
                    Binding.generic(ConfigHandler.waterSplash_onPlace_DEFAULT, () -> ConfigHandler.waterSplash_onPlace, newVal -> ConfigHandler.waterSplash_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigHandler.maxWaterSplash_onPlace_DEFAULT, () -> ConfigHandler.maxWaterSplash_onPlace, newVal -> ConfigHandler.maxWaterSplash_onPlace = newVal)
                ))
                .group( createFluidListOption(
                    Component.literal("Water Splash"),
                    ConfigHandler.waterSplash_fluids_DEFAULT, () -> ConfigHandler.waterSplash_fluids, newVal -> ConfigHandler.waterSplash_fluids = newVal
                ))

                // lava splash
                .group( createFluidParticleToggleAndMaxConfigGroup(
                    Component.literal("Lava Splash"),
                    Binding.generic(ConfigHandler.lavaSplash_onPlace_DEFAULT, () -> ConfigHandler.lavaSplash_onPlace, newVal -> ConfigHandler.lavaSplash_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigHandler.maxLavaSplash_onPlace_DEFAULT, () -> ConfigHandler.maxLavaSplash_onPlace, newVal -> ConfigHandler.maxLavaSplash_onPlace = newVal)
                ))
                .group( createFluidListOption(
                    Component.literal("Lava Splash"),
                    ConfigHandler.lavaSplash_fluids_DEFAULT, () -> ConfigHandler.lavaSplash_fluids, newVal -> ConfigHandler.lavaSplash_fluids = newVal
                ))

            .build())

        .build()
        .generateScreen(parentScreen);
    }

    private static OptionGroup createFluidParticleToggleAndMaxConfigGroup(Component particleName, Binding<Boolean> spawnOnFluidPlaceBinding, Option<Integer> maxPlaceParticlesOption) {
        return OptionGroup.createBuilder()
            .name(createPlaceholderTranslatedFallbackComponent("placeholder.particles", "%s Particles", particleName.getString()))
            .description(OptionDescription.of(createPlaceholderTranslatedFallbackComponent("placeholder.1", "When %s particles should be spawned", particleName.getString())))
            .option(Option.<Boolean>createBuilder()
                .name(Component.literal("Spawn when Fluid Placed"))
                .description(OptionDescription.of(createPlaceholderTranslatedFallbackComponent("placeholder.1", "Should %s particles spawn when a fluid is placed?", particleName.getString())))
                .binding(spawnOnFluidPlaceBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .option(maxPlaceParticlesOption)
            .build();
    }

    private static OptionGroup createParticleToggleAndMaxConfigGroup(Component particleName, Binding<Boolean> spawnOnBlockPlaceBinding, Option<Integer> maxPlaceParticlesOption, Binding<Boolean> spawnOnBlockBreakBinding, Option<Integer> maxBreakParticlesOption) {
        return OptionGroup.createBuilder()
            .name(createPlaceholderTranslatedFallbackComponent("placeholder.particles", "%s Particles", particleName.getString()))
            .description(OptionDescription.of(createPlaceholderTranslatedFallbackComponent("placeholder.1", "When %s particles should be spawned", particleName.getString())))
            .option(Option.<Boolean>createBuilder()
                .name(Component.literal("Spawn when Block Placed"))
                .description(OptionDescription.of(createPlaceholderTranslatedFallbackComponent("placeholder.1", "Should %s particles spawn when a block is placed?", particleName.getString())))
                .binding(spawnOnBlockPlaceBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build())
            .option(maxPlaceParticlesOption)
            .option(Option.<Boolean>createBuilder()
                .name(Component.literal("Spawn when Block Broken"))
                .description(OptionDescription.of(createPlaceholderTranslatedFallbackComponent("placeholder.1", "Should %s particles spawn when a block is broken?", particleName.getString())))
                .binding(spawnOnBlockBreakBinding)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build())
            .option(maxBreakParticlesOption)
        .build();
    }

    private static Option<Integer> maxParticlesOnPlaceOption(int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return createIntegerOption(maxParticlesDefault, getter, setter, Component.literal("Amount of Particles when Placed"), Component.literal("The maximum amount of particles that can spawn per edge of the block when placed\n\n This value is ignored if 'Spawn when Block Placed' is off"), 1, 16, 1);
    }
    private static Option<Integer> maxParticlesOnBreakOption(int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return createIntegerOption(maxParticlesDefault, getter, setter, Component.literal("Amount of Particles when Broken"), Component.literal("The maximum amount of particles that can spawn in a 3D grid in the shape of the broken block\n\n This value is ignored if 'Spawn when Block Broken' is off"), 2, 8, 1);
    }
    private static Option<Integer> createIntegerOption(int defaultValue, Supplier<Integer> getter, Consumer<Integer> setter, Component name, Component description, int min, int max, int step) {
        return Option.<Integer>createBuilder()
            .name(name)
            .description(OptionDescription.of(description))
            .binding(defaultValue, getter, setter)
            .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
        .build();
    }

    private static ListOption<BlockItem> createBlockItemListOption(Component particleName, List<BlockItem> defaultValue, Supplier<List<BlockItem>> getter, Consumer<List<BlockItem>> setter) {
        return createListOption((BlockItem) Items.STONE, BlockItemController::new, particleName, defaultValue, getter, setter);
    }
    private static ListOption<Fluid> createFluidListOption(Component particleName, List<Fluid> defaultValue, Supplier<List<Fluid>> getter, Consumer<List<Fluid>> setter) {
        return createListOption(Fluids.EMPTY, FluidController::new, particleName, defaultValue, getter, setter);
    }
    private static <T> ListOption<T> createListOption(T initial, Function<ListOptionEntry<T>, Controller<T>> controller, Component particleName, List<T> defaultValue, Supplier<List<T>> getter, Consumer<List<T>> setter) {
        return ListOption.<T>createBuilder()
            .name(createPlaceholderTranslatedFallbackComponent("placeholder.blocks", "%s Blocks", particleName.getString()))
            .description(OptionDescription.of(createPlaceholderTranslatedFallbackComponent("placeholder.1", "Blocks that should spawn %s particles when broken or placed. \n\nA block must have a registered BlockItem to appear in this list", particleName.getString())))
            .binding(defaultValue, getter, setter)
            .customController(controller)
            .initial(initial)
        .build();
    }

    private static Component createPlaceholderTranslatedFallbackComponent(String translationKey, String translationFallback, Object... args) {
        return Component.literal(Component.translatableWithFallback(translationKey, translationFallback).getString().formatted(args));
    }
}
