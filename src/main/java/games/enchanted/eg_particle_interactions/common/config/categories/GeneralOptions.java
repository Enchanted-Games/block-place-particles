package games.enchanted.eg_particle_interactions.common.config.categories;

import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;

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

    public static final ConfigOption<Boolean> PRESERVE_VANILLA_NETWORK_COMPATIBILITY = registerOption(
        new BoolOption(false, "preserve_vanilla_network_compatibility")
    );

    // - particle performance
    public static final ConfigOption<Boolean> PARTICLE_BOUNCE_PHYSICS = registerOption(
        new BoolOption(true, "particle_bounce_physics")
    );
    public static final ConfigOption<Boolean> PARTICLE_FLUID_REACTIVITY = registerOption(
        new BoolOption(true, "fluid_reactivity")
    );
    public static final ConfigOption<Boolean> PARTICLE_ALLOW_EMISSIONS = registerOption(
        new BoolOption(true, "particle_allow_emissions")
    );

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.GENERAL, option);
    }

    public static void init() {}
}
