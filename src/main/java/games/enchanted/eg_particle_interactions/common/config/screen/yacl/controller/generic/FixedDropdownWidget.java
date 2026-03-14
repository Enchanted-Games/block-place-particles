package games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.generic;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;
import dev.isxander.yacl3.gui.controllers.dropdown.DropdownWidget;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public class FixedDropdownWidget<T> extends DropdownWidget<T> {
    private final GenericListControllerElement<T, ?> genericListControllerElement;

    /**
     * An extension of {@link DropdownWidget} that passes its selectedIndex to the dropdownElement before it gets removed. Intended to be used with or an extension of {@link GenericListControllerElement}
     *
     * @param control         the control
     * @param screen          the screen
     * @param dim             the dim
     * @param dropdownElement the dropdown element
     */
    public FixedDropdownWidget(AbstractDropdownController<T> control, YACLScreen screen, Dimension<Integer> dim, GenericListControllerElement<T, ?> dropdownElement) {
        super(control, screen, dim, dropdownElement);
        this.genericListControllerElement = dropdownElement;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        genericListControllerElement.setLastSelectedDropdownIndex(this.selectedVisibleIndex());
        return super.mouseClicked(mouseButtonEvent, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        genericListControllerElement.setLastSelectedDropdownIndex(this.selectedVisibleIndex());
        return super.keyPressed(keyEvent);
    }
}
