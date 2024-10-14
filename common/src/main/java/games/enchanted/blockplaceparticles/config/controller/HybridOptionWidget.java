package games.enchanted.blockplaceparticles.config.controller;


import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class HybridOptionWidget extends ControllerWidget<HybridBlockAndColourController> {
    AbstractWidget firstWidget;
    AbstractWidget secondWidget;
    Dimension<Integer> originalDim;
    float displayRatio;

    /**
     * A widget that renders two options as if they were a single option
     *
     * @param control         the control
     * @param screen          the screen
     * @param widgetDimension the widget dimension
     * @param firstOption     the first option
     * @param secondOption    the second option
     * @param displayRatio    how much space each option controller will have, e.g: 0.75 will give the first option 75% of the width
     */
    public HybridOptionWidget(HybridBlockAndColourController control, YACLScreen screen, Dimension<Integer> widgetDimension, Option<ResourceLocation> firstOption, Option<Color> secondOption, float displayRatio) {
        super(control, screen, widgetDimension);
        this.firstWidget = firstOption.controller().provideWidget(screen, widgetDimension);
        this.secondWidget = secondOption.controller().provideWidget(screen, widgetDimension);
        this.originalDim = widgetDimension;
        this.displayRatio = displayRatio;
    }

    @Override
    public void setDimension(Dimension<Integer> dim) {
        super.setDimension(dim);
        int leftX = dim.x();
        int fullWidth = dim.width() - 2;
        firstWidget.setDimension(dim.withWidth(
            (int) Math.ceil(fullWidth * displayRatio))
        );
        int secondWidgetWidth = (int) Math.ceil(fullWidth * Math.abs(1 - displayRatio));
        secondWidget.setDimension(
            dim.withWidth(secondWidgetWidth)
            .withX(leftX + secondWidgetWidth + 1)
        );
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float v) {
        firstWidget.render(guiGraphics, mouseX, mouseY, v);
        secondWidget.render(guiGraphics, mouseX, mouseY, v);
    }

    @Override
    protected int getHoveredControlWidth() {
        return originalDim.width();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return firstWidget.mouseClicked(mouseX, mouseY, button) || secondWidget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void unfocus() {
        super.unfocus();
        firstWidget.unfocus();
        secondWidget.unfocus();
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        if(this.firstWidget.isFocused()) {
            return ComponentPath.leaf(this.secondWidget);
        }
        if(this.secondWidget.isFocused()) {
            return this.secondWidget.nextFocusPath(focusNavigationEvent);
        }
        return super.nextFocusPath(focusNavigationEvent);
    }

    @Override
    public void setFocused(boolean b) {
        super.setFocused(false);
        if(b) {
            this.firstWidget.setFocused(true);
            this.secondWidget.setFocused(false);
            return;
        };
        if(this.firstWidget.isFocused()) {
            this.firstWidget.setFocused(false);
            this.secondWidget.setFocused(true);
            return;
        };
    }
}