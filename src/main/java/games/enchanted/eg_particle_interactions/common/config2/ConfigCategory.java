package games.enchanted.eg_particle_interactions.common.config2;

public record ConfigCategory(String id) {
    public static ConfigCategory TEST = new ConfigCategory("test");
    public static ConfigCategory TEST_2 = new ConfigCategory("test2");
}
