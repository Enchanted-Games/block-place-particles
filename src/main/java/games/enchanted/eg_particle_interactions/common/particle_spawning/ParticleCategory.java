package games.enchanted.eg_particle_interactions.common.particle_spawning;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;

import java.util.function.Supplier;

public enum ParticleCategory {
    INTERACTION("interaction", GeneralOptions.INTERACTION_RENDER_DISTANCE::getValue),
    BLOCK_PLACE_OR_BREAK("block_place_or_break", GeneralOptions.BLOCK_RENDER_DISTANCE::getValue),
    AMBIENT("ambient", GeneralOptions.AMBIENT_RENDER_DISTANCE::getValue);

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
