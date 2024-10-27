package games.enchanted.blockplaceparticles.config.type;

import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public record ResourceLocationAndColour(ResourceLocation location, Color colour) implements TwoTypesInterface<ResourceLocation, Color> {
    @Override
    public ResourceLocation getTypeA() {
        return this.location();
    }

    @Override
    public Color getTypeB() {
        return this.colour();
    }
}
