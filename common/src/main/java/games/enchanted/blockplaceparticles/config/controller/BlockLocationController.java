package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BlockLocationController extends AbstractDropdownController<ResourceLocation> {
    public BlockLocationController(Option<ResourceLocation> option) {
        super(option);
    }

    @Override
    public String getString() {
        return option().pendingValue().toString();
    }

    @Override
    public void setFromString(String value) {
        if(isValueValid(value)) {
            option.requestSet(RegistryHelper.validateBlockLocationWithFallback(value, null));
        }
    }

    @Override
    public boolean isValueValid(String value) {
        ResourceLocation blockLocFromValue = RegistryHelper.validateBlockLocationWithFallback(value, null);
        return blockLocFromValue != null;
    }

    @Override
    protected String getValidValue(String value, int offset) {
        return RegistryHelper.getMatchingIdentifiers(value, BuiltInRegistries.BLOCK)
            .skip(offset)
            .findFirst()
            .map(ResourceLocation::toString)
            .orElseGet(this::getString);
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new BlockLocationControllerElement(this, screen, widgetDimension);
    }
}