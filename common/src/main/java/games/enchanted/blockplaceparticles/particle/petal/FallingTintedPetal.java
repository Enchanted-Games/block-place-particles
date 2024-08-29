package games.enchanted.blockplaceparticles.particle.petal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FallingTintedPetal extends FallingPetal {
    protected FallingTintedPetal(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        int tintColour = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
        this.rCol *= (float)(tintColour >> 16 & 255) / 255.0F;
        this.gCol *= (float)(tintColour >> 8 & 255) / 255.0F;
        this.bCol *= (float)(tintColour & 255) / 255.0F;
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FallingTintedPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), spriteSet);
        }
    }
}