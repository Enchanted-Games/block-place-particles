package games.enchanted.eg_particle_interactions.common.config2;

public record ConfigCategory(String id) {
    public static ConfigCategory GENERAL = new ConfigCategory("general");
    public static ConfigCategory TEST_2 = new ConfigCategory("test2");
}
