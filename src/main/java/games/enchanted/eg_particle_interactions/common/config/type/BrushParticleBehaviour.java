package games.enchanted.eg_particle_interactions.common.config.type;

import dev.isxander.yacl3.api.NameableEnum;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum BrushParticleBehaviour implements NameableEnum, StringRepresentable {
    BLOCK_OVERRIDE_OR_VANILLA("block_override_or_vanilla"),
    BLOCK_OVERRIDE_OR_DUST("block_override_or_dust"),
    DISABLED("none");

    private final String id;

    BrushParticleBehaviour(String id) {
        this.id = id;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("eg_particle_interactions.config.enum_option." + ConfigTranslation.BRUSH_PARTICLE_BEHAVIOUR + "." + this.id);
    }

    @Override
    public @NotNull String getSerializedName() {
        return id;
    }
}
