package games.enchanted.eg_particle_interactions.common.particle.types;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CustomMovementTerrainParticle extends TerrainParticle {
    public CustomMovementTerrainParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, state, pos);
    }

    public static class BlockProvider implements PIParticleProvider<PIParticleType.Simple> {
        public BlockProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            ParticleContext.BlockContext blockContext = context.blockContext();
            if (blockContext == null) return null;
            BlockState state = blockContext.state();
            if (!state.isAir() && !state.is(Blocks.MOVING_PISTON) && state.shouldSpawnTerrainParticles()) {
                return new CustomMovementTerrainParticle(context.level(), x, y, z, xSpeed, ySpeed, zSpeed, blockContext.state(), blockContext.pos());
            }
            return null;
        }
    }

    public static class CrackingProvider implements PIParticleProvider<PIParticleType.Simple> {
        public CrackingProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            ParticleContext.BlockContext blockContext = context.blockContext();
            if (blockContext == null) return null;
            BlockState state = blockContext.state();
            if (!state.isAir() && !state.is(Blocks.MOVING_PISTON) && state.shouldSpawnTerrainParticles()) {
                Particle particle = new CustomMovementTerrainParticle(context.level(), x, y, z, xSpeed, ySpeed, zSpeed, blockContext.state(), blockContext.pos());
                particle.setPower(0.2F).scale(0.6F);
                return particle;
            }
            return null;
        }
    }
}
