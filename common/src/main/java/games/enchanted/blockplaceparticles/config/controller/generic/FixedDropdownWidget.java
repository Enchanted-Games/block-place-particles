package games.enchanted.blockplaceparticles.config.controller.generic;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;
import dev.isxander.yacl3.gui.controllers.dropdown.DropdownWidget;

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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        genericListControllerElement.setLastSelectedDropdownIndex(this.selectedVisibleIndex());
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        genericListControllerElement.setLastSelectedDropdownIndex(this.selectedVisibleIndex());
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
