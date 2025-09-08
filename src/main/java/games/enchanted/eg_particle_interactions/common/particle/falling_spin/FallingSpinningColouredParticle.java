package games.enchanted.eg_particle_interactions.common.particle.falling_spin;

import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
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

public class FallingSpinningColouredParticle extends FallingSpinningParticle {
    protected FallingSpinningColouredParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState, SpriteSet spriteSet, float gravityMultiplier) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier);
        int tintColour = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
        int[] tintColourARGB = ColourUtil.RGBint_to_ARGB(tintColour);
        int[] averageTextureColourARGB = ColourUtil.getRandomBlockColour(blockState, tintColourARGB);
        this.rCol = (float)averageTextureColourARGB[1] / 255f;
        this.gCol = (float)averageTextureColourARGB[2] / 255f;
        this.bCol = (float)averageTextureColourARGB[3] / 255f;
        this.alpha = (float)averageTextureColourARGB[0] / 255f;
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
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 1f);
            float particleSize = level.random.nextBoolean() ? 0.1f : 0.15f;
            particle.quadSize = particleSize;
            particle.maxSpinSpeed = 0.5f;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class FlowerPetalProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public FlowerPetalProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 1f);
            particle.maxSpinSpeed = 0.5f;
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
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 1f);
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
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 2f);
            float particleSize = level.random.nextBoolean() ? 0.10F : 0.12F;
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }

    public static class ChainSnapProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public ChainSnapProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingSpinningColouredParticle particle = new FallingSpinningColouredParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet, 3f);
            float particleSize = level.random.nextBoolean() ? 0.14F : 0.15F;
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            particle.maxSpinSpeed = 0.2f;
            particle.spinAcceleration = (float)Math.toRadians(level.random.nextBoolean() ? -1.0 : 1.0);
            return particle;
        }
    }
}