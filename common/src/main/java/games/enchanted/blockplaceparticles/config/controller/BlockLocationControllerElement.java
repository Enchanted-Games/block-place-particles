package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.registry.BlockLocation;
import games.enchanted.blockplaceparticles.registry.RegistryHelpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockLocationControllerElement extends GenericListControllerElement<BlockLocation, BlockLocationController> {
    public BlockLocationControllerElement(BlockLocationController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    public List<BlockLocation> computeMatchingValues() {
        if(this.inputField.startsWith("#")) {
            // tag logic
            this.currentItem = new BlockLocation(ResourceLocation.parse(this.inputField.replace("#", "")), true);
            return List.of();
        }
        List<ResourceLocation> blockOrTagResourceLocations = RegistryHelpers.getMatchingLocations(this.inputField, BuiltInRegistries.BLOCK).toList();
        ArrayList<BlockLocation> blockLocations = new ArrayList<>();
        ResourceLocation validatedLoc = RegistryHelpers.validateBlockLocationWithFallback(this.inputField, null);
        this.currentItem = validatedLoc == null ? null : new BlockLocation(validatedLoc);
        for (ResourceLocation blockLocation : blockOrTagResourceLocations) {
            Block blockFromLocation = RegistryHelpers.getBlockFromLocation(blockLocation);
            if (blockFromLocation.defaultBlockState().isAir()) continue;
            this.matchingItems.put(blockLocation, new BlockLocation(RegistryHelpers.getLocationFromBlock(blockFromLocation)));
            blockLocations.add(new BlockLocation(blockLocation));
        }
        return blockLocations;
    }

    @Override
    public Item getItemToRender(BlockLocation value) {
        if(value.isTag()) return Items.AIR;
        return RegistryHelpers.getBlockFromLocation(value.location()).asItem();
    }

    @Override
    public Component getRenderedValueText() {
        BlockLocation currentValue = getController().option().pendingValue();
        if(currentValue.isTag()) {
            return Component.literal("block tag: " + currentValue.location().toString());
        }
        return Component.translatable( RegistryHelpers.getBlockFromLocation(currentValue.location()).getDescriptionId() );
    }

    @Override
    public @Nullable Component getHoverTooltipText() {
        return super.getHoverTooltipText();
    }
}
