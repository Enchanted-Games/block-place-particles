package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.gui.controllers.dropdown.ItemController;
import games.enchanted.blockplaceparticles.config.controller.BlockItemController;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;
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

                // snowflake particles
                .group(OptionGroup.createBuilder()
                    .name(Component.literal("Snowflake Particles"))
                    .description(OptionDescription.of(Component.literal("When snoflake particles should be spawned")))
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Spawn when Block Placed"))
                        .description(OptionDescription.of(Component.literal("Should snowflake particles spawn when a block is placed?")))
                        .binding(ConfigHandler.snowParticleOnBlockPlace_DEFAULT, () -> ConfigHandler.snowParticleOnBlockPlace, newVal -> ConfigHandler.snowParticleOnBlockPlace = newVal)
                        .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .build())
                    .option( maxParticlesOnPlaceOption(ConfigHandler.maxSnowflakePlaceParticles_DEFAULT, () -> ConfigHandler.maxSnowflakePlaceParticles, newVal -> ConfigHandler.maxSnowflakePlaceParticles = newVal) )
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Spawn when Block Broken"))
                        .description(OptionDescription.of(Component.literal("Should snowflake particles spawn when a block is broken?")))
                        .binding(ConfigHandler.snowParticleOnBlockBreak_DEFAULT, () -> ConfigHandler.snowParticleOnBlockBreak, newVal -> ConfigHandler.snowParticleOnBlockBreak = newVal)
                        .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .build())
                    .option( maxParticlesOnBreakOption(ConfigHandler.maxSnowflakeBreakParticles_DEFAULT, () -> ConfigHandler.maxSnowflakeBreakParticles, newVal -> ConfigHandler.maxSnowflakeBreakParticles = newVal) )
                .build())
                .group(ListOption.<BlockItem>createBuilder()
                    .name(Component.literal("Snowflake Particle Blocks"))
                    .description(OptionDescription.of(Component.literal("Blocks that should spawn snowflake particles when placed or broken")))
                    .binding(ConfigHandler.snowParticleBlockItems_DEFAULT, () -> ConfigHandler.snowParticleBlockItems, newVal -> ConfigHandler.snowParticleBlockItems = newVal)
                    .customController(BlockItemController::new)
                    .initial((BlockItem) Items.STONE)
                .build())

                // vanilla block particles
                .group(OptionGroup.createBuilder()
                    .name(Component.literal("Vanilla Block Particles"))
                    .description(OptionDescription.of(Component.literal("When vanilla block particles should be spawned")))
                    // block place
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Spawn when Block Placed"))
                        .description(OptionDescription.of(Component.literal("Should vanilla block particles spawn when a block is placed?")))
                        .binding(ConfigHandler.blockParticleOnBlockPlace_DEFAULT, () -> ConfigHandler.blockParticleOnBlockPlace, newVal -> ConfigHandler.blockParticleOnBlockPlace = newVal)
                        .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .build())
                    .option( maxParticlesOnPlaceOption(ConfigHandler.maxBlockPlaceParticles_DEFAULT, () -> ConfigHandler.maxBlockPlaceParticles, newVal -> ConfigHandler.maxBlockPlaceParticles = newVal) )
                    // block break
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Spawn when Block Broken"))
                        .description(OptionDescription.of(Component.literal("Should vanilla block particles spawn when a block is broken?")))
                        .binding(ConfigHandler.blockParticleOnBlockBreak_DEFAULT, () -> ConfigHandler.blockParticleOnBlockBreak, newVal -> ConfigHandler.blockParticleOnBlockBreak = newVal)
                        .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .build())
                    .option( maxParticlesOnBreakOption(ConfigHandler.maxBlockBreakParticles_DEFAULT, () -> ConfigHandler.maxBlockBreakParticles, newVal -> ConfigHandler.maxBlockBreakParticles = newVal) )
                .build())

            .build())
        .build()
        .generateScreen(parentScreen);
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
}
