package games.enchanted.blockplaceparticles.config.type;

import dev.isxander.yacl3.api.NameableEnum;
import games.enchanted.blockplaceparticles.localisation.ConfigTranslation;
import net.minecraft.network.chat.Component;

public enum BrushParticleBehaviour implements NameableEnum {
    BLOCK_OVERRIDE_OR_VANILLA("block_override_or_vanilla"),
    BLOCK_OVERRIDE_OR_DUST("block_override_or_dust"),
    DISABLED("none");

    private final String translationKey;

    BrushParticleBehaviour(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("eg_particle_interactions.config.enum_option." + ConfigTranslation.BRUSH_PARTICLE_BEHAVIOUR + "." + this.translationKey);
    }
}
