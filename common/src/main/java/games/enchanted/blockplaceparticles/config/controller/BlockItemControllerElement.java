package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.utils.ItemRegistryHelper;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class BlockItemControllerElement extends GenericListControllerElement<BlockItem, BlockItemController> {
    public BlockItemControllerElement(BlockItemController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    public List<ResourceLocation> computeMatchingValues() {
        List<ResourceLocation> resourceLocations = ItemRegistryHelper.getMatchingItemIdentifiers(inputField).toList();
        ArrayList<ResourceLocation> blockItemLocations = new ArrayList<>();
        currentItem = getDefaultedItem(inputField, null);
        for (ResourceLocation resourceLocation : resourceLocations) {
            Item itemFromLocation = BuiltInRegistries.ITEM.get(resourceLocation);
            if (isValueValidType(itemFromLocation)) {
                matchingItems.put(resourceLocation, (BlockItem) itemFromLocation);
                blockItemLocations.add(resourceLocation);
            }
        }
        return blockItemLocations;
    }

    BlockItem getDefaultedItem(String location, BlockItem defaultItem) {
        return RegistryHelper.getDefaultedBlockItem(location, defaultItem);
    }

    boolean isValueValidType(Item item) {
        return item instanceof BlockItem;
    }

    @Override
    public Item getItemToRender(BlockItem value) {
        return value;
    }

    @Override
    public Component getRenderedValueText() {
        return getController().option().pendingValue().getDescription();
    }
}
