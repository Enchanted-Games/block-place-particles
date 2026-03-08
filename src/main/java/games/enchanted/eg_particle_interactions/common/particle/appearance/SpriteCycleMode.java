package games.enchanted.eg_particle_interactions.common.particle.appearance;

import net.minecraft.util.StringRepresentable;

public enum SpriteCycleMode implements StringRepresentable {
    RANDOM_ON_SPAWN("random_on_spawn"),
    RANDOM_PER_TICK("random_per_tick"),
    AGE_CYCLE("age_cycle");

    final String name;

    SpriteCycleMode(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
