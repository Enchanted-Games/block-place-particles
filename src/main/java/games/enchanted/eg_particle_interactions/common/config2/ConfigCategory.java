package games.enchanted.eg_particle_interactions.common.config2;

public record ConfigCategory(String id) {
    public static ConfigCategory GENERAL = new ConfigCategory("general");
    public static ConfigCategory BLOCK_OVERRIDE = new ConfigCategory("block_override");
    public static ConfigCategory BLOCK_INTERACTIONS = new ConfigCategory("block_interactions");
}
