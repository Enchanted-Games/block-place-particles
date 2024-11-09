package games.enchanted.blockplaceparticles.particle.dust;

import games.enchanted.blockplaceparticles.util.ColourUtil;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FallingTintedOrAverageDust extends FallingDust {
    protected FallingTintedOrAverageDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState, SpriteSet spriteSet, float gravityMultiplier) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier);

        int tintColour = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
        if(tintColour == 0xffffff || tintColour == -1) {
            // use average texture colour
            int[] averageBlockColour = ColourUtil.getAverageBlockColour(blockState);
            this.rCol = (float)averageBlockColour[1] / 255f;
            this.gCol = (float)averageBlockColour[2] / 255f;
            this.bCol = (float)averageBlockColour[3] / 255f;
            this.alpha = (float)averageBlockColour[0] / 255f;
        } else {
            // use block biome tint colour
            this.rCol = ((tintColour >> 16 & 255) / 255f) * 0.75f;
            this.gCol = ((tintColour >> 8 & 255) / 255f) * 0.75f;
            this.bCol = ((tintColour & 255) / 255f) * 0.75f;
        }
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FallingTintedOrAverageDust(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 0.75f);
        }
    }

    public static class RandomisedSizeProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public RandomisedSizeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingTintedOrAverageDust particle = new FallingTintedOrAverageDust(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 0.75f);
            float particleSize = MathHelpers.randomBetween(0.08f, 0.12f);
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }
}
