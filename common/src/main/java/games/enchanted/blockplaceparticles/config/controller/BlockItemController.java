package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;
import dev.isxander.yacl3.gui.utils.ItemRegistryHelper;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;

public class BlockItemController extends AbstractDropdownController<BlockItem> {
    public BlockItemController(Option<BlockItem> option) {
        super(option);
    }

    @Override
    public String getString() {
        return option().pendingValue().toString();
    }

    @Override
    public void setFromString(String value) {
        if(isValueValid(value)) {
            option.requestSet(RegistryHelper.getDefaultedBlockItem(value, null));
        }
    }

    @Override
    public boolean isValueValid(String value) {
        BlockItem blockItemFromValue = RegistryHelper.getDefaultedBlockItem(value, null);
        return blockItemFromValue != null;
    }

    @Override
    protected String getValidValue(String value, int offset) {
        return ItemRegistryHelper.getMatchingItemIdentifiers(value)
            .filter(resourceLocation -> BuiltInRegistries.ITEM.get(resourceLocation) instanceof BlockItem)
            .skip(offset)
            .findFirst()
            .map(ResourceLocation::toString)
            .orElseGet(this::getString);
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new BlockItemControllerElement(this, screen, widgetDimension);
    }
}