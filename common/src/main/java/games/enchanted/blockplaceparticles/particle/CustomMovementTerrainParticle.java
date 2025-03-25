package games.enchanted.blockplaceparticles.particle;

import games.enchanted.blockplaceparticles.mixin.accessor.client.TerrainParticleInvoker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomMovementTerrainParticle extends TerrainParticle {
    public CustomMovementTerrainParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, state, pos);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return super.getRenderType();
    }

    public static class CrackingProvider implements ParticleProvider<BlockParticleOption> {
        public CrackingProvider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption particleOption, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = TerrainParticleInvoker.block_place_particle$invokeCreateTerrainParticle(particleOption, level, x, y, z, xSpeed, ySpeed, zSpeed);
            if (particle != null) {
                particle.setPower(0.2F).scale(0.6F);
            }
            return particle;
        }
    }

    public static class UncappedMotionProvider implements ParticleProvider<BlockParticleOption> {
        public UncappedMotionProvider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption particleOption, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return TerrainParticleInvoker.block_place_particle$invokeCreateTerrainParticle(particleOption, level, x, y, z, xSpeed * 6, ySpeed * 6, zSpeed * 6);
        }
    }
}
