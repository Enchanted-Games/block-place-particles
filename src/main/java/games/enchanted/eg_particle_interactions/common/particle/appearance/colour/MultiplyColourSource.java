package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;

import java.util.List;

public class MultiplyColourSource implements ColourSource {
    public static final MapCodec<MultiplyColourSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.list(ColourSources.CODEC).fieldOf("colours").forGetter(MultiplyColourSource::getSources)
        ).apply(
            instance,
            MultiplyColourSource::new
        )
    );

    final List<ColourSource> sources;

    public MultiplyColourSource(List<ColourSource> sources) {
        this.sources = sources;
    }

    protected List<ColourSource> getSources() {
        return this.sources;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        if(this.sources.isEmpty()) return ColourSources.WHITE.getARGB(context);
        int[] colour = this.sources.getFirst().getARGB(context);
        for (int i = 1; i < this.sources.size(); i++) {
            colour = this.sources.get(i).multiply(context, colour);
        }
        return colour;
    }

    @Override
    public MapCodec<? extends ColourSource> codec() {
        return CODEC;
    }
}
