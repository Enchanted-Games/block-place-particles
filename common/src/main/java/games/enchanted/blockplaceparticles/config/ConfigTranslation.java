package games.enchanted.blockplaceparticles.config;

import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import net.minecraft.network.chat.Component;

public class ConfigTranslation {
    private static final String CONFIG_KEY_PREFIX = ParticleInteractionsMod.MOD_ID + ".config";

    public static final String BLOCKS_CONFIG_CATEGORY = "blocks";
    public static final String BLOCK_AMBIENT_CONFIG_CATEGORY = "block_ambient";
    public static final String ITEMS_CONFIG_CATEGORY = "items";
    public static final String ENTITY_PARTICLES_CONFIG_CATEGORY = "entity";
    public static final String FLUIDS_CONFIG_CATEGORY = "fluids";

    public static final String ARE_PARTICLES_ENABLED = "is_particle_enabled";
    public static final String PARTICLE_SPAWN_CHANCE = "particle_spawn_chance";
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

    public static Component createDesc(TranslationKey translationKey) {
        return translationKey.append(".desc").toComponent();
    }
    public static Component createPlaceholder(Component component, Object... args) {
        return Component.literal(component.getString().formatted(args));
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

        Component toComponent() {
            return Component.translatable(this.key);
        }

        public String toString() {
            return this.key;
        }
    }
}
