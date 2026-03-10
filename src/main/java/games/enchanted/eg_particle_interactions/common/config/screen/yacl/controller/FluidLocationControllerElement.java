//package games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller;
//
//import dev.isxander.yacl3.api.utils.Dimension;
//import dev.isxander.yacl3.gui.YACLScreen;
//import games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.generic.GenericListControllerElement;
//import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
//import games.enchanted.eg_particle_interactions.common.util.TextUtil;
//import net.minecraft.client.gui.GuiGraphicsExtractor;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.material.Fluid;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FluidLocationControllerElement extends GenericListControllerElement<Identifier, FluidLocationController> {
//    public FluidLocationControllerElement(FluidLocationController control, YACLScreen screen, Dimension<Integer> dim) {
//        super(control, screen, dim);
//    }
//
//    @Override
//    public List<Identifier> computeMatchingValues() {
//        List<Identifier> resourceLocations = RegistryHelpers.getMatchingLocations(inputField, BuiltInRegistries.FLUID).toList();
//        ArrayList<Identifier> fluidLocations = new ArrayList<>();
//        currentItem = RegistryHelpers.validateFluidLocationWithFallback(inputField, null);
//        for (Identifier resourceLocation : resourceLocations) {
//            Fluid blockFromLocation = RegistryHelpers.getFluidFromLocation(resourceLocation);
//            if (blockFromLocation.defaultFluidState().createLegacyBlock().isAir()) continue;
//            matchingItems.put(resourceLocation, RegistryHelpers.getLocationFromFluid(blockFromLocation));
//            fluidLocations.add(resourceLocation);
//        }
//        return fluidLocations;
//    }
//
//    @Override
//    protected void renderDropdownEntry(GuiGraphicsExtractor graphics, Dimension<Integer> entryDimension, Identifier identifier) {
//        Identifier item = matchingItems.get(identifier);
//        if(item == null) return;
//        super.renderDropdownEntry(graphics, entryDimension, identifier);
//        this.renderItemIcon(graphics, getItemToRender(item), entryDimension.xLimit() - 2, entryDimension.y() + 1);
//    }
//
//    @Override
//    public @Nullable Component getHoverTooltipText() {
//        return TextUtil.formatIdentifierToChatComponent(this.getController().option().pendingValue());
//    }
//
//    @Override
//    public Item getItemToRender(Identifier value) {
//        return RegistryHelpers.getFluidFromLocation(value).getBucket();
//    }
//
//    @Override
//    public Component getRenderedValueText() {
//        return Component.translatable( RegistryHelpers.getFluidFromLocation(getController().option().pendingValue()).defaultFluidState().createLegacyBlock().getBlock().getDescriptionId() );
//    }
//}
