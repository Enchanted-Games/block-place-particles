package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.config.ConfigHandler;

import java.util.function.Supplier;

public enum ParticleCategory {
    INTERACTION("interaction", () -> ConfigHandler.general_interactionRenderDistance),
    BLOCK_PLACE_OR_BREAK("block_place_or_break", () -> ConfigHandler.general_blockRenderDistance),
    AMBIENT("ambient", () -> ConfigHandler.general_ambientRenderDistance);

    private final String name;
    private final Supplier<Integer> renderDistanceGetter;

    ParticleCategory(String name, Supplier<Integer> renderDistanceGetter) {
        this.name = name;
        this.renderDistanceGetter = renderDistanceGetter;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    /**
     * Gets the max distance that particles in this category should be visible from
     *
     * @return the max distance in chunks
     */
    public int getMaxDistance() {
        return this.renderDistanceGetter.get();
    }
}
