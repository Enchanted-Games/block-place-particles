package games.enchanted.eg_particle_interactions.common.particle.types;

import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.TerrainParticleInvoker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CustomMovementTerrainParticle extends TerrainParticle {
    public CustomMovementTerrainParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, state, pos);
    }

    public static class CrackingProvider implements ParticleProvider<BlockParticleOption> {
        public CrackingProvider(SpriteSet spriteSet) {}

        @Override
        public @Nullable Particle createParticle(
            BlockParticleOption type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            Particle particle = TerrainParticleInvoker.block_place_particle$invokeCreateTerrainParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
            if (particle != null) {
                particle.setPower(0.2F).scale(0.6F);
            }
            return particle;
        }
    }

    public static class UncappedMotionProvider implements ParticleProvider<BlockParticleOption> {
        public UncappedMotionProvider(SpriteSet spriteSet) {}

        @Override
        public @Nullable Particle createParticle(
            BlockParticleOption type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return TerrainParticleInvoker.block_place_particle$invokeCreateTerrainParticle(type, level, x, y, z, xSpeed * 6, ySpeed * 6, zSpeed * 6);
        }
    }
}
