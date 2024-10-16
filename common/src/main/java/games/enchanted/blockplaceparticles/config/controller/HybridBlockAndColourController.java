package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.controller.generic.HybridOptionController;
import games.enchanted.blockplaceparticles.config.controller.generic.HybridOptionWidget;
import games.enchanted.blockplaceparticles.config.type.ResourceLocationAndColour;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class HybridBlockAndColourController extends HybridOptionController<ResourceLocation, Color, ResourceLocationAndColour> implements Controller<ResourceLocationAndColour> {
    public HybridBlockAndColourController(Option<ResourceLocationAndColour> option) {
        super(option);
    }

    @Override
    protected Option<ResourceLocation> createOptionForTypeA() {
        return Option.<ResourceLocation>createBuilder()
            .name(Component.literal("test"))
            .customController(BlockLocationController::new)
            .binding(this.getTypeABinding(ResourceLocation.withDefaultNamespace("stone")))
            .instant(true)
        .build();
    }

    @Override
    protected Option<Color> createOptionForTypeB() {
        return Option.<Color>createBuilder()
            .name(Component.literal("test"))
            .controller(ColorControllerBuilder::create)
            .binding(this.getTypeBBinding(Color.white))
            .instant(true)
        .build();
    }

    @Override
    protected ResourceLocationAndColour getValueToSetAsPending(ResourceLocation typeA, Color typeB) {
        return new ResourceLocationAndColour(typeA, typeB);
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new HybridOptionWidget(this, screen, widgetDimension, this.getTypeAOption(), this.getTypeBOption(), 0.58f);
    }
}
