package games.enchanted.eg_particle_interactions.common.mixin.mod_compat.yacl.accessor;

import dev.isxander.yacl3.gui.controllers.dropdown.DropdownWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DropdownWidget.class)
public interface DropdownWidgetAccessor {
    @Accessor(value = "firstVisibleIndex", remap = false)
    void eg_particle_interactions$setFirstVisibleIndex(int newIndex);
}
