package games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.generic.AbstractFixedDropdownController;
import games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.generic.GenericListControllerElement;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class FluidLocationController extends AbstractFixedDropdownController<Identifier> {
    public FluidLocationController(Option<Identifier> option) {
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
        Identifier validatedValue = RegistryHelpers.validateFluidLocationWithFallback(
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
        Identifier blockLocFromValue = RegistryHelpers.validateFluidLocationWithFallback(value, null);
        return blockLocFromValue != null;
    }

    @Override
    protected String getValidValue(String value, int offset) {
        return RegistryHelpers.getMatchingLocations(value, BuiltInRegistries.FLUID)
            .filter((Identifier location) -> !RegistryHelpers.getFluidFromLocation(location).defaultFluidState().createLegacyBlock().isAir())
            .skip(offset)
            .findFirst()
            .map(Identifier::toString)
            .orElseGet(this::getString);
    }

    @Override
    public GenericListControllerElement<Identifier, ?> createWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new FluidLocationControllerElement(this, screen, widgetDimension);
    }
}