package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.registry.BlockOrTagLocation;
import games.enchanted.blockplaceparticles.registry.RegistryHelpers;
import games.enchanted.blockplaceparticles.util.TextUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockLocationControllerElement extends GenericListControllerElement<BlockOrTagLocation, BlockLocationController> {
    private static final ResourceLocation BLOCK_TAG_ICON = ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "block_tag_icon");

    public BlockLocationControllerElement(BlockLocationController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    public List<BlockOrTagLocation> computeMatchingValues() {
        if(this.inputField.startsWith("#"))  {
            // tag logic
            String value = this.inputField.replace("#", "");
            List<ResourceLocation> tagResourceLocations = RegistryHelpers.getMatchingTagLocations(value, BuiltInRegistries.BLOCK).toList();
            ArrayList<BlockOrTagLocation> tagLocations = new ArrayList<>();
            BlockOrTagLocation validatedLoc = RegistryHelpers.validateBlockOrTagLocationWithFallback(this.inputField, null);
            this.currentItem = validatedLoc;

            if(!validatedLoc.location().getPath().isEmpty()) {
                this.matchingItems.put(validatedLoc.location(), validatedLoc);
                tagLocations.add(validatedLoc);
            }

            for (ResourceLocation tagResourceLocation : tagResourceLocations) {
                this.matchingItems.put(tagResourceLocation, validatedLoc);
                tagLocations.add(new BlockOrTagLocation(tagResourceLocation, true));
            }
            return tagLocations;
        }
        List<ResourceLocation> blockResourceLocations = RegistryHelpers.getMatchingLocations(this.inputField, BuiltInRegistries.BLOCK).toList();
        ArrayList<BlockOrTagLocation> blockOrTagLocations = new ArrayList<>();
        ResourceLocation validatedLoc = RegistryHelpers.validateBlockLocationWithFallback(this.inputField, null);
        this.currentItem = validatedLoc == null ? null : new BlockOrTagLocation(validatedLoc);
        for (ResourceLocation blockLocation : blockResourceLocations) {
            Block blockFromLocation = RegistryHelpers.getBlockFromLocation(blockLocation);
            if (blockFromLocation.defaultBlockState().isAir()) continue;
            this.matchingItems.put(blockLocation, new BlockOrTagLocation(RegistryHelpers.getLocationFromBlock(blockFromLocation)));
            blockOrTagLocations.add(new BlockOrTagLocation(blockLocation));
        }
        return blockOrTagLocations;
    }

    @Override
    public Item getItemToRender(BlockOrTagLocation value) {
        if(value.isTag()) return null;
        return RegistryHelpers.getBlockFromLocation(value.location()).asItem();
    }

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, Dimension<Integer> entryDimension, BlockOrTagLocation blockOrTagLocation) {
        super.renderDropdownEntry(graphics, entryDimension, blockOrTagLocation);
        if(blockOrTagLocation.isTag()) {
            // render tag icon
            renderTagIcon( graphics, entryDimension.xLimit() - 2, entryDimension.y() + 1);
            return;
        }
        this.renderItemIcon(graphics, getItemToRender(blockOrTagLocation), entryDimension.xLimit() - 2, entryDimension.y() + 1);
    }

    @Override
    public Component getRenderedValueText() {
        BlockOrTagLocation currentValue = this.getController().option().pendingValue();
        if(currentValue.isTag()) {
            return TextUtil.formatResourceLocationToChatComponent(currentValue.location(), "#");
        }
        return Component.translatable( RegistryHelpers.getBlockFromLocation(currentValue.location()).getDescriptionId() );
    }

    @Override
    public @Nullable Component getHoverTooltipText() {
        BlockOrTagLocation value = this.getController().option().pendingValue();
        return TextUtil.formatResourceLocationToChatComponent(value.location(), value.isTag() ? "#" : "");
    }

    @Override
    protected void renderItemIcon(GuiGraphics graphics, Item item, int x, int y) {
        if(this.getController().option().pendingValue().isTag()) {
            // render tag icon
            renderTagIcon( graphics, x, y);
            return;
        }
        super.renderItemIcon(graphics, item, x, y);
    }

    protected void renderTagIcon(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(RenderType::guiTextured, BLOCK_TAG_ICON, x, y, 16, 16);
    }
}
