package games.enchanted.blockplaceparticles.particle.dust;

import games.enchanted.blockplaceparticles.particle.option.TintedParticleOption;
import games.enchanted.blockplaceparticles.util.ColourUtil;
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

        int[] rgb = ColourUtil.RGBint_to_RGB(tintedParticleOption.getColor());
        float colourVariation = tintedParticleOption.getVariationAmount();

        int[] randomRgb;
        if(tintedParticleOption.getUniformVariation()) {
            randomRgb = ColourUtil.randomiseNegativeUniform(rgb, colourVariation);
        } else {
            randomRgb = ColourUtil.randomiseNegative(rgb, colourVariation);
        }
        this.rCol = randomRgb[0] / 255f;
        this.gCol = randomRgb[1] / 255f;
        this.bCol = randomRgb[2] / 255f;

        this.speckGetter = speckGetter;
    }

    @Override
    protected @NotNull ParticleOptions getSpeckParticle() {
        return speckGetter.get();
    }

    public static class Provider implements ParticleProvider<TintedParticleOption> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull TintedParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BasicTintedDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 1f, type, false, true, () -> null);
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
}
