package games.enchanted.blockplaceparticles.mixin.accessor.yacl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(dev.isxander.yacl3.gui.controllers.dropdown.DropdownWidget.class)
public interface DropdownWidgetAccessor {
    @Accessor("firstVisibleIndex")
    void block_place_particle$setFirstVisibleIndex(int newIndex);
}
