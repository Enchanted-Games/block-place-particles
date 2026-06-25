package games.enchanted.eg_particle_interactions.common.resource.texture.palette.types;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.Palette;

public abstract class PaletteType {
    public abstract Palette getOrCreatePalette();

    public abstract MapCodec<? extends PaletteType> codec();
}
