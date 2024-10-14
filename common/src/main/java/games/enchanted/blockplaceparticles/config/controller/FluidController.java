package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.controller.generic.AbstractFixedDropdownController;
import games.enchanted.blockplaceparticles.config.controller.generic.GenericListControllerElement;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidController extends AbstractFixedDropdownController<Fluid> {
    public FluidController(Option<Fluid> option) {
        super(option);
    }

    @Override
    public String getString() {
        return BuiltInRegistries.FLUID.getKey(option().pendingValue()).toString();
    }

    @Override
    public void setFromStringIndex(String value, int index) {
        String valueFromDropdown = getValueFromDropdown(index);
        if(valueFromDropdown == null) {
            valueFromDropdown = value;
        }
        Fluid validatedValue = RegistryHelper.getDefaultedFluid(
            valueFromDropdown,
            null
        );
        if(isValueValid(valueFromDropdown) && validatedValue != null) {
            option.requestSet(
                validatedValue
            );
        }
    }

    @Override
    public boolean isValueValid(String value) {
        Fluid fluidFromValue = RegistryHelper.getDefaultedFluid(value, null);
        return fluidFromValue != null;
    }

    @Override
    protected String getValidValue(String value, int offset) {
        return RegistryHelper.getMatchingIdentifiers(value, BuiltInRegistries.FLUID)
            .filter((ResourceLocation location) -> location != BuiltInRegistries.FLUID.getKey(Fluids.EMPTY))
            .skip(offset)
            .findFirst()
            .map(ResourceLocation::toString)
            .orElseGet(this::getString);
    }

    @Override
    public GenericListControllerElement<Fluid, ?> createWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new FluidControllerElement(this, screen, widgetDimension);
    }
}