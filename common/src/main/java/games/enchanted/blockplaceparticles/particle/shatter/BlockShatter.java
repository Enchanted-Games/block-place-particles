package games.enchanted.blockplaceparticles.particle.shatter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockShatter extends AbstractShatter {
    protected final BlockState blockState;

    protected BlockShatter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState blockState) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.blockState = blockState;
        this.sprite = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState);
    }

    @Override
    protected Direction getParticleFacingDirection() {
        return this.blockState.hasProperty(NetherPortalBlock.AXIS) ? this.blockState.getValue(NetherPortalBlock.AXIS).getPositive() : null;
    }

    public static class BlockShatterProvider implements ParticleProvider<BlockParticleOption> {
        public BlockShatterProvider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BlockShatter(level, x, y, z, xSpeed, ySpeed, zSpeed, type.getState());
        }
    }
}
