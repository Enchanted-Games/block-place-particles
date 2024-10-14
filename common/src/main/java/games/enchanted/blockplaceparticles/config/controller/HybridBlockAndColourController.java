package games.enchanted.blockplaceparticles.config.controller;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ColorController;
import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.config.type.ResourceLocationAndColour;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HybridBlockAndColourController implements Controller<ResourceLocationAndColour> {
    private final Option<ResourceLocationAndColour> option;
    @NotNull Color currentColour;
    @NotNull Option<Color> colourOption;
    @NotNull ResourceLocation currentLocation;
    @NotNull Option<ResourceLocation> locationOption;

    public HybridBlockAndColourController(Option<ResourceLocationAndColour> option) {
        this.option = option;
        this.currentColour = option.pendingValue().colour();
        this.colourOption = Option.<Color>createBuilder()
            .name(Component.literal("test"))
            .controller(ColorControllerBuilder::create)
            .binding(Color.WHITE, () -> this.currentColour, this::setColour)
            .instant(true)
        .build();
        this.currentLocation = option.pendingValue().location();
        this.locationOption = Option.<ResourceLocation>createBuilder()
            .name(Component.literal("test"))
            .customController(BlockLocationController::new)
            .binding(ResourceLocation.withDefaultNamespace("stone"), () -> this.currentLocation, this::setLocation)
            .instant(true)
        .build();
        this.option.addListener(this::updateCurrentValues);
    }

    protected void setColour(Color colour) {
        this.currentColour = colour;
        this.setPendingValue();
    }
    protected void setLocation(ResourceLocation location) {
        this.currentLocation = location;
        this.setPendingValue();
    }
    protected void setPendingValue() {
        this.option.requestSet(new ResourceLocationAndColour(this.currentLocation, this.currentColour));
    }

    // updated the values of currentLocation and currentColour when the option is changed
    protected void updateCurrentValues(Option<ResourceLocationAndColour> changedOption, ResourceLocationAndColour pending) {
        this.locationOption.requestSet(pending.location());
        this.locationOption.applyValue();
        this.colourOption.requestSet(pending.colour());
        this.colourOption.applyValue();
    }

    @Override
    public Option<ResourceLocationAndColour> option() {
        return this.option;
    }

    @Override
    public Component formatValue() {
        return Component.literal("option.toString()");
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new HybridOptionWidget(this, screen, widgetDimension, this.locationOption, this.colourOption, 0.58f);
    }
}
