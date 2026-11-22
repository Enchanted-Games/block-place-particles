package games.enchanted.eg_particle_interactions.common.config;

public record ConfigCategory(String id) {
    public static ConfigCategory GENERAL = new ConfigCategory("general");
    public static ConfigCategory DEBUG = new ConfigCategory("debug");
    public static ConfigCategory BLOCK_INTERACTIONS = new ConfigCategory("block_interactions");
    public static ConfigCategory ITEM_INTERACTIONS = new ConfigCategory("item_interactions");
    public static ConfigCategory ENTITY = new ConfigCategory("entity");
    public static ConfigCategory FLUID_AMBIENT = new ConfigCategory("fluid_ambient");
}
