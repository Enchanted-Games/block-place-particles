package games.enchanted.eg_particle_interactions.common.config.type;

import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum LeavesParticleBehaviour implements StringRepresentable {
    CUSTOM_OR_VANILLA("custom_or_vanilla"),
    VANILLA_ONLY("vanilla_only"),
    NONE("none");

    private final String id;

    LeavesParticleBehaviour(String id) {
        this.id = id;
    }

    /**
     * Used in {@link games.enchanted.eg_particle_interactions.common.mixin.mod_compat.yacl.TranslatableLeavesParticleBehaviourMixin}
     * to show proper names in YACL
     *
     * @return the translated name
     */
    public Component getTranslatedName() {
        return Component.translatable("eg_particle_interactions.config.enum_option." + ConfigTranslation.LEAVES_PARTICLE_BEHAVIOUR + "." + this.id);
    }

    @Override
    public @NotNull String getSerializedName() {
        return id;
    }
}
