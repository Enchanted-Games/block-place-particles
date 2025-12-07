package games.enchanted.eg_particle_interactions.common.mixin.mod_compat.yacl;

import dev.isxander.yacl3.api.NameableEnum;
import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BrushParticleBehaviour.class)
public abstract class TranslatableBrushParticleBehaviourMixin implements NameableEnum {
    @Shadow public abstract @NotNull Component getTranslatedName();

    @Override
    public Component getDisplayName() {
        return getTranslatedName();
    }
}
