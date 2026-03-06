package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;

public interface ColourSource {
    int[] getARGB(ParticleContext context);

    MapCodec<? extends ColourSource> codec();

    default int[] multiply(ParticleContext context, int[] argb) {
        return ColourUtil.multiplyColours(this.getARGB(context), argb);
    }
}
