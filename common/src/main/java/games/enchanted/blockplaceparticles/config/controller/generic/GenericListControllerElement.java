package games.enchanted.blockplaceparticles.config.controller.generic;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownControllerElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericListControllerElement<T, R extends AbstractDropdownController<T>> extends AbstractDropdownControllerElement<T, ResourceLocation> {
    private final R controller;
    protected T currentItem = null;
    protected Map<ResourceLocation, T> matchingItems = new HashMap<>();

    public GenericListControllerElement(R control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
        this.controller = control;
    }

    public R getController() {
        return controller;
    }

    @Override
    protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var oldDimension = getDimension();
        setDimension(getDimension().withWidth(getDimension().width() - getDecorationPadding()));
        super.drawValueText(graphics, mouseX, mouseY, delta);
        setDimension(oldDimension);
        if (currentItem != null) {
            graphics.renderFakeItem(new ItemStack(getItemToRender(currentItem)), getDimension().xLimit() - getXPadding() - getDecorationPadding() + 2, getDimension().y() + 2);
        }
    }

    @Override
    public abstract List<ResourceLocation> computeMatchingValues();

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, Dimension<Integer> entryDimension, ResourceLocation identifier) {
        T item = matchingItems.get(identifier);
        if(item == null) return;
        super.renderDropdownEntry(graphics, entryDimension, identifier);
        graphics.renderFakeItem(
            new ItemStack(getItemToRender(item)),
            entryDimension.xLimit() - 2,
            entryDimension.y() + 1
        );
    }

    @Override
    public String getString(ResourceLocation identifier) {
        return identifier.toString();
    }

    @Override
    protected int getDecorationPadding() {
        return 16;
    }

    @Override
    protected int getDropdownEntryPadding() {
        return 4;
    }

    @Override
    protected int getControlWidth() {
        return super.getControlWidth() + getDecorationPadding();
    }

    @Override
    protected Component getValueText() {
        if (inputField.isEmpty() || controller == null)
            return super.getValueText();

        if (inputFieldFocused)
            return Component.literal(inputField);

        return getRenderedValueText();
    }

    public abstract Item getItemToRender(T value);

    public abstract Component getRenderedValueText();
}
