package games.enchanted.eg_particle_interactions.common.config.type;

import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum BrushParticleBehaviour implements StringRepresentable {
    VANILLA_LIKE("block_override_or_vanilla"),
    DUST("block_override_or_dust"),
    NONE("none");

    private final String id;

    BrushParticleBehaviour(String id) {
        this.id = id;
    }

    public Component getTranslatedName() {
        return Component.translatable("eg_particle_interactions.config.enum_option." + ConfigTranslation.BRUSH_PARTICLE_BEHAVIOUR + "." + this.id);
    }

    @Override
    public @NotNull String getSerializedName() {
        return id;
    }
}
