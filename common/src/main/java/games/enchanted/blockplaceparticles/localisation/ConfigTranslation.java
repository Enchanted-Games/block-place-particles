package games.enchanted.blockplaceparticles.localisation;

import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import net.minecraft.network.chat.Component;

public class ConfigTranslation {
    private static final String CONFIG_KEY_PREFIX = ParticleInteractionsMod.MOD_ID + ".config";

    public static final TranslationKey MOD_CREDITS_KEY = new TranslationKey(ParticleInteractionsMod.MOD_ID + ".mod_credits");
    public static final String GENERAL_CATEGORY = "general";
    public static final String BLOCKS_CONFIG_CATEGORY = "blocks";
    public static final String BLOCK_AMBIENT_CONFIG_CATEGORY = "block_ambient";
    public static final String ITEMS_CONFIG_CATEGORY = "items";
    public static final String ENTITY_PARTICLES_CONFIG_CATEGORY = "entity";
    public static final String FLUIDS_CONFIG_CATEGORY = "fluids";

    public static final String IS_PARTICLE_ENABLED = "is_particle_enabled";
    public static final String IS_PARTICLE_ENABLED_WITH_TYPE = "is_particle_enabled_with_type";
    public static final String IS_OVERRIDE_ENABLED = "is_override_enabled";
    public static final String PARTICLE_SPAWN_CHANCE = "particle_spawn_chance";
    public static final String PARTICLE_SPAWN_CHANCE_WITH_TYPE = "particle_spawn_chance_with_type";
    public static final String MAX_PARTICLES_ON_BLOCK_PLACE = "max_particles_block_place";
    public static final String MAX_PARTICLES_ON_BLOCK_PLACE_ALONG_EDGES = "max_particles_block_place_along_edges";
    public static final String MAX_PARTICLES_ON_BLOCK_BREAK = "max_particles_block_break";
    public static final String MAX_PARTICLES_ON_BLOCK_BREAK_ALONG_AXIS = "max_particles_block_break_along_axis";
    public static final String MAX_PARTICLES_ON_FLUID_PLACE = "max_particles_fluid_place";
    public static final String MAX_PARTICLES_ON_ITEM_USE = "max_particles_item_use";
    public static final String ITEM_USE_PARTICLE_INTENSITY = "item_use_particle_intensity";
    public static final String SPAWN_PARTICLE_ON_ITEM_USE = "spawn_particle_on_item_use";
    public static final String SPAWN_BLOCK_PARTICLE_ON_PLACE = "spawn_block_particle_on_place";
    public static final String SPAWN_BLOCK_PARTICLE_ON_BREAK = "spawn_block_particle_on_break";
    public static final String SPAWN_FLUID_PARTICLE_ON_PLACE = "spawn_fluid_particle_on_place";
    public static final String SPAWN_PARTICLE_WHEN_MINECART_AT_MAX_SPEED = "spawn_particle_at_minecart_max_speed";
    public static final String MINECART_WHEEL_PARTICLE_AMOUNT = "minecart_wheel_particle_amount";
    public static final String MINECART_ONLY_WITH_PASSENGER = "minecart_only_with_passenger";
    public static final String MAX_PARTICLES_ON_BLOCK_CRAFT = "max_particles_block_craft";
    public static final String SPAWN_PARTICLE_ON_ENTITY_HURT = "spawn_particle_on_entity_hurt";
    public static final String ENTITY_AMBIENT_PARTICLE_SPAWN_CHANCE = "entity_ambient_particle_spawn_chance";
    public static final String AMOUNT_TO_SPAWN_ON_ENTITY_HURT = "amount_to_spawn_on_entity_hurt";
    public static final String PARTICLE_EFFECT_RENDER_DISTANCE = "particle_effect_render_distance";
    public static final String AMOUNT_TO_SPAWN_ON_INTERACT = "amount_to_spawn_on_interact";

    public static final String BRUSH_PARTICLE_BEHAVIOUR = "brush_particle_behaviour";

    public static final String PIXEL_CONSISTENT_TERRAIN_PARTICLES = "pixel_consistent_terrain_particles";
    public static final String PARTICLE_ZFIGHTING_FIX = "particle_zfighting_fix";
    public static final String AUTO_COLLAPSE_CONFIG_LISTS = "auto_collapse_config_lists";

    public static final String PARTICLE_PHYSICS_ENABLED = "particle_physics_enabled";

    public static final String SPARKS_ADDITIONAL_FLASH_EFFECT = "sparks_additional_flash_effect";
    public static final String SPARKS_WATER_EVAPORATION = "sparks_water_evaporation";
    public static final String DUST_ADDITIONAL_SPECKS = "dust_additional_specks";

    public static final String TOGGLE_INTERACTION_DEBUG_LOGS = "toggle_debug_logs";
    public static final String TOGGLE_TEXTURE_DEBUG_LOGS = "toggle_texture_debug_logs";
    public static final String DEBUG_SHOW_EMITTER_BOUNDS = "debug_show_emitter_bounds";

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
