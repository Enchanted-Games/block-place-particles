package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockLocationControllerElement extends GenericListControllerElement<ResourceLocation, BlockLocationController> {
    public BlockLocationControllerElement(BlockLocationController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    public List<ResourceLocation> computeMatchingValues() {
        List<ResourceLocation> resourceLocations = RegistryHelper.getMatchingIdentifiers(inputField, BuiltInRegistries.BLOCK).toList();
        ArrayList<ResourceLocation> blockLocations = new ArrayList<>();
        currentItem = RegistryHelper.validateBlockLocationWithFallback(inputField, null);
        for (ResourceLocation resourceLocation : resourceLocations) {
            Block blockFromLocation = RegistryHelper.getBlockFromLocation(resourceLocation);
            if (blockFromLocation.defaultBlockState().isAir()) continue;
            matchingItems.put(resourceLocation, RegistryHelper.getLocationFromBlock(blockFromLocation));
            blockLocations.add(resourceLocation);
        }
        return blockLocations;
    }

    @Override
    public Item getItemToRender(ResourceLocation value) {
        return RegistryHelper.getBlockFromLocation(value).asItem();
    }

    @Override
    public Component getRenderedValueText() {
        return Component.translatable( RegistryHelper.getBlockFromLocation(getController().option().pendingValue()).getDescriptionId() );
    }
}
