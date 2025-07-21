package games.enchanted.eg_particle_interactions.common.particle.dust;

import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FloatingColouredDust extends AbstractDust {
    protected final BlockState dustBlockState;

    protected FloatingColouredDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState, SpriteSet spriteSet, float gravityMultiplier, boolean spawnSpecks) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier, spawnSpecks);

        this.dustBlockState = blockState;

        int tintColour = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
        int[] tintColourARGB = ColourUtil.RGBint_to_ARGB(tintColour);
        int[] averageTextureColourARGB = ColourUtil.getRandomBlockColour(blockState, tintColourARGB);
        this.rCol = (float)averageTextureColourARGB[1] / 255f;
        this.gCol = (float)averageTextureColourARGB[2] / 255f;
        this.bCol = (float)averageTextureColourARGB[3] / 255f;
        this.alpha = (float)averageTextureColourARGB[0] / 255f;
    }

    @Override
    public @NotNull ParticleOptions getSpeckParticle() {
        return new BlockParticleOption(ModParticleTypes.TINTED_DUST_SPECK, this.dustBlockState);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        if(this.alpha < 0.99) {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }
        return super.getRenderType();
    }

    public static class TintedDustProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public TintedDustProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FloatingColouredDust(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), this.spriteSet, 0.7f, true);
        }
    }

    public static class TintedDustSpeckProvider implements ParticleProvider<BlockParticleOption>  {
        private final SpriteSet spriteSet;

        public TintedDustSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FloatingColouredDust(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), this.spriteSet, 0.35f, false);
        }
    }
}
