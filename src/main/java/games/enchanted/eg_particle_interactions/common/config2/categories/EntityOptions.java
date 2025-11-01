package games.enchanted.eg_particle_interactions.common.config2.categories;

import games.enchanted.eg_particle_interactions.common.config2.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config2.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config2.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config2.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config2.option.IntOption;

public class EntityOptions {
    public static final ConfigOption<Boolean> MINECART_SPARKS_ENABLED = registerOption(
        new BoolOption(true, "minecart_sparks_enabled")
    );
    public static final ConfigOption<Integer> MINECART_SPARKS_SPAWN_CHANCE = registerOption(
        new IntOption(50, "minecart_sparks_spawn_chance")
    );
    public static final ConfigOption<Boolean> MINECART_SPARKS_ONLY_WITH_PASSENGER = registerOption(
        new BoolOption(true, "minecart_sparks_only_with_passenger")
    );

    public static final ConfigOption<Boolean> LIGHTNING_STRIKE_ENABLED = registerOption(
        new BoolOption(true, "lightning_strike_enabled")
    );
    public static final ConfigOption<Integer> LIGHTNING_STRIKE_AMOUNT_OF_ARCS = registerOption(
        new IntOption(3, "lightning_strike_amount_of_arcs")
    );
    public static final ConfigOption<Integer> LIGHTNING_STRIKE_AMOUNT_OF_SPARKS = registerOption(
        new IntOption(18, "lightning_strike_amount_of_sparks")
    );

    public static final ConfigOption<Integer> BLAZE_SPARKS_SPAWN_CHANCE = registerOption(
        new IntOption(25, "blaze_sparks_spawn_chance")
    );
    public static final ConfigOption<Boolean> BLAZE_SPARKS_SPAWN_ON_HURT = registerOption(
        new BoolOption(true, "blaze_sparks_enabled")
    );
    public static final ConfigOption<Integer> BLAZE_SPARKS_AMOUNT_ON_HURT = registerOption(
        new IntOption(6, "blaze_sparks_amount_on_hurt")
    );

    public static final ConfigOption<Boolean> ITEM_FRAME_INTERACTION_ENABLED = registerOption(
        new BoolOption(true, "item_frame_interaction_enabled")
    );
    public static final ConfigOption<Integer> ITEM_FRAME_INTERACTION_AMOUNT = registerOption(
        new IntOption(4, "item_frame_interaction_amount")
    );

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.ENTITY, option);
    }

    public static void init() {}
}
