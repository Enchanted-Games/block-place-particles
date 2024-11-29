package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.controller.generic.AbstractFixedDropdownController;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.util.RegistryHelpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class FluidLocationController extends AbstractFixedDropdownController<ResourceLocation> {
    public FluidLocationController(Option<ResourceLocation> option) {
        super(option);
    }

    @Override
    public String getString() {
        return option().pendingValue().toString();
    }

    @Override
    public void setFromStringIndex(String value, int index) {
        String valueFromDropdown = getValueFromDropdown(index);
        if(valueFromDropdown == null) {
            valueFromDropdown = value;
        }
        ResourceLocation validatedValue = RegistryHelpers.validateFluidLocationWithFallback(
            valueFromDropdown,
            null
        );
        if(isValueValid(valueFromDropdown) && validatedValue != null) {
            option.requestSet(
                validatedValue
            );
        }
    };

    @Override
    public boolean isValueValid(String value) {
        ResourceLocation blockLocFromValue = RegistryHelpers.validateFluidLocationWithFallback(value, null);
        return blockLocFromValue != null;
    }

    @Override
    protected String getValidValue(String value, int offset) {
        return RegistryHelpers.getMatchingLocations(value, BuiltInRegistries.FLUID)
            .filter((ResourceLocation location) -> !RegistryHelpers.getFluidFromLocation(location).defaultFluidState().createLegacyBlock().isAir())
            .skip(offset)
            .findFirst()
            .map(ResourceLocation::toString)
            .orElseGet(this::getString);
    }

    @Override
    public GenericListControllerElement<ResourceLocation, ?> createWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new FluidLocationControllerElement(this, screen, widgetDimension);
    }
}