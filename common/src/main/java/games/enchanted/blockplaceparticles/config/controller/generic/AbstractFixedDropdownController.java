package games.enchanted.blockplaceparticles.config.controller.generic;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;

import java.util.List;

public abstract class AbstractFixedDropdownController<T> extends AbstractDropdownController<T> {
    GenericListControllerElement<?, ?> widget;

    protected AbstractFixedDropdownController(Option<T> option) {
        super(option);
    }

    public GenericListControllerElement<?, ?> getWidget() {
        return widget;
    }

    @Override
    public abstract String getString();

    @Override
    public void setFromString(String value) {
        int index = 0;
        if(widget != null) {
            index = widget.getLastSelectedDropdownIndex();
        }
        this.setFromStringIndex(value, index);
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        this.widget = this.createWidget(screen, widgetDimension);
        return widget;
    }

    protected String getValueFromDropdown(int offset) {
        List<?> matchingValues = getWidget().computeMatchingValues();
        if(offset < 0) return null;
        if(offset <= matchingValues.size() - 1) {
            return matchingValues.get(offset).toString();
        }
        return null;
    }

    /**
     * Should set the `option` to a valid value based on the `value` passed in
     *
     * @param value the value
     * @param index the selected index of the dropdown element
     */
    public abstract void setFromStringIndex(String value, int index);

    public abstract GenericListControllerElement<T, ?> createWidget(YACLScreen screen, Dimension<Integer> widgetDimension);
}
