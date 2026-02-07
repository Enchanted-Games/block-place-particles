package games.enchanted.eg_particle_interactions.common.particle.colour;

public class StaticColourSource extends ParticleColourSource {
    final int[] argb;

    public StaticColourSource(int[] argb) {
        this.argb = argb;
    }

    @Override
    public int[] getARGB(ParticleColourContext context) {
        return this.argb;
    }
}
