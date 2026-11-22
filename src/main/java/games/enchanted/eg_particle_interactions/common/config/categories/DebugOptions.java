package games.enchanted.eg_particle_interactions.common.config.categories;

import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;

public class DebugOptions {
    public static final ConfigOption<Boolean> DEBUG_KEYBIND_ENABLED = registerOption(
        new BoolOption(false, "keybind_enabled")
    );

    public static final ConfigOption<Boolean> DEBUG_EMITTER_BOUNDS = registerOption(
        new BoolOption(false, "emitter_bounds")
    );
    public static final ConfigOption<Boolean> DEBUG_PARTICLE_TICK_BOUNDING_BOXES = registerOption(
        new BoolOption(false, "particle_tick_bounding_boxes")
    );
    public static final ConfigOption<Boolean> DEBUG_PARTICLE_RENDER_BOUNDING_BOXES = registerOption(
        new BoolOption(false, "particle_render_bounding_boxes")
    );
    public static final ConfigOption<Boolean> DEBUG_EXTRA_INFO_ON_PARTICLE_PACKET_ERROR = registerOption(
        new BoolOption(false, "extra_particle_packet_error_info")
    );
    public static final ConfigOption<Boolean> DEBUG_TEXTURE_LOGGING = registerOption(
        new BoolOption(false, "texture_logging")
    );

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.DEBUG, option);
    }

    public static void init() {}
}
