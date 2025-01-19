package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.registry.RegistryHelpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

public class FluidLocationControllerElement extends GenericListControllerElement<ResourceLocation, FluidLocationController> {
    public FluidLocationControllerElement(FluidLocationController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    public List<ResourceLocation> computeMatchingValues() {
        List<ResourceLocation> resourceLocations = RegistryHelpers.getMatchingLocations(inputField, BuiltInRegistries.FLUID).toList();
        ArrayList<ResourceLocation> fluidLocations = new ArrayList<>();
        currentItem = RegistryHelpers.validateFluidLocationWithFallback(inputField, null);
        for (ResourceLocation resourceLocation : resourceLocations) {
            Fluid blockFromLocation = RegistryHelpers.getFluidFromLocation(resourceLocation);
            if (blockFromLocation.defaultFluidState().createLegacyBlock().isAir()) continue;
            matchingItems.put(resourceLocation, RegistryHelpers.getLocationFromFluid(blockFromLocation));
            fluidLocations.add(resourceLocation);
        }
        return fluidLocations;
    }

    @Override
    public Item getItemToRender(ResourceLocation value) {
        return RegistryHelpers.getFluidFromLocation(value).getBucket();
    }

    @Override
    public Component getRenderedValueText() {
        return Component.translatable( RegistryHelpers.getFluidFromLocation(getController().option().pendingValue()).defaultFluidState().createLegacyBlock().getBlock().getDescriptionId() );
    }
}
