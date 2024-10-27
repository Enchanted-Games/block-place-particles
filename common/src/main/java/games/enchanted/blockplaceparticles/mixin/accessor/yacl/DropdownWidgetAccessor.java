package games.enchanted.blockplaceparticles.mixin.accessor.yacl;

import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownControllerElement;
import dev.isxander.yacl3.gui.controllers.dropdown.DropdownWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(dev.isxander.yacl3.gui.controllers.dropdown.DropdownWidget.class)
public interface DropdownWidgetAccessor {
    @Accessor("firstVisibleIndex")
    void setFirstVisibleIndex(int newIndex);
}
