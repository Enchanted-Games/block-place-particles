package games.enchanted.blockplaceparticles.particle.petal;

import games.enchanted.blockplaceparticles.util.ColourUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FallingTintedOrAveragePetal extends FallingPetal {
    protected FallingTintedOrAveragePetal(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState, SpriteSet spriteSet, float gravityMultiplier) {
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

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        if(this.alpha < 0.99) {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }
        return super.getRenderType();
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FallingTintedOrAveragePetal(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 1f);
        }
    }

    public static class LargerSpriteProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public LargerSpriteProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingTintedOrAveragePetal particle = new FallingTintedOrAveragePetal(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 1f);
            float particleSize = level.random.nextBoolean() ? 0.10F : 0.12F;
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class LargerSpriteMoreGravityProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public LargerSpriteMoreGravityProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingTintedOrAveragePetal particle = new FallingTintedOrAveragePetal(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 2f);
            float particleSize = level.random.nextBoolean() ? 0.10F : 0.12F;
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }
}