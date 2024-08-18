package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

import games.enchanted.blockplaceparticles.util.MathHelpers;

public class SpawnParticles {
    public static void spawnBlockPlaceParticle(ClientLevel level, BlockPos blockPos) {
        double maxParticlesPerEdge = 4;
        BlockState placedBlockState = level.getBlockState(blockPos);

        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(placedBlockState);
        ParticleOptions particleOption;
        if (particleOverride == BlockParticleOverride.NONE) {
            particleOption = new BlockParticleOption(ParticleTypes.BLOCK, placedBlockState);
        } else if(particleOverride.isBlockStateParticle()) {
            particleOption = particleOverride.getBlockParticleOption(placedBlockState);
        }else {
            particleOption = particleOverride.getParticleOption();
        }

        if (!placedBlockState.isAir() && placedBlockState.shouldSpawnTerrainParticles()) {
            VoxelShape blockShape = placedBlockState.getShape(level, blockPos);
            double verticalAxisOffset = level.getBlockState(blockPos.offset(0, -1, 0)).isSolid() ? 0.01 : 0; // move particles up out the block below them if it is solid
            blockShape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
                double width = Math.abs(x1 - x2);
                double height = Math.abs(y1 - y2);
                double depth = Math.abs(z1 - z2);

                double edgeLength;
                Direction.Axis biggestEdge;
                if(width > height && width > depth) {
                    edgeLength = width;
                    biggestEdge = Direction.Axis.X;
                } else if(height > width && height > depth) {
                    edgeLength = height;
                    biggestEdge = Direction.Axis.Y;
                } else {
                    edgeLength = depth;
                    biggestEdge = Direction.Axis.Z;
                }

                int amountOfParticlesAlongEdge = Mth.ceil(edgeLength * maxParticlesPerEdge);
                if(amountOfParticlesAlongEdge < 1) amountOfParticlesAlongEdge = 1; // always try to spawn at least 1 particle per edge

                for(int i = 0; i < amountOfParticlesAlongEdge; ++i) {
                    double particlePos = ((double)i + 0.5) / (double)amountOfParticlesAlongEdge;
                    if (particlePos > edgeLength + (double) 1/16) continue;
                    double particleXOffset = (biggestEdge == Direction.Axis.X ? particlePos : 1 * width) + x1;
                    double particleYOffset = (biggestEdge == Direction.Axis.Y ? particlePos : 1 * height) + y1 + verticalAxisOffset;
                    double particleZOffset = (biggestEdge == Direction.Axis.Z ? particlePos : 1 * depth) + z1;
                    ParticleOptions particleToSpawn;
                    if(particleOverride == BlockParticleOverride.SNOW_POWDER) {
                        // sometimes spawn poof particle if the biome is ultra warm
                        particleToSpawn = level.dimensionType().ultraWarm() && level.random.nextInt(5) == 0 ? ParticleTypes.POOF : particleOption;
                    }else {
                        particleToSpawn = particleOption;
                    }
                    level.addParticle(
                        particleToSpawn,
                        (double)blockPos.getX() + MathHelpers.expandWhenOutOfBound(particleXOffset, 0, 1),
                        (double)blockPos.getY() + MathHelpers.expandWhenOutOfBound(particleYOffset, 0, 1),
                        (double)blockPos.getZ() + MathHelpers.expandWhenOutOfBound(particleZOffset, 0, 1),
                        0, 0, 0
                    );
                }
            });
        }
    }

    public static void spawnFlintAndSteelSparkParticle(Level level, BlockPos particlePos) {
        for (int i = 0; i < 6; i++) {
            double x = particlePos.getX() + level.random.nextDouble();
            double y = particlePos.getY() + 0.2;
            double z = particlePos.getZ() + level.random.nextDouble();
            level.addParticle(ParticleTypes.NOTE, x, y, z, 0.0, 0.05, 0.0);
        }
    }

    public static void spawnFireChargeSmokeParticle(Level level, BlockPos particlePos) {
        for (int i = 0; i < 6; i++) {
            double x = particlePos.getX() + level.random.nextDouble();
            double y = particlePos.getY() + 0.2;
            double z = particlePos.getZ() + level.random.nextDouble();
            level.addParticle(ParticleTypes.DUST_PLUME, x, y, z, 0.0, 0.05, 0.0);
        }
    }

    public static void spawnHoeTillParticle(Level level, BlockPos particlePos, Player player) {
        for (int i = 0; i < 6; i++) {
            double x = particlePos.getX() + level.random.nextDouble();
            double y = particlePos.getY() + (double) 14 /16;
            double z = particlePos.getZ() + level.random.nextDouble();
            level.addParticle(ParticleTypes.DUST_PLUME, x, y, z, 0.0, 0.05, 0.0);
        }
    }

    // not implemented
    public static void spawnFarmlandTrampleParticle(Level level, BlockPos particlePos) {
    }

    public static void spawnShovelFlattenParticle(Level level, BlockPos particlePos, Player player) {
        for (int i = 0; i < 6; i++) {
            double x = particlePos.getX() + level.random.nextDouble();
            double y = particlePos.getY() + (double) 14 /16;
            double z = particlePos.getZ() + level.random.nextDouble();
            level.addParticle(ParticleTypes.DUST_PLUME, x, y, z, 0.0, 0.05, 0.0);
        }
    }

    public static void spawnFluidPlacedParticle(LevelAccessor levelAccessor, BlockPos particlePos, Fluid placedFluid) {
        if (placedFluid.isSame(Fluids.EMPTY)) {
            return;
        }
        ResourceLocation loc = null;
        try {
            loc = ResourceLocation.parse("abc:test");
        } catch (ResourceLocationException exception) {
            ParticleInteractionsLogging.message(exception.getMessage());
            return;
        }
        ParticleInteractionsLogging.message(loc.toString());
        ParticleOptions fluidParticle = placedFluid.defaultFluidState().getDripParticle();
        for (int i = 0; i < 6; i++) {
            double x = particlePos.getX() + levelAccessor.getRandom().nextDouble();
            double y = particlePos.getY() + (double) 13/16;
            double z = particlePos.getZ() + levelAccessor.getRandom().nextDouble();
            levelAccessor.addParticle(fluidParticle, x, y, z, 0.0, 0.05, 0.0);
        }
    }

    public static void spawnAxeStripParticle(Level level, BlockPos particlePos, Player player) {
        for (int i = 0; i < 6; i++) {
            double x = particlePos.getX() + level.random.nextDouble();
            double y = particlePos.getY() + level.random.nextDouble();
            double z = particlePos.getZ() + level.random.nextDouble();
            level.addParticle(ParticleTypes.DUST_PLUME, x, y, z, 0.0, 0.05, 0.0);
        }
    }
}
