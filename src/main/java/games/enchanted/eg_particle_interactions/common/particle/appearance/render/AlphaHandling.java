package games.enchanted.eg_particle_interactions.common.particle.appearance.render;

import net.minecraft.util.StringRepresentable;

public enum AlphaHandling implements StringRepresentable {
    CUTOUT("cutout"),
    TRANSLUCENT("translucent");

    private final String id;

    AlphaHandling(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.id;
    }
}
