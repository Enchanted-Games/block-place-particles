package games.enchanted.blockplaceparticles.particle.dust;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.util.ColourUtil;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FloatingBrushDust extends FloatingDust {
    private static final int COLOR_RGB = 0xcec5d6;

    protected FloatingBrushDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier, boolean spawnSpecks) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier, spawnSpecks);
        float brightness = (float)Math.random() * 0.15F;
        int[] rgb = ColourUtil.RGBint_to_RGB(COLOR_RGB);
        this.rCol = (float) rgb[0] / 255.0F - brightness;
        this.gCol = (float) rgb[1] / 255.0F - brightness;
        this.bCol = (float) rgb[2] / 255.0F - brightness;
    }

    @Override
    protected @NotNull ParticleOptions getSpeckParticle() {
        return ModParticleTypes.BRUSH_DUST_SPECK;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FloatingBrushDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.7f, true);
        }
    }

    public static class SpeckProvider implements ParticleProvider<SimpleParticleType>  {
        private final SpriteSet spriteSet;

        public SpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FloatingBrushDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.35f, false);
        }
    }
}
