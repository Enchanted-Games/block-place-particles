package games.enchanted.eg_particle_interactions.common.config.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.eg_particle_interactions.common.config.controller.generic.AbstractFixedDropdownController;
import games.enchanted.eg_particle_interactions.common.config.controller.generic.GenericListControllerElement;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BlockLocationController extends AbstractFixedDropdownController<BlockOrTagLocation> {
    public BlockLocationController(Option<BlockOrTagLocation> option) {
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
        BlockOrTagLocation validatedValue = RegistryHelpers.validateBlockOrTagLocationWithFallback(
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
        BlockOrTagLocation blockLocFromValue = RegistryHelpers.validateBlockOrTagLocationWithFallback(value, null);
        return blockLocFromValue != null;
    }

    @Override
    protected String getValidValue(String value, int offset) {
        if(value.startsWith("#")) {
            value = value.replace("#", "");
            return RegistryHelpers.getMatchingTagLocations(value, BuiltInRegistries.BLOCK)
                .skip(offset)
                .findFirst()
                .map(ResourceLocation::toString)
                .orElseGet(this::getString);
        }

        return RegistryHelpers.getMatchingLocations(value, BuiltInRegistries.BLOCK)
            .filter((ResourceLocation location) -> !RegistryHelpers.getBlockFromLocation(location).defaultBlockState().isAir())
            .skip(offset)
            .findFirst()
            .map(ResourceLocation::toString)
            .orElseGet(this::getString);
    }

    @Override
    public GenericListControllerElement<BlockOrTagLocation, ?> createWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new BlockLocationControllerElement(this, screen, widgetDimension);
    }
}