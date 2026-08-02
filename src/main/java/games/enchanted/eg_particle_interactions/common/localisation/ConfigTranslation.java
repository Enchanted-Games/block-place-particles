package games.enchanted.eg_particle_interactions.common.localisation;

import games.enchanted.eg_particle_interactions.common.Constants;
import net.minecraft.network.chat.Component;

public class ConfigTranslation {
    private static final String CONFIG_KEY_PREFIX = Constants.MOD_ID + ".config";
    private static final String FALLBACK_CONFIG_KEY_PREFIX = Constants.MOD_ID + ".fallback_config";
    private static final String NOT_IN_LEVEL_CONFIG_KEY_PREFIX = Constants.MOD_ID + ".not_in_level";

    public static final TranslationKey MOD_CREDITS_KEY = new TranslationKey(Constants.MOD_ID + ".mod_credits");
    public static final String GENERAL_CATEGORY = "general";
    public static final String BLOCK_AMBIENT_CONFIG_CATEGORY = "block_ambient";
    public static final String ITEMS_CONFIG_CATEGORY = "items";
    public static final String ENTITY_PARTICLES_CONFIG_CATEGORY = "entity";
    public static final String FLUIDS_CONFIG_CATEGORY = "fluids";

    public static final String IS_PARTICLE_ENABLED = "is_particle_enabled";
    public static final String IS_PARTICLE_ENABLED_WITH_TYPE = "is_particle_enabled_with_type";
    public static final String PARTICLE_SPAWN_CHANCE_WITH_TYPE = "particle_spawn_chance_with_type";
    public static final String MAX_PARTICLES_ON_BLOCK_PLACE = "max_particles_block_place";
    public static final String MAX_PARTICLES_ON_BLOCK_BREAK = "max_particles_block_break";
    public static final String MAX_PARTICLES_ON_FLUID_PLACE = "max_particles_fluid_place";
    public static final String MAX_PARTICLES_ON_ITEM_USE = "max_particles_item_use";
    public static final String SPAWN_PARTICLE_ON_ITEM_USE = "spawn_particle_on_item_use";
    public static final String SPAWN_ON_HONEY_COLLECTED = "spawn_particle_on_honey_collected";
    public static final String MAX_PARTICLES_ON_HONEY_COLLECTED = "max_particles_on_honey_collected";
    public static final String REPLACE_VANILLA_PARTICLES = "replace_vanilla_honey_particles";
    public static final String SPAWN_FLUID_PARTICLE_ON_PLACE = "spawn_fluid_particle_on_place";
    public static final String SPAWN_PARTICLE_WHEN_MINECART_AT_MAX_SPEED = "spawn_particle_at_minecart_max_speed";
    public static final String MINECART_WHEEL_PARTICLE_AMOUNT = "minecart_wheel_particle_amount";
    public static final String MINECART_ONLY_WITH_PASSENGER = "minecart_only_with_passenger";
    public static final String MAX_PARTICLES_ON_BLOCK_CRAFT = "max_particles_block_craft";
    public static final String PARTICLE_EFFECT_RENDER_DISTANCE = "particle_effect_render_distance";
    public static final String AMOUNT_TO_SPAWN_ON_INTERACT = "amount_to_spawn_on_interact";
    public static final String AMOUNT_TO_SPAWN_ON_LIGHTNING_STRIKE = "amount_to_spawn_on_lightning_strike";
    public static final String ARE_VANILLA_FURNACE_PARTICLES_ENABLED = "are_vanilla_furnace_particles_enabled";
    public static final String AMOUNT_TO_SPAWN_ON_CUBE_CONSUME = "amount_to_spawn_cube_consume";
    public static final String ACCURATE_MACE_SMASH = "accurate_mace_smash";

    public static final String BRUSH_PARTICLE_BEHAVIOUR = "brush_particle_behaviour";
    public static final String LEAVES_PARTICLE_BEHAVIOUR = "leaves_particle_behaviour";

    public static final String PIXEL_CONSISTENT_TERRAIN_PARTICLES = "pixel_consistent_terrain_particles";
    public static final String PARTICLE_ZFIGHTING_FIX = "particle_zfighting_fix";
    public static final String PROJECTILE_PARTICLE_VELOCITY_FIX = "projectile_particle_velocity_fix";
    public static final String AUTO_COLLAPSE_CONFIG_LISTS = "auto_collapse_config_lists";
    public static final String FIREFLY_FIXES = "firefly_fixes";
    public static final String SHOW_BUTTON_IN_OPTIONS_SCREEN = "show_button_in_options_screen";

    public static final String RENDER_DISTANCE_INTERACTION = "render_distance_interaction";
    public static final String RENDER_DISTANCE_BLOCK = "render_distance_block";
    public static final String RENDER_DISTANCE_AMBIENT = "render_distance_ambient";

    public static final String PARTICLE_FLUID_REACTIVITY = "particle_fluid_reactivity";
    public static final String PARTICLE_BOUNCE_PHYSICS_ENABLED = "particle_bounce_physics_enabled";
    public static final String PARTICLE_ALLOW_EMISSIONS_ENABLED = "particle_allow_emissions_enabled";

    public static final String DEBUG_SHOW_EMITTER_BOUNDS = "debug_show_emitter_bounds";
    public static final String DEBUG_PARTICLE_TICK_BOUNDING_BOXES = "debug_particle_tick_bounding_boxes";
    public static final String DEBUG_PARTICLE_RENDER_BOUNDING_BOXES = "debug_particle_render_bounding_boxes";
    public static final String DEBUG_EXTRA_INFO_ON_PARTICLE_PACKET_ERROR = "debug_info_on_particle_packet_error";
    public static final String DEBUG_TEXTURE_LOGGING = "debug_texture_logging";

    public static Component createDesc(TranslationKey translationKey) {
        return translationKey.copy().append(".desc").toComponent();
    }
    public static Component createPlaceholder(Component component, Object... args) {
        return Component.literal(component.getString().formatted(args));
    }
    public static Component createPlaceholder(Component component, String placeholder) {
        return Component.literal(component.getString().formatted(placeholder, placeholder, placeholder, placeholder, placeholder, placeholder, placeholder, placeholder));
    }

    public static TranslationKey getConfigTitle() {
        return new TranslationKey(CONFIG_KEY_PREFIX + ".title");
    }

    public static TranslationKey getCategoryName(String category) {
        return new TranslationKey(CONFIG_KEY_PREFIX + "." + category);
    }

    public static TranslationKey getGroupName(String category, String group) {
        return new TranslationKey(CONFIG_KEY_PREFIX + "." + category + "." + group);
    }

    public static TranslationKey getOption(String category, String group, String option) {
        return new TranslationKey(CONFIG_KEY_PREFIX + "." + category + "." + group + "." + option);
    }

    public static TranslationKey getGlobalOption(String option) {
        return new TranslationKey(CONFIG_KEY_PREFIX + ".global_option." + option);
    }

    public static TranslationKey getParticleType(String particleKey) {
        return new TranslationKey(CONFIG_KEY_PREFIX + ".particle_type." + particleKey);
    }

    public static Component createPlaceholderTranslatableComponent(String translationKey, Object... args) {
        return Component.literal(Component.translatable(translationKey).getString().formatted(args));
    }


    public static TranslationKey getFallbackConfigTitle() {
        return new TranslationKey(FALLBACK_CONFIG_KEY_PREFIX + ".title");
    }
    public static TranslationKey getFallbackConfigBody() {
        return new TranslationKey(FALLBACK_CONFIG_KEY_PREFIX + ".body");
    }

    public static TranslationKey getDownloadYACLButtonMessage() {
        return new TranslationKey("eg_particle_interactions.button.download_yacl");
    }

    public static TranslationKey getNotInLevelConfigTitle() {
        return new TranslationKey(NOT_IN_LEVEL_CONFIG_KEY_PREFIX + ".title");
    }
    public static TranslationKey getNotInLevelConfigBody() {
        return new TranslationKey(NOT_IN_LEVEL_CONFIG_KEY_PREFIX + ".body");
    }


    public static class TranslationKey {
        String key;
        public TranslationKey(String key) {
            this.key = key;
        }

        TranslationKey append(String string) {
            this.key = this.key + string;
            return this;
        }

        TranslationKey copy() {
            return new TranslationKey(this.key);
        }

        public Component toComponent() {
            return Component.translatable(this.key);
        }

        public String toString() {
            return this.key;
        }
    }
}
