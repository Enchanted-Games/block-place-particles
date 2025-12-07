package games.enchanted.eg_particle_interactions.common.mixin.mod_compat.yacl;

import dev.isxander.yacl3.api.NameableEnum;
import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BrushParticleBehaviour.class)
public class TranslatableBrushParticleBehaviour implements NameableEnum {
    @Shadow @Final private String id;

    @Override
    public Component getDisplayName() {
        return Component.translatable("eg_particle_interactions.config.enum_option." + ConfigTranslation.BRUSH_PARTICLE_BEHAVIOUR + "." + this.id);
    }
}
