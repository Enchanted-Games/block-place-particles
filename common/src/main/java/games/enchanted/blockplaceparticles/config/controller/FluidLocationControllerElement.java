package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public class FluidLocationControllerElement extends GenericListControllerElement<ResourceLocation, FluidLocationController> {
    public FluidLocationControllerElement(FluidLocationController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    public List<ResourceLocation> computeMatchingValues() {
        List<ResourceLocation> resourceLocations = RegistryHelper.getMatchingIdentifiers(inputField, BuiltInRegistries.FLUID).toList();
        ArrayList<ResourceLocation> fluidLocations = new ArrayList<>();
        currentItem = RegistryHelper.validateFluidLocationWithFallback(inputField, null);
        for (ResourceLocation resourceLocation : resourceLocations) {
            Fluid blockFromLocation = RegistryHelper.getFluidFromLocation(resourceLocation);
            if (blockFromLocation.defaultFluidState().createLegacyBlock().isAir()) continue;
            matchingItems.put(resourceLocation, RegistryHelper.getLocationFromFluid(blockFromLocation));
            fluidLocations.add(resourceLocation);
        }
        return fluidLocations;
    }

    @Override
    public Item getItemToRender(ResourceLocation value) {
        return RegistryHelper.getFluidFromLocation(value).getBucket();
    }

    @Override
    public Component getRenderedValueText() {
        return Component.translatable( RegistryHelper.getFluidFromLocation(getController().option().pendingValue()).defaultFluidState().createLegacyBlock().getBlock().getDescriptionId() );
    }
}