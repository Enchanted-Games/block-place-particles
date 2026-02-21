package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BasicTintedDust extends BasicDust {
    protected BasicTintedDust(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravityMultiplier, boolean spawnSpecks, Supplier<PIParticleOptions> speckGetter) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, gravityMultiplier, spawnSpecks, speckGetter);
    }

    public static class BrushProvider implements PIParticleProvider<PIParticleType.Simple> {
        public BrushProvider() {
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
            return new BasicTintedDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.7f, true, () -> ParticleTypesRegistry.BRUSH_DUST_SPECK);
        }
    }

    public static class BrushSpeckProvider implements PIParticleProvider<PIParticleType.Simple> {
        public BrushSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple optionstype,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new BasicTintedDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.35f, false, () -> null);
        }
    }

    public static class ItemFrameProvider implements PIParticleProvider<PIParticleType.Simple> {
        public ItemFrameProvider() {
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
            return new BasicTintedDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.7f, true, () -> ParticleTypesRegistry.ITEM_FRAME_DUST_SPECK);
        }
    }

    public static class ItemFrameSpeckProvider implements PIParticleProvider<PIParticleType.Simple> {
        public ItemFrameSpeckProvider() {
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
            return new BasicTintedDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.35f, false, () -> null);
        }
    }

    public static class GlowItemFrameProvider implements PIParticleProvider<PIParticleType.Simple> {
        public GlowItemFrameProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple optionse,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            BasicTintedDust particle = new BasicTintedDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.7f, true, () -> ParticleTypesRegistry.GLOW_ITEM_FRAME_DUST_SPECK);
            particle.emissive = true;
            return particle;
        }
    }

    public static class GlowItemFrameSpeckProvider implements PIParticleProvider<PIParticleType.Simple> {
        public GlowItemFrameSpeckProvider() {
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
            BasicTintedDust particle = new BasicTintedDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.35f, false, () -> null);
            particle.emissive = true;
            return particle;
        }
    }
}
