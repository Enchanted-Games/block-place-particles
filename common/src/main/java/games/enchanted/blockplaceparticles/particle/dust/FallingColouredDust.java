package games.enchanted.blockplaceparticles.particle.dust;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.util.ColourUtil;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FallingColouredDust extends FallingDust {
    protected final BlockState dustBlockState;

    protected FallingColouredDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState, SpriteSet spriteSet, float gravityMultiplier, boolean spawnSpecks) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier, spawnSpecks);

        this.dustBlockState = blockState;

        int tintColour = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
        if(tintColour == 0xffffff || tintColour == -1) {
            // use average texture colour
            int[] averageBlockColour = ColourUtil.getRandomBlockColour(blockState);
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
    public @NotNull ParticleOptions getSpeckParticle() {
        return new BlockParticleOption(ModParticleTypes.TINTED_DUST_SPECK, this.dustBlockState);
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingColouredDust particle = new FallingColouredDust(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), this.spriteSet, 0.7f, true);
            float particleSize = MathHelpers.randomBetween(0.08f, 0.12f);
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class SpeckProvider implements ParticleProvider<BlockParticleOption>  {
        private final SpriteSet spriteSet;

        public SpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingColouredDust particle = new FallingColouredDust(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), this.spriteSet, 0.35f, false);
            float particleSize = MathHelpers.randomBetween(0.08f, 0.12f);
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }
}
