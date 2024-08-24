package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;
import dev.isxander.yacl3.gui.utils.ItemRegistryHelper;
import games.enchanted.blockplaceparticles.config.util.RegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;

public class FluidController extends AbstractDropdownController<Fluid> {
    public FluidController(Option<Fluid> option) {
        super(option);
    }

    @Override
    public String getString() {
        return BuiltInRegistries.FLUID.getKey(option().pendingValue()).toString();
    }

    @Override
    public void setFromString(String value) {
        if(isValueValid(value)) {
            option.requestSet(RegistryHelper.getDefaultedFluid(value, null));
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
            .skip(offset)
            .findFirst()
            .map(ResourceLocation::toString)
            .orElseGet(this::getString);
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new FluidControllerElement(this, screen, widgetDimension);
    }
}