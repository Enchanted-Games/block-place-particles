package games.enchanted.eg_particle_interactions.common.particle.types.falling_spin;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class FallingSpinningColouredParticle extends FallingSpinningParticle {
    protected FallingSpinningColouredParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravityMultiplier) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, gravityMultiplier);

        int[] colour = appearance.colourSource().getARGB(context);
        this.setRGBA(
            (float) colour[1] / 255f,
            (float) colour[2] / 255f,
            (float) colour[3] / 255f,
            (float) colour[0] / 255f
        );
    }

    public static class TintedLeafProvider implements PIParticleProvider<PIParticleType.Simple> {
        public TintedLeafProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 1f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.1f : 0.15f;
            particle.setScale(particleSize);
            particle.maxSpinSpeed = 0.5f;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class FlowerPetalProvider implements PIParticleProvider<PIParticleType.Simple> {
        public FlowerPetalProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 1f);
            particle.maxSpinSpeed = 0.5f;
            return particle;
        }
    }

    public static class GrassBladeProvider implements PIParticleProvider<PIParticleType.Simple> {
        public GrassBladeProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 1f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.10F : 0.12F;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class HeavyGrassBladeProvider implements PIParticleProvider<PIParticleType.Simple> {
        public HeavyGrassBladeProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 2f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.10F : 0.12F;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class ChainSnapProvider implements PIParticleProvider<PIParticleType.Simple> {
        public ChainSnapProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 3f);
            ClientLevel level = context.level();
            float particleSize = level.getRandom().nextBoolean() ? 0.14F : 0.15F;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            particle.maxSpinSpeed = 0.2f;
            particle.spinAcceleration = (float) Math.toRadians(level.getRandom().nextBoolean() ? -1.0 : 1.0);
            return particle;
        }
    }

    public static class SugarCaneProvider implements PIParticleProvider<PIParticleType.Simple> {
        public SugarCaneProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 2.5f);
            float particleSize = context.level().getRandom().nextBoolean() ? 0.11F : 0.13F;
            particle.setScale(particleSize);
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }
}