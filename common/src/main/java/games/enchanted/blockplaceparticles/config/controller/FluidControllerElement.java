package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.config.util.RegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public class FluidControllerElement extends GenericListControllerElement<Fluid, FluidController> {
    public FluidControllerElement(FluidController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    public List<ResourceLocation> computeMatchingValues() {
        List<ResourceLocation> resourceLocations = RegistryHelper.getMatchingIdentifiers(inputField, BuiltInRegistries.FLUID).toList();
        ArrayList<ResourceLocation> fluidLocations = new ArrayList<>();
        currentItem = getDefaultedFluid(inputField, null);
        for (ResourceLocation resourceLocation : resourceLocations) {
            Fluid fluidFromLocation = BuiltInRegistries.FLUID.get(resourceLocation);
            if(fluidFromLocation == Fluids.EMPTY) continue;
            matchingItems.put(resourceLocation, fluidFromLocation);
            fluidLocations.add(resourceLocation);
        }
        return fluidLocations;
    }

    Fluid getDefaultedFluid(String location, Fluid defaultFluid) {
        return RegistryHelper.getDefaultedFluid(location, defaultFluid);
    }

    @Override
    public Item getItemToRender(Fluid value) {
        return value.getBucket();
    }

    @Override
    public Component getRenderedValueText() {
        return Component.translatable(getController().option().pendingValue().defaultFluidState().createLegacyBlock().getBlock().getDescriptionId());
    }
}
