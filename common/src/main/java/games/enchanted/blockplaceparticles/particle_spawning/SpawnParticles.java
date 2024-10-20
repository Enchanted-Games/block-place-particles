package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle_spawning.override.BlockParticleOverride;
import games.enchanted.blockplaceparticles.particle_spawning.override.FluidPlacementParticle;
import games.enchanted.blockplaceparticles.util.FluidHelpers;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class SpawnParticles {
    public static void spawnBlockPlaceParticle(ClientLevel level, BlockPos blockPos, BlockState placedBlockState) {
        if(ConfigHandler.underwaterBubbles_onPlace) spawnUnderwaterBubbles(ConfigHandler.maxUnderwaterBubbles_onPlace, level, blockPos);

        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(placedBlockState);
        if (particleOverride == BlockParticleOverride.NONE) {
            return;
        }

        int maxParticlesPerEdge = BlockParticleOverride.getParticleMultiplierForOverride(particleOverride, true);
        if(maxParticlesPerEdge <= 0) return;

        double particleOutwardVelocityAdjustment = particleOverride.getParticleVelocityMultiplier();

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

                    ParticleOptions particleToSpawn = particleOverride.getParticleOptionForState(placedBlockState, level, blockPos);
                    if (particleToSpawn == null) {
                        continue;
                    }
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
        if(ConfigHandler.underwaterBubbles_onBreak) spawnUnderwaterBubbles(ConfigHandler.maxUnderwaterBubbles_onBreak, level, brokenBlockPos);

        if (particleOverride == BlockParticleOverride.NONE) {
            return;
        }

        int maxParticlesPerLength = BlockParticleOverride.getParticleMultiplierForOverride(particleOverride, false);
        if(maxParticlesPerLength <= 0) return;

        double particleOutwardVelocityAdjustment = particleOverride.getParticleVelocityMultiplier();

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

                            ParticleOptions particleToSpawn = particleOverride.getParticleOptionForState(brokenBlockState, level, brokenBlockPos);
                            if (particleToSpawn == null) {
                                continue;
                            }

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
    private static void spawnUnderwaterBubbles(int amountOfBubbles, Level level, BlockPos blockPos) {
        if(!FluidHelpers.probablyPlacedUnderwater(level, blockPos)) return;
        for (int i = 0; i < Math.max(amountOfBubbles + level.random.nextIntBetweenInclusive(-2, 0), 1); i++) {
            double x = level.random.nextDouble();
            double y = level.random.nextDouble();
            double z = level.random.nextDouble();
            boolean blockAboveIsWater = level.getFluidState(blockPos.above()).is(FluidTags.WATER);
            double verticalVelocity = (y - 0.5) * (blockAboveIsWater ? 2 : 0);
            double horizontalVelocityMul = !blockAboveIsWater ? 1.5 : 1;
            level.addParticle(
                ModParticleTypes.UNDERWATER_RISING_BUBBLE,
                blockPos.getX() + x,
                blockPos.getY() + y,
                blockPos.getZ() + z,
                (x - 0.5) * 2 * horizontalVelocityMul,
                level.getBlockState(blockPos.below()).isSolid() ? Math.abs(verticalVelocity) + 0.1 : verticalVelocity,
                (z - 0.5) * 2 * horizontalVelocityMul
            );
        }
    }

    public static void spawnSparksAtMinecartWheels(double minecartX, double minecartY, double minecartZ, double minecartHorizontalRot, double minecartVerticalRot, boolean isOnRails, boolean hasPassenger, boolean hasBlock, Vec3 deltaMovement, double maxSpeed, Level level) {
        if(!ConfigHandler.minecart_enabled) return;
        if(!isOnRails) return;
        if(!(hasBlock || hasPassenger) && ConfigHandler.minecart_onlyWithPassenger) return;

        double speed = MathHelpers.maxVec3(deltaMovement.toVector3f(), true);
        if(speed < 0.05) return;

        float sparksChancePerWheel = (float) ( Math.clamp(speed, 0, maxSpeed) / maxSpeed ) - 0.75f;
        sparksChancePerWheel *= ConfigHandler.minecart_spawnChance / 50f;

        float rotX = (float) ((minecartHorizontalRot) * (Math.PI / 180));
        float rotY = (float) (minecartVerticalRot / 45);
        float sparkDeltaX = (float) Math.clamp(-deltaMovement.x / 3, -0.7, 0.7);
        float sparkDeltaZ = (float) Math.clamp(-deltaMovement.z / 3, -0.7, 0.7);

        minecartY += 0.0425;
        if(level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos1 = minecartWheelPoint(rotX, rotY, 0.45f, 0.35f,  0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos1.x + minecartX, wheelPos1.y + minecartY, wheelPos1.z + minecartZ, sparkDeltaX , 0.17, sparkDeltaZ);
        }
        if(level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos2 = minecartWheelPoint(rotX, rotY, -0.45f, -0.35f, 0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos2.x + minecartX, wheelPos2.y + minecartY, wheelPos2.z + minecartZ, sparkDeltaX, 0.17, sparkDeltaZ);
        }
        if(level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos3 = minecartWheelPoint(rotX, rotY, 0.45f, 0.35f, -0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos3.x + minecartX, wheelPos3.y + minecartY, wheelPos3.z + minecartZ, sparkDeltaX, 0.17, sparkDeltaZ);
        }
        if(level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos4 = minecartWheelPoint(rotX, rotY, -0.45f, -0.35f, -0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos4.x + minecartX, wheelPos4.y + minecartY, wheelPos4.z + minecartZ, sparkDeltaX, 0.17, sparkDeltaZ);
        }
    }
    private static Vector3f minecartWheelPoint(float rotationX, float rotationY, float pointX, float pointY, float pointZ) {
        return new Vector3f((float) (pointX * Math.cos(rotationX) - pointZ * Math.sin(rotationX)), pointY * rotationY, (float) (pointZ * Math.cos(rotationX) + pointX * Math.sin(rotationX)));
    }

    public static void spawnFlintAndSteelSparkParticle(Level level, BlockPos particlePos) {
        if(!ConfigHandler.flintAndSteelSpark_onUse) return;
        BlockState fireOrLitBlock = level.getBlockState(particlePos);
        boolean isSoulBlock = level.getBlockState(particlePos.below()).is(BlockTags.SOUL_FIRE_BASE_BLOCKS) || fireOrLitBlock.is(Blocks.SOUL_CAMPFIRE);
        double sparkIntensity = ConfigHandler.flintAndSteelSpark_intensity / 12.;
        for (int i = 0; i < ConfigHandler.maxFlintAndSteelSpark_onUse; i++) {
            double x = particlePos.getX() + 0.25 + (level.random.nextDouble() / 2);
            double y = particlePos.getY() + 0.25 + (level.random.nextDouble() / 2);
            double z = particlePos.getZ() + 0.25 + (level.random.nextDouble() / 2);
            level.addParticle(isSoulBlock ? ModParticleTypes.FLYING_SOUL_SPARK : ModParticleTypes.FLYING_SPARK, x, y, z, (level.random.nextDouble() - 0.5) * sparkIntensity, (level.random.nextDouble() + 0.5) * sparkIntensity, (level.random.nextDouble() - 0.5) * sparkIntensity);
        }
    }

    public static void spawnAmbientCampfireSparks(Level level, BlockPos particlePos, BlockState campfireState) {
        if(!ConfigHandler.campfireSpark_enabled) return;
        double sparkIntensity = 5 / 12.;
        if (level.random.nextFloat() * 101 <= ConfigHandler.campfireSpark_spawnChance) {
            for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 3) + 1; i++) {
                spawnMostlyUpwardsMotionParticleOption(
                    level,
                    campfireState.is(Blocks.SOUL_CAMPFIRE) ? ModParticleTypes.FLOATING_SOUL_SPARK : ModParticleTypes.FLOATING_SPARK,
                    (double)particlePos.getX() + 0.5,
                    (double)particlePos.getY() + 0.5,
                    (double)particlePos.getZ() + 0.5,
                    sparkIntensity
                );
            }
        }
    }

    public static void spawnAmbientFireSparks(Level level, BlockState fireState, BlockPos particlePos, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if(!ConfigHandler.fireSpark_enabled) return;
        double sparkIntensity = 5 / 12.;
        double width = Math.abs(minX - maxX);
        double height = Math.abs(minY - maxY);
        double depth = Math.abs(minZ - maxZ);
        if (level.random.nextFloat() * 101 <= ConfigHandler.fireSpark_spawnChance) {
            for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 3) + 1; i++) {
                spawnMostlyUpwardsMotionParticleOption(
                    level,
                    fireState.is(Blocks.SOUL_FIRE) ? ModParticleTypes.FLOATING_SOUL_SPARK : ModParticleTypes.FLOATING_SPARK,
                    particlePos.getX() + minX + (level.random.nextFloat() * width),
                    particlePos.getY() + minY + (level.random.nextFloat() * height),
                    particlePos.getZ() + minZ + (level.random.nextFloat() * depth),
                    sparkIntensity
                );
            }
        }
    }

    public static void spawnFireChargeSmokeParticle(Level level, BlockPos particlePos) {
        if(!ConfigHandler.fireCharge_onUse) return;
        double lavaIntensity = ConfigHandler.fireCharge_intensity / 24.;
        double smokeIntensity = ConfigHandler.fireCharge_intensity / 58.;
        for (int i = 0; i < ConfigHandler.maxFireCharge_onUse; i++) {
            double x = particlePos.getX() + 0.25 + (level.random.nextDouble() / 2);
            double y = particlePos.getY() + 0.25 + (level.random.nextDouble() / 2);
            double z = particlePos.getZ() + 0.25 + (level.random.nextDouble() / 2);
            if(level.random.nextFloat() > 0.2) {
                level.addParticle(level.random.nextFloat() > 0.3 ? ParticleTypes.SMOKE : ParticleTypes.LARGE_SMOKE, x, y, z, (level.random.nextDouble() - 0.5) * smokeIntensity, (level.random.nextDouble() + 0.5) * smokeIntensity, (level.random.nextDouble() - 0.5) * smokeIntensity);
                continue;
            }
            level.addParticle(ParticleTypes.LAVA, x, y, z, (level.random.nextDouble() - 0.5) * lavaIntensity, (level.random.nextDouble() + 0.5) * lavaIntensity, (level.random.nextDouble() - 0.5) * lavaIntensity);
        }
    }

    public static void spawnHoeTillParticle(Level level, BlockPos blockPos, UseOnContext context) {
        if(!ConfigHandler.hoeTill_onUse) return;
        Vec3 clickedPosition = context.getClickLocation();
        Direction clickDirection = context.getClickedFace();
        for (int i = 0; i < ConfigHandler.maxHoeTill_onUse; i++) {
            double x = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepX());
            double y = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepY());
            double z = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepZ());
            ParticleOptions blockParticle = new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockPos));
            level.addParticle(
                blockParticle,
                clickedPosition.x + x,
                clickedPosition.y + y,
                clickedPosition.z + z,
                clickDirection.getStepX() + (level.random.nextDouble() - 0.5),
                clickDirection.getStepY() + (level.random.nextDouble() - 0.5),
                clickDirection.getStepZ() + (level.random.nextDouble() - 0.5)
            );
        }
    }

    public static void spawnShovelFlattenParticle(Level level, BlockPos blockPos, UseOnContext context) {
        if(!ConfigHandler.shovelFlatten_onUse) return;
        Vec3 clickedPosition = context.getClickLocation();
        Direction clickDirection = context.getClickedFace();
        for (int i = 0; i < ConfigHandler.maxShovelFlatten_onUse; i++) {
            double x = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepX());
            double y = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepY());
            double z = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepZ());
            ParticleOptions blockParticle = new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockPos));
            level.addParticle(
                blockParticle,
                clickedPosition.x + x,
                clickedPosition.y + y,
                clickedPosition.z + z,
                clickDirection.getStepX() + (level.random.nextDouble() - 0.5),
                clickDirection.getStepY() + (level.random.nextDouble() - 0.5),
                clickDirection.getStepZ() + (level.random.nextDouble() - 0.5)
            );
        }
    }

    public static void spawnAxeStripParticle(Level level, BlockPos blockPos, BlockState unstrippedBlockState, BlockState strippedBlockState, UseOnContext context) {
        if(!ConfigHandler.axeStrip_onUse) return;
        Vec3 clickedPosition = context.getClickLocation();
        Direction clickDirection = context.getClickedFace();
        for (int i = 0; i < ConfigHandler.maxAxeStrip_onUse; i++) {
            double x = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepX());
            double y = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepY());
            double z = (level.random.nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepZ());
            ParticleOptions blockParticle = level.random.nextFloat() > 0.9 ? new BlockParticleOption(ParticleTypes.BLOCK, strippedBlockState) : new BlockParticleOption(ParticleTypes.BLOCK, unstrippedBlockState);
            level.addParticle(
                blockParticle,
                clickedPosition.x + x,
                clickedPosition.y + y,
                clickedPosition.z + z,
                clickDirection.getStepX() + (level.random.nextDouble() - 0.5),
                clickDirection.getStepY() + (level.random.nextDouble() - 0.5),
                clickDirection.getStepZ() + (level.random.nextDouble() - 0.5)
            );
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


    private static void spawnMostlyUpwardsMotionParticleOption(Level level, ParticleOptions particleOptions, double xPos, double yPos, double zPos, double velocityIntensity) {
        level.addParticle(
            particleOptions,
            xPos,
            yPos,
            zPos,
            (level.random.nextDouble() - 0.5) * velocityIntensity * 0.4,
            Math.abs((level.random.nextDouble() - 0.25) * velocityIntensity) + 0.25,
            (level.random.nextDouble() - 0.5) * velocityIntensity * 0.4
        );
    }
}
