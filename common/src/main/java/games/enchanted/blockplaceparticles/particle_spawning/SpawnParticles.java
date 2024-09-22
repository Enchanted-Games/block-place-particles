package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.util.BiomeTemperatureHelpers;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class SpawnParticles {
    public static void spawnBlockPlaceParticle(ClientLevel level, BlockPos blockPos, BlockState placedBlockState) {
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(placedBlockState);
        ParticleOptions particleOption;
        if (particleOverride == BlockParticleOverride.NONE) {
            return;
        } else if(particleOverride.isBlockStateParticle()) {
            particleOption = particleOverride.getBlockParticleOption(placedBlockState);
        }else {
            particleOption = particleOverride.getParticleOption();
        }

        int maxParticlesPerEdge = BlockParticleOverride.getParticleMultiplierForOverride(particleOverride, true);

        double particleOutwardVelocityAdjustment = particleOverride == BlockParticleOverride.BLOCK ? 1. : 0.05;
        final boolean particleInWarmBiome = BiomeTemperatureHelpers.isWarmBiomeOrDimension(level, blockPos);

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
                    double particleXOffset = (biggestEdge == Direction.Axis.X ? particlePos : width) + x1;
                    double particleYOffset = (biggestEdge == Direction.Axis.Y ? particlePos : height) + y1 + verticalAxisOffset;
                    double particleZOffset = (biggestEdge == Direction.Axis.Z ? particlePos : depth) + z1;

                    ParticleOptions particleToSpawn;
                    if(particleOverride == BlockParticleOverride.SNOW_POWDER) {
                        // sometimes spawn poof particle if the biome is ultra warm
                        particleToSpawn = particleInWarmBiome && level.random.nextInt(5) == 0 ? ParticleTypes.POOF : particleOption;
                    }else {
                        particleToSpawn = particleOption;
                    }

                    assert particleToSpawn != null;
                    level.addParticle(
                        particleToSpawn,
                        (double)blockPos.getX() + MathHelpers.expandWhenOutOfBound(particleXOffset, 0, 1),
                        (double)blockPos.getY() + MathHelpers.expandWhenOutOfBound(particleYOffset, 0, 1),
                        (double)blockPos.getZ() + MathHelpers.expandWhenOutOfBound(particleZOffset, 0, 1),
                        (particleXOffset - 0.5) * particleOutwardVelocityAdjustment,
                        (particleYOffset - 0.5) * particleOutwardVelocityAdjustment,
                        (particleZOffset - 0.5) * particleOutwardVelocityAdjustment
                    );
                }
            });
        }
    }
    public static void spawnBlockPlaceParticle(ClientLevel level, BlockPos blockPos) {
        BlockState placedBlockState = level.getBlockState(blockPos);
        spawnBlockPlaceParticle(level, blockPos, placedBlockState);
    }

    public static void spawnBlockBreakParticle(ClientLevel level, BlockState brokenBlockState, BlockPos brokenBlockPos, BlockParticleOverride particleOverride) {
        int maxParticlesPerLength = BlockParticleOverride.getParticleMultiplierForOverride(particleOverride, false);

        ParticleOptions particleOption;
        if (particleOverride == BlockParticleOverride.NONE) {
            return;
        } else if(particleOverride.isBlockStateParticle()) {
            particleOption = particleOverride.getBlockParticleOption(brokenBlockState);
        }else {
            particleOption = particleOverride.getParticleOption();
        }

        double particleOutwardVelocityAdjustment = particleOverride == BlockParticleOverride.BLOCK ? 1. : 0.1;
        final boolean particleInWarmBiome = BiomeTemperatureHelpers.isWarmBiomeOrDimension(level, brokenBlockPos);

        if (!brokenBlockState.isAir() && brokenBlockState.shouldSpawnTerrainParticles()) {
            VoxelShape blockShape = brokenBlockState.getShape(level, brokenBlockPos);
            blockShape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
                double width = Math.abs(x1 - x2);
                int amountAlongWidth = Math.clamp(Mth.ceil(width * maxParticlesPerLength), 1, 999);
                double height = Math.abs(y1 - y2);
                int amountAlongHeight = Math.clamp(Mth.ceil(height * maxParticlesPerLength), 1, 999);
                double depth = Math.abs(z1 - z2);
                int amountAlongDepth = Math.clamp(Mth.ceil(depth * maxParticlesPerLength), 1, 999);

                for(int i_W = 0; i_W < amountAlongWidth; ++i_W) {
                    for (int i_H = 0; i_H < amountAlongHeight; ++i_H) {
                        for (int i_D = 0; i_D < amountAlongDepth; ++i_D) {
                            double particleXOffset = (((double) i_W + 0.5) / (double) amountAlongWidth);
                            double particleYOffset = (((double) i_H + 0.5) / (double) amountAlongHeight);
                            double particleZOffset = (((double) i_D + 0.5) / (double) amountAlongDepth);

                            ParticleOptions particleToSpawn;
                            if (particleOverride == BlockParticleOverride.SNOW_POWDER) {
                                // sometimes spawn poof particle if the dimension is ultra warm
                                particleToSpawn = particleInWarmBiome && level.random.nextInt(5) == 0 ? ParticleTypes.POOF : particleOption;
                            } else {
                                particleToSpawn = particleOption;
                            }

                            assert particleToSpawn != null;
                            level.addParticle(
                                particleToSpawn,
                                brokenBlockPos.getX() + (particleXOffset * width + x1),
                                brokenBlockPos.getY() + (particleYOffset * height + y1),
                                brokenBlockPos.getZ() + (particleZOffset * depth + z1),
                                (particleXOffset - 0.5) * particleOutwardVelocityAdjustment,
                                (particleYOffset - 0.5) * particleOutwardVelocityAdjustment,
                                (particleZOffset - 0.5) * particleOutwardVelocityAdjustment
                            );
                        }
                    }
                }
            });
        }
    }

    public static void spawnSparksAtMinecartWheels(double minecartX, double minecartY, double minecartZ, double minecartHorizontalRot, double minecartVerticalRot, boolean isOnRails, boolean hasPassenger, Vec3 deltaMovement, double maxSpeed, Level level) {
        if(!isOnRails) return;
        if(!hasPassenger) return;
        float sparksChancePerWheel = (float) ( Math.clamp(MathHelpers.maxVec3(deltaMovement.toVector3f(), true), 0, maxSpeed) / maxSpeed ) - 0.75f;
        sparksChancePerWheel *= 2f;

        float rotX = (float) ((minecartHorizontalRot) * (Math.PI / 180));
        float rotY = (float) (minecartVerticalRot / 45);
        float sparkDeltaX = (float) Math.clamp(-deltaMovement.x / 2, -0.8, 0.8);
        float sparkDeltaZ = (float) Math.clamp(-deltaMovement.z / 2, -0.8, 0.8);

        if(level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos1 = minecartWheelPoint(rotX, rotY, 0.45f, 0.35f,  0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos1.x + minecartX, wheelPos1.y + minecartY, wheelPos1.z + minecartZ, sparkDeltaX , 0.3, sparkDeltaZ);
        }

        if(level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos2 = minecartWheelPoint(rotX, rotY, -0.45f, -0.35f, 0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos2.x + minecartX, wheelPos2.y + minecartY, wheelPos2.z + minecartZ, sparkDeltaX, 0.3, sparkDeltaZ);
        }

        if(level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos3 = minecartWheelPoint(rotX, rotY, 0.45f, 0.35f, -0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos3.x + minecartX, wheelPos3.y + minecartY, wheelPos3.z + minecartZ, sparkDeltaX, 0.3, sparkDeltaZ);
        }

        if(level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos4 = minecartWheelPoint(rotX, rotY, -0.45f, -0.35f, -0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos4.x + minecartX, wheelPos4.y + minecartY, wheelPos4.z + minecartZ, sparkDeltaX, 0.3, sparkDeltaZ);
        }
    }
    private static Vector3f minecartWheelPoint(float rotationX, float rotationY, float pointX, float pointY, float pointZ) {
        return new Vector3f((float) (pointX * Math.cos(rotationX) - pointZ * Math.sin(rotationX)), pointY * rotationY, (float) (pointZ * Math.cos(rotationX) + pointX * Math.sin(rotationX)));
    }

    public static void spawnFlintAndSteelSparkParticle(Level level, BlockPos particlePos) {
        if(!ConfigHandler.flintAndSteelSpark_onUse) return;
        double sparkIntensity = ConfigHandler.flintAndSteelSpark_intensity / 12.;
        for (int i = 0; i < ConfigHandler.maxFlintAndSteelSpark_onUse; i++) {
            double x = particlePos.getX() + 0.25 + (level.random.nextDouble() / 2);
            double y = particlePos.getY() + 0.25 + (level.random.nextDouble() / 2);
            double z = particlePos.getZ() + 0.25 + (level.random.nextDouble() / 2);
            level.addParticle(ModParticleTypes.FLYING_SPARK, x, y, z, (level.random.nextDouble() - 0.5) * sparkIntensity, (level.random.nextDouble() + 0.5) * sparkIntensity, (level.random.nextDouble() - 0.5) * sparkIntensity);
        }
    }

    public static void spawnAmbientCampfireSparks(Level level, BlockPos particlePos) {
        if(!ConfigHandler.campfireSpark_enabled) return;
        double sparkIntensity = 5 / 12.;
        if (level.random.nextFloat() * 101 <= ConfigHandler.campfireSpark_spawnChance) {
            for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 3) + 1; i++) {
                level.addParticle(
                    ModParticleTypes.FLOATING_SPARK,
                    (double)particlePos.getX() + 0.5,
                    (double)particlePos.getY() + 0.5,
                    (double)particlePos.getZ() + 0.5,
                    (level.random.nextDouble() - 0.5) * sparkIntensity,
                    Math.abs((level.random.nextDouble() - 0.25) * sparkIntensity * 2) + 0.1,
                    (level.random.nextDouble() - 0.5) * sparkIntensity
                );
            }
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

        FluidPlacementParticle particleOverride = FluidPlacementParticle.getParticleForFluid(placedFluid);

        ParticleOptions particleOption;
        if (particleOverride == FluidPlacementParticle.NONE) {
            return;
        } else if(particleOverride.isBlockStateParticle()) {
            particleOption = particleOverride.getBlockParticleOption(placedFluid.defaultFluidState().createLegacyBlock());
        }else {
            particleOption = particleOverride.getParticleOption();
        }

        if(particleOption == null) return;

        int maxParticles = FluidPlacementParticle.getParticleMultiplier(particleOverride, true);

        for (int i = 0; i < maxParticles; i++) {
            double x = particlePos.getX() + levelAccessor.getRandom().nextDouble();
            double y = particlePos.getY() + (levelAccessor.getRandom().nextDouble() / 1.5) + 0.6;
            double z = particlePos.getZ() + levelAccessor.getRandom().nextDouble();
            levelAccessor.addParticle(particleOption, x, y, z, 0.0, 0.21, 0.0);
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
