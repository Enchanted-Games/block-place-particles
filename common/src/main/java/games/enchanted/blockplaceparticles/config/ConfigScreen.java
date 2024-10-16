package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import games.enchanted.blockplaceparticles.config.controller.BlockLocationController;
import games.enchanted.blockplaceparticles.config.controller.FluidController;
import games.enchanted.blockplaceparticles.config.controller.HybridBlockAndColourController;
import games.enchanted.blockplaceparticles.config.type.ResourceLocationAndColour;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
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

                .group(OptionGroup.createBuilder()
                    .name( Component.literal("--------------") )
                    .collapsed(true)
                    .option(LabelOption.createBuilder()
                    .build())
                .build())

                .group(OptionGroup.createBuilder()
                    .name( Component.literal("test group") )
                    .collapsed(false)
                    .option(
                        Option.<ResourceLocationAndColour>createBuilder()
                            .name(Component.literal("test"))
                            .customController(HybridBlockAndColourController::new)
                            .binding(ConfigHandler.testBlockAndColour_DEFAULT, () -> ConfigHandler.testBlockAndColour, newVal -> ConfigHandler.testBlockAndColour = newVal)
                        .build()
                    )
                .build())

                // underwater bubbles
                .group( createMultipleOptionsConfigGroup(
                    "underwater_block_bubbles",
                    "underwater_block_bubbles",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    booleanOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_PLACE, "underwater_block_bubbles", Binding.generic(ConfigHandler.underwaterBubbles_onPlace_DEFAULT, () -> ConfigHandler.underwaterBubbles_onPlace, newVal -> ConfigHandler.underwaterBubbles_onPlace = newVal)),
                    integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE, "underwater_block_bubbles", ConfigHandler.maxUnderwaterBubbles_onPlace_DEFAULT, () -> ConfigHandler.maxUnderwaterBubbles_onPlace, newVal -> ConfigHandler.maxUnderwaterBubbles_onPlace = newVal, 1, 50, 1),
                    booleanOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_BREAK, "underwater_block_bubbles", Binding.generic(ConfigHandler.underwaterBubbles_onBreak_DEFAULT, () -> ConfigHandler.underwaterBubbles_onBreak, newVal -> ConfigHandler.underwaterBubbles_onBreak = newVal)),
                    integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK, "underwater_block_bubbles", ConfigHandler.maxUnderwaterBubbles_onBreak_DEFAULT, () -> ConfigHandler.maxUnderwaterBubbles_onBreak, newVal -> ConfigHandler.maxUnderwaterBubbles_onBreak = newVal, 1, 50, 1)
                ))

                .group(OptionGroup.createBuilder()
                    .name( Component.literal("--------------") )
                    .collapsed(true)
                    .option(LabelOption.createBuilder()
                    .build())
                .build())

                // snowflake particles
                .group( createBlockPlaceAndBreakConfigGroup(
                    "snowflake",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.snowflake_onPlace_DEFAULT, () -> ConfigHandler.snowflake_onPlace, newVal -> ConfigHandler.snowflake_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_ALONG_EDGES, ConfigHandler.maxSnowflakes_onPlace_DEFAULT, () -> ConfigHandler.maxSnowflakes_onPlace, newVal -> ConfigHandler.maxSnowflakes_onPlace = newVal),
                    Binding.generic(ConfigHandler.snowflake_onBreak_DEFAULT, () -> ConfigHandler.snowflake_onBreak, newVal -> ConfigHandler.snowflake_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_ALONG_AXIS, ConfigHandler.maxSnowflakes_onBreak_DEFAULT, () -> ConfigHandler.maxSnowflakes_onBreak, newVal -> ConfigHandler.maxSnowflakes_onBreak = newVal)
                ))
                .group( createBlockLocationListOption(
                    "snowflake",
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.snowflake_Blocks_DEFAULT, () -> ConfigHandler.snowflake_Blocks, newVal -> ConfigHandler.snowflake_Blocks = newVal
                ))

                // cherry petal particles
                .group( createBlockPlaceAndBreakConfigGroup(
                    "cherry_petal",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.cherryPetal_onPlace_DEFAULT, () -> ConfigHandler.cherryPetal_onPlace, newVal -> ConfigHandler.cherryPetal_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_ALONG_EDGES, ConfigHandler.maxCherryPetals_onPlace_DEFAULT, () -> ConfigHandler.maxCherryPetals_onPlace, newVal -> ConfigHandler.maxCherryPetals_onPlace = newVal),
                    Binding.generic(ConfigHandler.cherryPetal_onBreak_DEFAULT, () -> ConfigHandler.cherryPetal_onBreak, newVal -> ConfigHandler.cherryPetal_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_ALONG_AXIS, ConfigHandler.maxCherryPetals_onBreak_DEFAULT, () -> ConfigHandler.maxCherryPetals_onBreak, newVal -> ConfigHandler.maxCherryPetals_onBreak = newVal)
                ))
                .group( createBlockLocationListOption(
                    "cherry_petal",
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.cherryPetal_Blocks_DEFAULT, () -> ConfigHandler.cherryPetal_Blocks, newVal -> ConfigHandler.cherryPetal_Blocks = newVal
                ))

                // azalea leaf particles
                .group( createBlockPlaceAndBreakConfigGroup(
                    "azalea_leaf",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.azaleaLeaf_onPlace_DEFAULT, () -> ConfigHandler.azaleaLeaf_onPlace, newVal -> ConfigHandler.azaleaLeaf_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_ALONG_EDGES, ConfigHandler.maxAzaleaLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxAzaleaLeaves_onPlace, newVal -> ConfigHandler.maxAzaleaLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.azaleaLeaf_onBreak_DEFAULT, () -> ConfigHandler.azaleaLeaf_onBreak, newVal -> ConfigHandler.azaleaLeaf_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_ALONG_AXIS, ConfigHandler.maxAzaleaLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxAzaleaLeaves_onBreak, newVal -> ConfigHandler.maxAzaleaLeaves_onBreak = newVal)
                ))
                .group( createBlockLocationListOption(
                    "azalea_leaf",
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.azaleaLeaf_Blocks_DEFAULT, () -> ConfigHandler.azaleaLeaf_Blocks, newVal -> ConfigHandler.azaleaLeaf_Blocks = newVal
                ))

                // flowering azalea leaf particles
                .group( createBlockPlaceAndBreakConfigGroup(
                    "flowering_azalea_leaf",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.floweringAzaleaLeaf_onPlace_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_onPlace, newVal -> ConfigHandler.floweringAzaleaLeaf_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_ALONG_EDGES, ConfigHandler.maxFloweringAzaleaLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace, newVal -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.floweringAzaleaLeaf_onBreak_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_onBreak, newVal -> ConfigHandler.floweringAzaleaLeaf_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_ALONG_AXIS, ConfigHandler.maxFloweringAzaleaLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak, newVal -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak = newVal)
                ))
                .group( createBlockLocationListOption(
                    "flowering_azalea_leaf",
                    "generic_particle_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.floweringAzaleaLeaf_Blocks_DEFAULT, () -> ConfigHandler.floweringAzaleaLeaf_Blocks, newVal -> ConfigHandler.floweringAzaleaLeaf_Blocks = newVal
                ))

                // tinted leaf particles
                .group( createBlockPlaceAndBreakConfigGroup(
                    "biome_leaf",
                    "biome_leaf",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.tintedLeaves_onPlace_DEFAULT, () -> ConfigHandler.tintedLeaves_onPlace, newVal -> ConfigHandler.tintedLeaves_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_ALONG_EDGES, ConfigHandler.maxTintedLeaves_onPlace_DEFAULT, () -> ConfigHandler.maxTintedLeaves_onPlace, newVal -> ConfigHandler.maxTintedLeaves_onPlace = newVal),
                    Binding.generic(ConfigHandler.tintedLeaves_onBreak_DEFAULT, () -> ConfigHandler.tintedLeaves_onBreak, newVal -> ConfigHandler.tintedLeaves_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_ALONG_AXIS, ConfigHandler.maxTintedLeaves_onBreak_DEFAULT, () -> ConfigHandler.maxTintedLeaves_onBreak, newVal -> ConfigHandler.maxTintedLeaves_onBreak = newVal)
                ))
                .group( createBlockLocationListOption(
                    "biome_leaf",
                    "biome_leaf_blocks",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    ConfigHandler.tintedLeaves_Blocks_DEFAULT, () -> ConfigHandler.tintedLeaves_Blocks, newVal -> ConfigHandler.tintedLeaves_Blocks = newVal
                ))

                // vanilla block particles
                .group( createBlockPlaceAndBreakConfigGroup(
                    "vanilla_particle",
                    "generic_particle",
                    ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
                    Binding.generic(ConfigHandler.block_onPlace_DEFAULT, () -> ConfigHandler.block_onPlace, newVal -> ConfigHandler.block_onPlace = newVal),
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_ALONG_EDGES, ConfigHandler.maxBlock_onPlace_DEFAULT, () -> ConfigHandler.maxBlock_onPlace, newVal -> ConfigHandler.maxBlock_onPlace = newVal),
                    Binding.generic(ConfigHandler.block_onBreak_DEFAULT, () -> ConfigHandler.block_onBreak, newVal -> ConfigHandler.block_onBreak = newVal),
                    maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_ALONG_AXIS, ConfigHandler.maxBlock_onBreak_DEFAULT, () -> ConfigHandler.maxBlock_onBreak, newVal -> ConfigHandler.maxBlock_onBreak = newVal)
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
                    integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "flint_and_steel_sparks", ConfigHandler.maxFlintAndSteelSpark_onUse_DEFAULT, () -> ConfigHandler.maxFlintAndSteelSpark_onUse, newVal -> ConfigHandler.maxFlintAndSteelSpark_onUse = newVal, 1, 32, 1),
                    integerSliderOption(ConfigTranslation.ITEM_USE_PARTICLE_INTENSITY, "flint_and_steel_sparks", ConfigHandler.flintAndSteelSpark_intensity_DEFAULT, () -> ConfigHandler.flintAndSteelSpark_intensity, newVal -> ConfigHandler.flintAndSteelSpark_intensity = newVal, 1, 8, 1)
                ))

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

                // axe strip
                .group( createMultipleOptionsConfigGroup(
                    "axe_strip_particles",
                    "axe_strip_particles",
                    ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                    booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "axe_strip_particles", Binding.generic(ConfigHandler.axeStrip_onUse_DEFAULT, () -> ConfigHandler.axeStrip_onUse, newVal -> ConfigHandler.axeStrip_onUse = newVal)),
                    integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "axe_strip_particles", ConfigHandler.maxAxeStrip_onUse_DEFAULT, () -> ConfigHandler.maxAxeStrip_onUse, newVal -> ConfigHandler.maxAxeStrip_onUse = newVal, 1, 50, 1)
                ))

                // hoe till
                .group( createMultipleOptionsConfigGroup(
                    "hoe_till_particles",
                    "hoe_till_particles",
                    ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                    booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "hoe_till_particles", Binding.generic(ConfigHandler.hoeTill_onUse_DEFAULT, () -> ConfigHandler.hoeTill_onUse, newVal -> ConfigHandler.hoeTill_onUse = newVal)),
                    integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "hoe_till_particles", ConfigHandler.maxHoeTill_onUse_DEFAULT, () -> ConfigHandler.maxHoeTill_onUse, newVal -> ConfigHandler.maxHoeTill_onUse = newVal, 1, 50, 1)
                ))

                // shovel flatten
                .group( createMultipleOptionsConfigGroup(
                    "shovel_flatten_particles",
                    "shovel_flatten_particles",
                    ConfigTranslation.ITEMS_CONFIG_CATEGORY,
                    booleanOption(ConfigTranslation.SPAWN_PARTICLE_ON_ITEM_USE, "shovel_flatten_particles", Binding.generic(ConfigHandler.shovelFlatten_onUse_DEFAULT, () -> ConfigHandler.shovelFlatten_onUse, newVal -> ConfigHandler.shovelFlatten_onUse = newVal)),
                    integerSliderOption(ConfigTranslation.MAX_PARTICLES_ON_ITEM_USE, "shovel_flatten_particles", ConfigHandler.maxShovelFlatten_onUse_DEFAULT, () -> ConfigHandler.maxShovelFlatten_onUse, newVal -> ConfigHandler.maxShovelFlatten_onUse = newVal, 1, 50, 1)
                ))

            .build())

            // entity category
            .category(ConfigCategory.createBuilder()
                .name(ConfigTranslation.getCategoryName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY).toComponent())
                .tooltip(ConfigTranslation.createDesc(ConfigTranslation.getCategoryName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY)))

                // category info
                .group(OptionGroup.createBuilder()
                    .name( ConfigTranslation.getGroupName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY, "info").toComponent() )
                    .description(OptionDescription.of( ConfigTranslation.createDesc(ConfigTranslation.getGroupName(ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY, "info")) ))
                    .collapsed(true)
                    .option(LabelOption.createBuilder()

                    .build())
                .build())

                // minecart sparks at max speed
                .group( createMultipleOptionsConfigGroup(
                    "minecart_sparks",
                    "minecart_sparks",
                    ConfigTranslation.ENTITY_PARTICLES_CONFIG_CATEGORY,
                    booleanOption(ConfigTranslation.SPAWN_PARTICLE_WHEN_MINECART_AT_MAX_SPEED, "minecart_sparks", Binding.generic(ConfigHandler.minecart_enabled_DEFAULT, () -> ConfigHandler.minecart_enabled, newVal -> ConfigHandler.minecart_enabled = newVal)),
                    integerSliderOption(ConfigTranslation.MINECART_WHEEL_PARTICLE_AMOUNT, "minecart_sparks", ConfigHandler.minecart_spawnChance_DEFAULT, () -> ConfigHandler.minecart_spawnChance, newVal -> ConfigHandler.minecart_spawnChance = newVal, 1, 100, 1),
                    booleanOption(ConfigTranslation.MINECART_ONLY_WITH_PASSENGER, "minecart_sparks", Binding.generic(ConfigHandler.minecart_onlyWithPassenger_DEFAULT, () -> ConfigHandler.minecart_onlyWithPassenger, newVal -> ConfigHandler.minecart_onlyWithPassenger = newVal))
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
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, ConfigHandler.maxTintedWaterSplash_onPlace_DEFAULT, () -> ConfigHandler.maxTintedWaterSplash_onPlace, newVal -> ConfigHandler.maxTintedWaterSplash_onPlace = newVal)
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
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, ConfigHandler.maxLavaSplash_onPlace_DEFAULT, () -> ConfigHandler.maxLavaSplash_onPlace, newVal -> ConfigHandler.maxLavaSplash_onPlace = newVal)
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
                    maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_FLUID_PLACE, ConfigHandler.maxGenericSplash_onPlace_DEFAULT, () -> ConfigHandler.maxGenericSplash_onPlace, newVal -> ConfigHandler.maxGenericSplash_onPlace = newVal)
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
        Option<Boolean> onFluidPlaceOption = Option.<Boolean>createBuilder()
            .name( ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_FLUID_PARTICLE_ON_PLACE).toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_FLUID_PARTICLE_ON_PLACE)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(spawnOnFluidPlaceBinding)
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
        .build();

        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, onFluidPlaceOption, maxPlaceParticlesOption);
    }

    private static OptionGroup createBlockPlaceAndBreakConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> spawnOnBlockPlaceBinding, Option<Integer> maxPlaceParticlesOption, Binding<Boolean> spawnOnBlockBreakBinding, Option<Integer> maxBreakParticlesOption) {
        Option<Boolean> spawnOnBlockPlaceOption = Option.<Boolean>createBuilder()
            .name( ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_PLACE).toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_PLACE)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(spawnOnBlockPlaceBinding)
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
        .build();
        Option<Boolean> spawnOnBlockBreakOption = Option.<Boolean>createBuilder()
            .name( ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_BREAK).toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_BLOCK_PARTICLE_ON_BREAK)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(spawnOnBlockBreakBinding)
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
        .build();

        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, spawnOnBlockPlaceOption, maxPlaceParticlesOption, spawnOnBlockBreakOption, maxBreakParticlesOption);

    }

    private static OptionGroup createParticleToggleAndMaxAndIntensityConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> particleEnabledBinding, String particleEnabledTranslationOption, Option<Integer> maxParticlesOnUseOption, Option<Integer> particleIntensityOption) {
        Option<Boolean> particleToggleOption = booleanOption(particleEnabledTranslationOption, particleTypeKey, particleEnabledBinding);
        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, particleToggleOption, maxParticlesOnUseOption, particleIntensityOption);
    }

    private static OptionGroup createParticleToggleAndIntSliderConfigGroup(String particleTypeKey, String groupName, String category, Binding<Boolean> particleEnabledBinding, String particleEnabledTranslationOption, Option<Integer> intSlider) {
        Option<Boolean> particleToggleOption = booleanOption(particleEnabledTranslationOption, particleTypeKey, particleEnabledBinding);
        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, particleToggleOption, intSlider);
    }

    private static OptionGroup createMultipleOptionsConfigGroup(String particleTypeKey, String groupName, String category, Option<?> ...options) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        OptionGroup.Builder optionGroupBuilder = OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ));
        for (Option<?> option : options) {
            optionGroupBuilder.option(option);
        }
        return optionGroupBuilder.build();
    }

    private static Option<Boolean> booleanOption(String booleanOptionLabelText, String particleTypeKey, Binding<Boolean> binding) {
        return Option.<Boolean>createBuilder()
            .name( ConfigTranslation.getGlobalOption(booleanOptionLabelText).toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(booleanOptionLabelText)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(binding)
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
        .build();
    }

    private static Option<Integer> maxParticlesOnPlaceOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return integerSliderOption(optionName, maxParticlesDefault, getter, setter, 1, 16, 1);
    }
    private static Option<Integer> maxParticlesOnBreakOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter) {
        return integerSliderOption(optionName, maxParticlesDefault, getter, setter, 2, 8, 1);
    }

    private static Option<Integer> integerSliderOption(String optionName, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step) {
        return createIntegerOption(maxParticlesDefault, getter, setter, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), min, max, step);
    }
    private static Option<Integer> integerSliderOption(String optionName, String particleTypeKey, int maxParticlesDefault, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step) {
        return createIntegerOption(maxParticlesDefault, getter, setter, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createPlaceholder( ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ), min, max, step);
    }

    private static Option<Integer> createIntegerOption(int defaultValue, Supplier<Integer> getter, Consumer<Integer> setter, Component name, Component description, int min, int max, int step) {
        return Option.<Integer>createBuilder()
            .name(name)
            .description(OptionDescription.of(description))
            .binding(defaultValue, getter, setter)
            .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
        .build();
    }

    private static ListOption<ResourceLocation> createBlockLocationListOption(String particleTypeKey, String groupName, String category, List<ResourceLocation> defaultValue, Supplier<List<ResourceLocation>> getter, Consumer<List<ResourceLocation>> setter) {
        return createListOption(RegistryHelper.getLocationFromBlock(Blocks.STONE), BlockLocationController::new, particleTypeKey, groupName, category, defaultValue, getter, setter);
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
