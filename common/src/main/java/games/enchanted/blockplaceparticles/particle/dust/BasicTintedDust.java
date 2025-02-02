package games.enchanted.blockplaceparticles.particle.dust;

import games.enchanted.blockplaceparticles.particle.option.TintedParticleOption;
import games.enchanted.blockplaceparticles.util.ColourUtil;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BasicTintedDust extends AbstractDust {
    private final Supplier<ParticleOptions> speckGetter;

    protected BasicTintedDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier, TintedParticleOption tintedParticleOption, boolean spawnSpecks, boolean spriteFromAge, Supplier<ParticleOptions> speckGetter) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier, spawnSpecks);
        this.spriteFromAge = spriteFromAge;

        int[] rgb = tintedParticleOption.getRandomisedColour();
        this.rCol = rgb[0] / 255f;
        this.gCol = rgb[1] / 255f;
        this.bCol = rgb[2] / 255f;

        this.speckGetter = speckGetter;
    }

    @Override
    protected @NotNull ParticleOptions getSpeckParticle() {
        return speckGetter.get();
    }

    public static class RedstoneProvider implements ParticleProvider<TintedParticleOption> {
        private final SpriteSet spriteSet;

        public RedstoneProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull TintedParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BasicTintedDust particle = new BasicTintedDust(level, x, y, z, xSpeed, MathHelpers.randomBetween(0.06f, 0.13f), zSpeed, this.spriteSet, -0.0f, type, false, true, () -> null);
            particle.roll = 0;
            particle.oRoll = 0;
            particle.lifetime = (int)(particle.lifetime * 0.6f);
            return particle;
        }
    }

    public static class BrushProvider implements ParticleProvider<TintedParticleOption> {
        private final SpriteSet spriteSet;

        public BrushProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull TintedParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.7f, type, true, true, () -> TintedParticleOption.BRUSH_SPECK_OPTION);
        }
    }
    public static class BrushSpeckProvider implements ParticleProvider<TintedParticleOption>  {
        private final SpriteSet spriteSet;

        public BrushSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull TintedParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.35f, type, false, false, () -> null);
        }
    }

    public static class ItemFrameProvider implements ParticleProvider<TintedParticleOption> {
        private final SpriteSet spriteSet;

        public ItemFrameProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull TintedParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.7f, type, true, true, () -> TintedParticleOption.ITEM_FRAME_DUST_SPECK_OPTION);
        }
    }
    public static class ItemFrameSpeckProvider implements ParticleProvider<TintedParticleOption>  {
        private final SpriteSet spriteSet;

        public ItemFrameSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull TintedParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.35f, type, false, false, () -> null);
        }
    }

    public static class GlowItemFrameProvider implements ParticleProvider<TintedParticleOption> {
        private final SpriteSet spriteSet;

        public GlowItemFrameProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull TintedParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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

        @Nullable
        @Override
        public Particle createParticle(@NotNull TintedParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BasicTintedDust particle = new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.35f, type, false, false, () -> null);
            particle.emissive = true;
            return particle;
        }
    }
}
