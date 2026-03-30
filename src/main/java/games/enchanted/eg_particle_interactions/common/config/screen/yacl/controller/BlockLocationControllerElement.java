package games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.generic.GenericListControllerElement;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import games.enchanted.eg_particle_interactions.common.util.TextUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockLocationControllerElement extends GenericListControllerElement<ObjectOrTagLocation, BlockLocationController> {
    private static final Identifier BLOCK_TAG_ICON = ParticleInteractionsMod.id("block_tag_icon");

    public BlockLocationControllerElement(BlockLocationController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    public List<ObjectOrTagLocation> computeMatchingValues() {
        if(this.inputField.startsWith("#"))  {
            // tag logic
            String value = this.inputField.replace("#", "");
            List<Identifier> tagIdentifiers = RegistryHelpers.getMatchingTagLocations(value, BuiltInRegistries.BLOCK).toList();
            ArrayList<ObjectOrTagLocation> tagLocations = new ArrayList<>();
            ObjectOrTagLocation validatedLoc = RegistryHelpers.validateBlockOrTagLocationWithFallback(this.inputField, null);
            this.currentItem = validatedLoc;

            if(!validatedLoc.location().getPath().isEmpty()) {
                this.matchingItems.put(validatedLoc.location(), validatedLoc);
                tagLocations.add(validatedLoc);
            }

            for (Identifier tagIdentifier : tagIdentifiers) {
                this.matchingItems.put(tagIdentifier, validatedLoc);
                tagLocations.add(new ObjectOrTagLocation(tagIdentifier, true));
            }
            return tagLocations;
        }
        List<Identifier> blockIdentifiers = RegistryHelpers.getMatchingLocations(this.inputField, BuiltInRegistries.BLOCK).toList();
        ArrayList<ObjectOrTagLocation> objectOrTagLocations = new ArrayList<>();
        Identifier validatedLoc = RegistryHelpers.validateBlockLocationWithFallback(this.inputField, null);
        this.currentItem = validatedLoc == null ? null : new ObjectOrTagLocation(validatedLoc);
        for (Identifier blockLocation : blockIdentifiers) {
            Block blockFromLocation = RegistryHelpers.getBlockFromLocation(blockLocation);
            if (blockFromLocation.defaultBlockState().isAir()) continue;
            this.matchingItems.put(blockLocation, new ObjectOrTagLocation(RegistryHelpers.getLocationFromBlock(blockFromLocation)));
            objectOrTagLocations.add(new ObjectOrTagLocation(blockLocation));
        }
        return objectOrTagLocations;
    }

    @Override
    public Item getItemToRender(ObjectOrTagLocation value) {
        if(value.isTag()) return null;
        return RegistryHelpers.getBlockFromLocation(value.location()).asItem();
    }

    @Override
    protected void extractDropdownEntry(GuiGraphicsExtractor graphics, Dimension<Integer> entryDimension, ObjectOrTagLocation objectOrTagLocation) {
        super.extractDropdownEntry(graphics, entryDimension, objectOrTagLocation);
        if(objectOrTagLocation.isTag()) {
            // render tag icon
            renderTagIcon(graphics, entryDimension.xLimit() - 2, entryDimension.y() + 1);
            return;
        }
        this.extractItemIcon(graphics, getItemToRender(objectOrTagLocation), entryDimension.xLimit() - 2, entryDimension.y() + 1);
    }

    @Override
    public Component getRenderedValueText() {
        ObjectOrTagLocation currentValue = this.getController().option().pendingValue();
        if(currentValue.isTag()) {
            return TextUtil.formatIdentifierToChatComponent(currentValue.location(), "#");
        }
        return Component.translatable( RegistryHelpers.getBlockFromLocation(currentValue.location()).getDescriptionId() );
    }

    @Override
    public @Nullable Component getHoverTooltipText() {
        ObjectOrTagLocation value = this.getController().option().pendingValue();
        return TextUtil.formatIdentifierToChatComponent(value.location(), value.isTag() ? "#" : "");
    }

    @Override
    protected void extractItemIcon(GuiGraphicsExtractor graphics, Item item, int x, int y) {
        if(this.getController().option().pendingValue().isTag()) {
            // render tag icon
            renderTagIcon( graphics, x, y);
            return;
        }
        super.extractItemIcon(graphics, item, x, y);
    }

    protected void renderTagIcon(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BLOCK_TAG_ICON, x, y, 16, 16);
    }
}
