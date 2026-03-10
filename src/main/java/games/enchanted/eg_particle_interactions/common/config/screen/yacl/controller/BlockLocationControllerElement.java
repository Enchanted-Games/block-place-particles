//package games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller;
//
//import dev.isxander.yacl3.api.utils.Dimension;
//import dev.isxander.yacl3.gui.YACLScreen;
//import games.enchanted.eg_particle_interactions.common.Constants;
//import games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.generic.GenericListControllerElement;
//import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
//import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
//import games.enchanted.eg_particle_interactions.common.util.TextUtil;
//import net.minecraft.client.gui.GuiGraphicsExtractor;
//import net.minecraft.client.renderer.RenderPipelines;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlockLocationControllerElement extends GenericListControllerElement<BlockOrTagLocation, BlockLocationController> {
//    private static final Identifier BLOCK_TAG_ICON = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block_tag_icon");
//
//    public BlockLocationControllerElement(BlockLocationController control, YACLScreen screen, Dimension<Integer> dim) {
//        super(control, screen, dim);
//    }
//
//    @Override
//    public List<BlockOrTagLocation> computeMatchingValues() {
//        if(this.inputField.startsWith("#"))  {
//            // tag logic
//            String value = this.inputField.replace("#", "");
//            List<Identifier> tagIdentifiers = RegistryHelpers.getMatchingTagLocations(value, BuiltInRegistries.BLOCK).toList();
//            ArrayList<BlockOrTagLocation> tagLocations = new ArrayList<>();
//            BlockOrTagLocation validatedLoc = RegistryHelpers.validateBlockOrTagLocationWithFallback(this.inputField, null);
//            this.currentItem = validatedLoc;
//
//            if(!validatedLoc.location().getPath().isEmpty()) {
//                this.matchingItems.put(validatedLoc.location(), validatedLoc);
//                tagLocations.add(validatedLoc);
//            }
//
//            for (Identifier tagIdentifier : tagIdentifiers) {
//                this.matchingItems.put(tagIdentifier, validatedLoc);
//                tagLocations.add(new BlockOrTagLocation(tagIdentifier, true));
//            }
//            return tagLocations;
//        }
//        List<Identifier> blockIdentifiers = RegistryHelpers.getMatchingLocations(this.inputField, BuiltInRegistries.BLOCK).toList();
//        ArrayList<BlockOrTagLocation> blockOrTagLocations = new ArrayList<>();
//        Identifier validatedLoc = RegistryHelpers.validateBlockLocationWithFallback(this.inputField, null);
//        this.currentItem = validatedLoc == null ? null : new BlockOrTagLocation(validatedLoc);
//        for (Identifier blockLocation : blockIdentifiers) {
//            Block blockFromLocation = RegistryHelpers.getBlockFromLocation(blockLocation);
//            if (blockFromLocation.defaultBlockState().isAir()) continue;
//            this.matchingItems.put(blockLocation, new BlockOrTagLocation(RegistryHelpers.getLocationFromBlock(blockFromLocation)));
//            blockOrTagLocations.add(new BlockOrTagLocation(blockLocation));
//        }
//        return blockOrTagLocations;
//    }
//
//    @Override
//    public Item getItemToRender(BlockOrTagLocation value) {
//        if(value.isTag()) return null;
//        return RegistryHelpers.getBlockFromLocation(value.location()).asItem();
//    }
//
//    @Override
//    protected void renderDropdownEntry(GuiGraphicsExtractor graphics, Dimension<Integer> entryDimension, BlockOrTagLocation blockOrTagLocation) {
//        super.renderDropdownEntry(graphics, entryDimension, blockOrTagLocation);
//        if(blockOrTagLocation.isTag()) {
//            // render tag icon
//            renderTagIcon( graphics, entryDimension.xLimit() - 2, entryDimension.y() + 1);
//            return;
//        }
//        this.renderItemIcon(graphics, getItemToRender(blockOrTagLocation), entryDimension.xLimit() - 2, entryDimension.y() + 1);
//    }
//
//    @Override
//    public Component getRenderedValueText() {
//        BlockOrTagLocation currentValue = this.getController().option().pendingValue();
//        if(currentValue.isTag()) {
//            return TextUtil.formatIdentifierToChatComponent(currentValue.location(), "#");
//        }
//        return Component.translatable( RegistryHelpers.getBlockFromLocation(currentValue.location()).getDescriptionId() );
//    }
//
//    @Override
//    public @Nullable Component getHoverTooltipText() {
//        BlockOrTagLocation value = this.getController().option().pendingValue();
//        return TextUtil.formatIdentifierToChatComponent(value.location(), value.isTag() ? "#" : "");
//    }
//
//    @Override
//    protected void renderItemIcon(GuiGraphicsExtractor graphics, Item item, int x, int y) {
//        if(this.getController().option().pendingValue().isTag()) {
//            // render tag icon
//            renderTagIcon( graphics, x, y);
//            return;
//        }
//        super.renderItemIcon(graphics, item, x, y);
//    }
//
//    protected void renderTagIcon(GuiGraphicsExtractor graphics, int x, int y) {
//        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BLOCK_TAG_ICON, x, y, 16, 16);
//    }
//}
