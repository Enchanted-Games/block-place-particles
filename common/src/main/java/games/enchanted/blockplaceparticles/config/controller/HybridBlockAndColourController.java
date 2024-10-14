package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.type.BlockLocationAndColour;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HybridBlockAndColourController implements Controller<BlockLocationAndColour> {
    private final Option<BlockLocationAndColour> option;
    @NotNull Color currentColour;
    @NotNull Option<Color> colourOption;
    @NotNull ResourceLocation currentLocation;
    @NotNull Option<ResourceLocation> locationOption;

    public HybridBlockAndColourController(Option<BlockLocationAndColour> option) {
        this.option = option;
        this.currentColour = Color.WHITE;
        this.colourOption = Option.<Color>createBuilder()
            .name(Component.literal("test"))
            .controller(ColorControllerBuilder::create)
            .binding(Color.WHITE, () -> this.currentColour, newVal -> this.currentColour = newVal)
        .build();
        this.currentLocation = ResourceLocation.withDefaultNamespace("stone");
        this.locationOption = Option.<ResourceLocation>createBuilder()
            .name(Component.literal("test"))
            .customController(BlockLocationController::new)
            .binding(ResourceLocation.withDefaultNamespace("stone"), () -> this.currentLocation, newVal -> this.currentLocation = newVal)
        .build();
    }

    @Override
    public Option<BlockLocationAndColour> option() {
        return this.option;
    }

    @Override
    public Component formatValue() {
        return Component.literal("option.toString()");
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new HybridOptionWidget(this, screen, widgetDimension, this.locationOption, this.colourOption, 0.5f);
    }
}
