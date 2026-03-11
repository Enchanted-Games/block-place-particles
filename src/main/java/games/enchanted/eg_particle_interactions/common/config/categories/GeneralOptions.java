package games.enchanted.eg_particle_interactions.common.config.categories;

import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config.option.IntOption;

public class GeneralOptions {
    public static final ConfigOption<Boolean> PIXEL_CONSISTENT_TERRAIN_PARTICLES = registerOption(
        new BoolOption(true, "pixel_consistent_terrain_particles")
    );
    public static final ConfigOption<Boolean> PARTICLE_Z_FIGHTING_FIX = registerOption(
        new BoolOption(true, "particle_z_fighting_fixes")
    );
    public static final ConfigOption<Boolean> PROJECTILE_BREAKING_PARTICLE_VELOCITY_FIX = registerOption(
        new BoolOption(true, "projectile_breaking_particle_velocity_fix")
    );
    public static final ConfigOption<Boolean> AUTO_COLLAPSE_CONFIG_LISTS = registerOption(
        new BoolOption(true, "auto_collapse_config_lists")
    );
    public static final ConfigOption<Boolean> FIREFLY_FIXES = registerOption(
        new BoolOption(true, "firefly_fixes")
    );
    public static final ConfigOption<Boolean> SHOW_BUTTON_IN_OPTIONS_SCREEN = registerOption(
        new BoolOption(true, "show_button_in_options_screen")
    );

    // - performance
    public static final ConfigOption<Integer> INTERACTION_RENDER_DISTANCE = registerOption(
        new IntOption(6, "interaction_render_distance")
    );
    public static final ConfigOption<Integer> BLOCK_RENDER_DISTANCE = registerOption(
        new IntOption(6, "block_render_distance")
    );
    public static final ConfigOption<Integer> AMBIENT_RENDER_DISTANCE = registerOption(
        new IntOption(3, "ambient_render_distance")
    );
    public static final ConfigOption<Boolean> ADVANCED_PARTICLE_PHYSICS = registerOption(
        new BoolOption(true, "advanced_particle_physics")
    );
    public static final ConfigOption<Boolean> ADDITIONAL_SPARK_FLASH_EFFECT = registerOption(
        new BoolOption(true, "additional_spark_flash_effect")
    );
    public static final ConfigOption<Boolean> SPARK_WATER_EVAPORATION = registerOption(
        new BoolOption(true, "spark_water_evaporation")
    );
    public static final ConfigOption<Boolean> DUST_SPECKS = registerOption(
        new BoolOption(true, "dust_specks")
    );

    // - debug
    public static final ConfigOption<Boolean> DEBUG_EMITTER_BOUNDS = registerOption(
        new BoolOption(false, false)
    );
    public static final ConfigOption<Boolean> DEBUG_PARTICLE_TICK_BOUNDING_BOXES = registerOption(
        new BoolOption(false, false)
    );
    public static final ConfigOption<Boolean> DEBUG_PARTICLE_RENDER_BOUNDING_BOXES = registerOption(
        new BoolOption(false, false)
    );
    public static final ConfigOption<Boolean> DEBUG_EXTRA_INFO_ON_PARTICLE_PACKET_ERROR = registerOption(
        new BoolOption(false, false)
    );

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.GENERAL, option);
    }

    public static void init() {}
}
