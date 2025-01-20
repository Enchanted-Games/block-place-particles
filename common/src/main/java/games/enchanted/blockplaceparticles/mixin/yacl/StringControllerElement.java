package games.enchanted.blockplaceparticles.mixin.yacl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(dev.isxander.yacl3.gui.controllers.string.StringControllerElement.class)
public abstract class StringControllerElement {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/gui/controllers/string/StringControllerElement;isHovered()Z", ordinal = 0),
        method = "drawValueText(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
    )
    private boolean bypassHoverCheckForGenericListControllerElements(dev.isxander.yacl3.gui.controllers.string.StringControllerElement instance, Operation<Boolean> original) {
        if((Object) this instanceof GenericListControllerElement<?,?>) return true;
        return original.call(instance);
    }
}
