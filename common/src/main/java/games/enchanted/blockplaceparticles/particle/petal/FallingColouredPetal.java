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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FallingColouredPetal extends FallingPetal {
    protected FallingColouredPetal(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState, SpriteSet spriteSet, float gravityMultiplier) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier);
        int tintColour = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
        int[] tintColourRGB = ColourUtil.RGBint_to_ARGB(tintColour);
        int[] averageTextureColourRGB = ColourUtil.getRandomBlockColour(blockState);
        int[] multipliedColour = ColourUtil.multiplyColours(tintColourRGB, averageTextureColourRGB);
        this.rCol = (float)multipliedColour[1] / 255f;
        this.gCol = (float)multipliedColour[2] / 255f;
        this.bCol = (float)multipliedColour[3] / 255f;
        this.alpha = (float)multipliedColour[0] / 255f;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        if(this.alpha < 0.99) {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }
        return super.getRenderType();
    }

    public static class TintedLeafProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public TintedLeafProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingColouredPetal particle = new FallingColouredPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 1f);
            float particleSize = level.random.nextBoolean() ? 0.1f : 0.15f;
            particle.quadSize = particleSize;
            particle.maxSpinSpeed = 0.5f;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class GrassBladeProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public GrassBladeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingColouredPetal particle = new FallingColouredPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 1f);
            float particleSize = level.random.nextBoolean() ? 0.10F : 0.12F;
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class HeavyGrassBladeProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public HeavyGrassBladeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingColouredPetal particle = new FallingColouredPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 2f);
            float particleSize = level.random.nextBoolean() ? 0.10F : 0.12F;
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }
}