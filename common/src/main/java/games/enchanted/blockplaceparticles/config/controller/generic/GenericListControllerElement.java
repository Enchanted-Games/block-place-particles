package games.enchanted.blockplaceparticles.config.controller.generic;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownControllerElement;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.mixin.accessor.yacl.DropdownWidgetAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericListControllerElement<T, R extends AbstractDropdownController<T>> extends AbstractDropdownControllerElement<T, T> {
    private static final ResourceLocation MISSING_ITEM_ICON_SPRITE = ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "missing_item_icon");

    private final R controller;
    protected T currentItem = null;
    protected Map<ResourceLocation, T> matchingItems = new HashMap<>();
    int lastKnownSelectedDropdownIndex = 0;

    public GenericListControllerElement(R control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
        this.controller = control;
    }

    public R getController() {
        return controller;
    }

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, Dimension<Integer> entryDimension, T value) {
        super.renderDropdownEntry(graphics, entryDimension, value);
    }

    @Override
    protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var oldDimension = getDimension();
        setDimension(getDimension().withWidth(getDimension().width() - getDecorationPadding()));
        super.drawValueText(graphics, mouseX, mouseY, delta);
        setDimension(oldDimension);
        if (currentItem != null) {
            this.renderItemIcon(graphics, getItemToRender(currentItem), getDimension().xLimit() - getXPadding() - getDecorationPadding() + 2, getDimension().y() + 2);
        }
    }

    protected void renderItemIcon(GuiGraphics graphics, Item item, int x, int y) {
        if(item == null) return;;
        if(item == Items.AIR) {
            graphics.blitSprite(RenderType::guiTextured, MISSING_ITEM_ICON_SPRITE, x, y, 16, 16);
            return;
        }
        graphics.renderFakeItem(new ItemStack(item), x, y);
    }

    @Override
    protected void drawHoveredControl(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.drawHoveredControl(graphics, mouseX, mouseY, delta);

        Component text = this.getHoverTooltipText();
        if(text == null) return;
        Dimension<Integer> dimension = this.getDimension();

        graphics.renderTooltip(Minecraft.getInstance().font, text, dimension.x(), dimension.y());
    }

    public @Nullable Component getHoverTooltipText() {
        return Component.literal(this.getController().option().pendingValue().toString());
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if(this.dropdownWidget != null) {
            ((DropdownWidgetAccessor) this.dropdownWidget).block_place_particle$setFirstVisibleIndex(0);
            this.dropdownWidget.scrollUp();
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public String getString(T val) {
        return val.toString();
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX + this.getDecorationPadding(), mouseY, button);
    }

    public void setLastSelectedDropdownIndex(int index) {
        this.lastKnownSelectedDropdownIndex = index;
    }
    public int getLastSelectedDropdownIndex() {
        return this.lastKnownSelectedDropdownIndex;
    }

    @Override
    public void createDropdownWidget() {
        dropdownVisible = true;
        dropdownWidget = new FixedDropdownWidget<>(controller, screen, getDimension(), this);
        screen.addPopupControllerWidget(dropdownWidget);
    }

    @Override
    protected Component getValueText() {
        if (inputField.isEmpty() || controller == null) {
            return super.getValueText();
        }

        if (inputFieldFocused) {
            return Component.literal(inputField);
        }

        return getRenderedValueText();
    }

    public abstract Item getItemToRender(T value);

    public abstract Component getRenderedValueText();
}
