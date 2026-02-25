package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ColourCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;

public class StaticColourSource implements ColourSource {
    public static final MapCodec<StaticColourSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ColourCodecs.HEX_OR_ARGB_LIST_CODEC.fieldOf("value").forGetter(StaticColourSource::getArgb)
        ).apply(
            instance,
            StaticColourSource::new
        )
    );

    final int argb;

    public StaticColourSource(int argb) {
        this.argb = argb;
    }

    protected int getArgb() {
        return this.argb;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        return ColourUtil.ARGBint_to_ARGB(this.argb);
    }

    @Override
    public MapCodec<? extends ColourSource> codec() {
        return CODEC;
    }
}
