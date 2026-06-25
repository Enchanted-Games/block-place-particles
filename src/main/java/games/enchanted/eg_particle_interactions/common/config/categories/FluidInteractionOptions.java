package games.enchanted.eg_particle_interactions.common.config.categories;

import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.option.BlockOrTagLocationListOption;
import games.enchanted.eg_particle_interactions.common.config.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config.option.IntOption;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class FluidInteractionOptions {
    public static final ConfigOption<Integer> AMOUNT_ON_PLACE = registerOption(
        new IntOption(15, "amount_on_place")
    );

    public static final ConfigOption<Boolean> LAVA_BUBBLE_POP_ENABLED = registerOption(
        new BoolOption(true, "lava_bubble_pop_enabled")
    );
    public static final ConfigOption<Integer> LAVA_BUBBLE_POP_SPAWN_CHANCE = registerOption(
        new IntOption(20, "lava_bubble_pop_spawn_chance")
    );

    public static final ConfigOption<List<ObjectOrTagLocation>> UNDERWATER_BUBBLE_STREAM_BLOCKS = registerOption(
        new BlockOrTagLocationListOption(
            List.of(
                RegistryHelpers.getBlockLocationFromBlock(Blocks.SEAGRASS),
                RegistryHelpers.getBlockLocationFromBlock(Blocks.TALL_SEAGRASS)
            ),
            "underwater_bubble_stream_blocks"
        )
    );
    public static final ConfigOption<Boolean> UNDERWATER_BUBBLE_STREAM_ENABLED = registerOption(
        new BoolOption(true, "underwater_bubble_stream_enabled")
    );
    public static final ConfigOption<Integer> UNDERWATER_BUBBLE_STREAM_SPAWN_CHANCE = registerOption(
        new IntOption(12, "underwater_bubble_stream_spawn_chance")
    );

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.FLUID_AMBIENT, option);
    }

    public static void init() {}
}
