package games.enchanted.eg_particle_interactions.common.resource.texture.palette.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ColourCodecs;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.Palette;

import java.util.List;

public class StaticPaletteType extends PaletteType {
    public static final MapCodec<StaticPaletteType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            ColourCodecs.HEX_OR_ARGB_LIST_CODEC.listOf().fieldOf("colours").forGetter(StaticPaletteType::colours)
        ).apply(
            i,
            StaticPaletteType::new
        )
    );

    final List<Integer> argbValues;
    final Palette palette;

    StaticPaletteType(List<Integer> argbValues) {
        this.argbValues = argbValues;
        this.palette = new Palette(Palette.Entry.argbIntsToEntries(this.argbValues), true);
    }

    @Override
    public Palette getOrCreatePalette() {
        return this.palette;
    }

    @Override
    public MapCodec<? extends PaletteType> codec() {
        return CODEC;
    }

    protected List<Integer> colours() {
        return this.argbValues;
    }
}
