package games.enchanted.eg_particle_interactions.common.config.categories;

import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.option.BlockOrTagLocationListOption;
import games.enchanted.eg_particle_interactions.common.config.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config.option.IntOption;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class BlockInteractionOptions {
    public static final ConfigOption<Boolean> UNDERWATER_BUBBLES_ON_PLACE_ENABLED = registerOption(
        new BoolOption(true, "underwater_bubbles_on_place_enabled")
    );
    public static final ConfigOption<Integer> UNDERWATER_BUBBLES_MAX_ON_PLACE = registerOption(
        new IntOption(12, "underwater_bubbles_max_on_place")
    );
    public static final ConfigOption<Boolean> UNDERWATER_BUBBLES_ON_BREAK_ENABLED = registerOption(
        new BoolOption(true, "underwater_bubbles_on_break_enabled")
    );
    public static final ConfigOption<Integer> UNDERWATER_BUBBLES_MAX_ON_BREAK = registerOption(
        new IntOption(6, "underwater_bubbles_max_on_break")
    );

    public static final ConfigOption<Boolean> CAMPFIRE_SPARK_ENABLED = registerOption(
        new BoolOption(true, "campfire_spark_enabled")
    );
    public static final ConfigOption<Integer> CAMPFIRE_SPARK_SPAWN_CHANCE = registerOption(
        new IntOption(20, "campfire_spark_spawn_chance")
    );
    public static final ConfigOption<Boolean> CAMPFIRE_EMBER_ENABLED = registerOption(
        new BoolOption(true, "campfire_ember_enabled")
    );
    public static final ConfigOption<Integer> CAMPFIRE_EMBER_SPAWN_CHANCE = registerOption(
        new IntOption(45, "campfire_ember_spawn_chance")
    );

    public static final ConfigOption<Boolean> FIRE_SPARK_ENABLED = registerOption(
        new BoolOption(true, "fire_spark_enabled")
    );
    public static final ConfigOption<Integer> FIRE_SPARK_SPAWN_CHANCE = registerOption(
        new IntOption(25, "fire_spark_spawn_chance")
    );
    public static final ConfigOption<Boolean> FIRE_EMBER_ENABLED = registerOption(
        new BoolOption(true, "fire_ember_enabled")
    );
    public static final ConfigOption<Integer> FIRE_EMBER_SPAWN_CHANCE = registerOption(
        new IntOption(45, "fire_ember_spawn_chance")
    );

    public static final ConfigOption<Boolean> BLOCK_RUSTLE_ENABLED = registerOption(
        new BoolOption(true, "block_rustle_enabled")
    );
    public static final ConfigOption<List<BlockOrTagLocation>> BLOCK_RUSTLE_BLOCKS = registerOption(
        new BlockOrTagLocationListOption(
            List.of(
                RegistryHelpers.getBlockLocationFromBlock(Blocks.SUGAR_CANE),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.FIREFLY_BUSH),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.BUSH),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.SHORT_DRY_GRASS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.TALL_DRY_GRASS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.LEAF_LITTER),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.VINE),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.PALE_HANGING_MOSS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.SHORT_GRASS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.TALL_GRASS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.FERN),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.LARGE_FERN),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.SWEET_BERRY_BUSH),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.DEAD_BUSH),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.SEAGRASS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.TALL_SEAGRASS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.WARPED_ROOTS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.TWISTING_VINES),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.TWISTING_VINES_PLANT),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.NETHER_SPROUTS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.CRIMSON_ROOTS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.WEEPING_VINES),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.WEEPING_VINES_PLANT),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.HANGING_ROOTS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.COBWEB),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.SNOW),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.REDSTONE_WIRE),
                new BlockOrTagLocation(BlockTags.CAVE_VINES.location(), true),
                new BlockOrTagLocation(BlockTags.FLOWERS.location(), true),
                new BlockOrTagLocation(ResourceLocation.fromNamespaceAndPath("c", "flowers"), true),
                new BlockOrTagLocation(ResourceLocation.fromNamespaceAndPath("c", "flowers/small"), true),
                new BlockOrTagLocation(ResourceLocation.fromNamespaceAndPath("c", "flowers/tall"), true),
                new BlockOrTagLocation(BlockTags.CROPS.location(), true)
            ),
            "block_rustle_blocks"
        )
    );

    public static final ConfigOption<Boolean> ANVIL_USE_SPARKS_ENABLED = registerOption(
        new BoolOption(true, "anvil_use_sparks_enabled")
    );
    public static final ConfigOption<Integer> ANVIL_USE_SPARKS_MAX_ON_USE = registerOption(
        new IntOption(18, "anvil_use_sparks_max_on_use")
    );

    public static final ConfigOption<Boolean> GRINDSTONE_USE_SPARKS_ENABLED = registerOption(
        new BoolOption(true, "grindstone_use_sparks_enabled")
    );
    public static final ConfigOption<Integer> GRINDSTONE_USE_SPARKS_MAX_ON_USE = registerOption(
        new IntOption(12, "grindstone_use_sparks_max_on_use")
    );

    public static final ConfigOption<Boolean> BLOCK_FALLING_EFFECT_ENABLED = registerOption(
        new BoolOption(true, "block_falling_effect_enabled")
    );
    public static final ConfigOption<Integer> BLOCK_FALLING_EFFECT_RENDER_DISTANCE = registerOption(
        new IntOption(64, "block_falling_effect_render_distance")
    );

    public static final ConfigOption<Boolean> REDSTONE_INTERACTION_DUST_ENABLED = registerOption(
        new BoolOption(true, "redstone_interaction_dust_enabled")
    );
    public static final ConfigOption<Integer> REDSTONE_INTERACTION_DUST_AMOUNT = registerOption(
        new IntOption(6, "redstone_interaction_dust_amount")
    );

    public static final ConfigOption<Boolean> SMOKER_SMOKE_ENABLED = registerOption(
        new BoolOption(true, "smoker_smoke_enabled")
    );

    public static final ConfigOption<Boolean> FURNACE_EMBERS_ENABLED = registerOption(
        new BoolOption(true, "furnace_embers_enabled")
    );
    public static final ConfigOption<Boolean> VANILLA_FURNACE_PARTICLES_ENABLED = registerOption(
        new BoolOption(false, "vanilla_furnace_particles_enabled")
    );

    public static final ConfigOption<Boolean> BLAST_FURNACE_SPARKS_ENABLED = registerOption(
        new BoolOption(true, "blast_furnace_sparks_enabled")
    );


    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.BLOCK_INTERACTIONS, option);
    }

    public static void init() {}
}
