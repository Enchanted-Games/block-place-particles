package games.enchanted.eg_particle_interactions.common.mixin.mod_compat.yacl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.yacl3.gui.controllers.string.StringControllerElement;
import games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.generic.GenericListControllerElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StringControllerElement.class)
public abstract class StringControllerElementMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/gui/controllers/string/StringControllerElement;isHovered()Z", ordinal = 0),
        method = "extractValueText"
    )
    private boolean eg_particle_interactions$bypassComponentShorteningWhenUnhovered(StringControllerElement instance, Operation<Boolean> original) {
        if((Object) this instanceof GenericListControllerElement<?,?>) return true;
        return original.call(instance);
    }
}
