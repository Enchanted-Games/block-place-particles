package games.enchanted.eg_particle_interactions.common.particle.coloursource;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public class StaticColourSource extends ParticleColourSource {
    final int[] argb;

    public StaticColourSource(int[] argb) {
        this.argb = argb;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        return this.argb;
    }
}
