package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownControllerElement;
import dev.isxander.yacl3.gui.utils.ItemRegistryHelper;
import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.config.util.BlockItemUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockItemControllerElement extends AbstractDropdownControllerElement<BlockItem, ResourceLocation> {
    private final BlockItemController blockItemController;
    protected BlockItem currentItem = null;
    protected Map<ResourceLocation, BlockItem> matchingItems = new HashMap<>();

    public BlockItemControllerElement(BlockItemController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
        this.blockItemController = control;
    }

    @Override
    protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var oldDimension = getDimension();
        setDimension(getDimension().withWidth(getDimension().width() - getDecorationPadding()));
        super.drawValueText(graphics, mouseX, mouseY, delta);
        setDimension(oldDimension);
        if (currentItem != null) {
            graphics.renderFakeItem(new ItemStack(currentItem), getDimension().xLimit() - getXPadding() - getDecorationPadding() + 2, getDimension().y() + 2);
        }
    }

    @Override
    public List<ResourceLocation> computeMatchingValues() {
        List<ResourceLocation> resourceLocations = ItemRegistryHelper.getMatchingItemIdentifiers(inputField).toList();
        ArrayList<ResourceLocation> blockItemLocations = new ArrayList<>();
        currentItem = BlockItemUtil.getDefaultedBlockItem(inputField, null);
        for (ResourceLocation resourceLocation : resourceLocations) {
            Item blockItemFromLocation = BuiltInRegistries.ITEM.get(resourceLocation);
            if (blockItemFromLocation instanceof BlockItem) {
                matchingItems.put(resourceLocation, (BlockItem) blockItemFromLocation);
                blockItemLocations.add(resourceLocation);
            }
        }
        return blockItemLocations;
    }

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, Dimension<Integer> entryDimension, ResourceLocation identifier) {
        BlockItem item = matchingItems.get(identifier);
        if(item == null) return;
        super.renderDropdownEntry(graphics, entryDimension, identifier);
        graphics.renderFakeItem(
            new ItemStack(item),
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
        if (inputField.isEmpty() || blockItemController == null)
            return super.getValueText();

        if (inputFieldFocused)
            return Component.literal(inputField);

        return blockItemController.option().pendingValue().getDescription();
    }
}
