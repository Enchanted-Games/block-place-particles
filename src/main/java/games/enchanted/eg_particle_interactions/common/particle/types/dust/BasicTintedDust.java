package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.particle.options.TintedParticleOption;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BasicTintedDust extends BasicDust {
    protected BasicTintedDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier, TintedParticleOption tintedParticleOption, boolean spawnSpecks, boolean spriteFromAge, Supplier<ParticleOptions> speckGetter) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier, spawnSpecks, spriteFromAge, speckGetter);

        int[] rgb = tintedParticleOption.getRandomisedColour();
        this.setRGB(rgb[0] / 255f, rgb[1] / 255f, rgb[2] / 255f);
    }

    public static class BrushProvider implements ParticleProvider<TintedParticleOption> {
        private final SpriteSet spriteSet;

        public BrushProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            TintedParticleOption type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.7f, type, true, true, () -> TintedParticleOption.BRUSH_SPECK_OPTION);
        }
    }
    public static class BrushSpeckProvider implements ParticleProvider<TintedParticleOption>  {
        private final SpriteSet spriteSet;

        public BrushSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            TintedParticleOption type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.35f, type, false, false, () -> null);
        }
    }

    public static class ItemFrameProvider implements ParticleProvider<TintedParticleOption> {
        private final SpriteSet spriteSet;

        public ItemFrameProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            TintedParticleOption type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.7f, type, true, true, () -> TintedParticleOption.ITEM_FRAME_DUST_SPECK_OPTION);
        }
    }
    public static class ItemFrameSpeckProvider implements ParticleProvider<TintedParticleOption>  {
        private final SpriteSet spriteSet;

        public ItemFrameSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            TintedParticleOption type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.35f, type, false, false, () -> null);
        }
    }

    public static class GlowItemFrameProvider implements ParticleProvider<TintedParticleOption> {
        private final SpriteSet spriteSet;

        public GlowItemFrameProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            TintedParticleOption type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            BasicTintedDust particle = new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.7f, type, true, true, () -> TintedParticleOption.GLOW_ITEM_FRAME_DUST_SPECK_OPTION);
            particle.emissive = true;
            return particle;
        }
    }
    public static class GlowItemFrameSpeckProvider implements ParticleProvider<TintedParticleOption>  {
        private final SpriteSet spriteSet;

        public GlowItemFrameSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            TintedParticleOption type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            BasicTintedDust particle = new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.35f, type, false, false, () -> null);
            particle.emissive = true;
            return particle;
        }
    }
}
