package games.enchanted.eg_particle_interactions.common.particle.types.splash;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class ColouredBucketSplash extends BucketSplash {
    protected ColouredBucketSplash(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
        int[] tintColour = appearance.colourSource().getARGB(context);
        this.setRGBA(
            this.getRed() * tintColour[1] / 255.0F,
            this.getGreen() * tintColour[2] / 255.0F,
            this.getBlue() * tintColour[3] / 255.0F,
            this.getAlpha() * tintColour[0] / 255.0F
        );
    }

    public static class Provider implements PIParticleProvider<PIParticleType.Simple> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple type,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new ColouredBucketSplash(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
