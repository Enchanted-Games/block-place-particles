package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.config.type.BrushParticleBehaviour;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.option.ParticleEmitterOptions;
import games.enchanted.blockplaceparticles.particle.option.TintedParticleOption;
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
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class SpawnParticles {
    public static void spawnBlockPlaceParticle(ClientLevel level, BlockPos blockPos, BlockState placedBlockState) {
        if (ConfigHandler.underwaterBubbles_onPlace) spawnUnderwaterBubbles(ConfigHandler.maxUnderwaterBubbles_onPlace, level, blockPos);

        int overrideOrigin = BlockParticleOverride.ORIGIN_BLOCK_PLACED;
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(placedBlockState, overrideOrigin);
        if (particleOverride == BlockParticleOverride.NONE) {
            return;
        }

        int maxParticlesPerEdge = BlockParticleOverride.getParticleMultiplierForOverride(particleOverride, true);
        if (maxParticlesPerEdge <= 0) return;

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
                if (width > height && width > depth) {
                    edgeLength = width;
                    biggestEdge = Direction.Axis.X;
                } else if (height > width && height > depth) {
                    edgeLength = height;
                    biggestEdge = Direction.Axis.Y;
                } else {
                    edgeLength = depth;
                    biggestEdge = Direction.Axis.Z;
                }

                int amountOfParticlesAlongEdge = Mth.ceil(edgeLength * maxParticlesPerEdge);
                if (amountOfParticlesAlongEdge < 1) amountOfParticlesAlongEdge = 1; // always try to spawn at least 1 particle per edge

                for (int i = 0; i < amountOfParticlesAlongEdge; ++i) {
                    double particlePos = ((double) i + 0.5) / (double) amountOfParticlesAlongEdge;
                    if (particlePos > edgeLength + (double) 1 / 16) continue;
                    double particleXOffset = (biggestEdge == Direction.Axis.X ? particlePos : width) + x1;
                    double particleYOffset = (biggestEdge == Direction.Axis.Y ? particlePos : height) + y1 + verticalAxisOffset;
                    double particleZOffset = (biggestEdge == Direction.Axis.Z ? particlePos : depth) + z1;

                    ParticleOptions particleToSpawn = particleOverride.getParticleOptionForState(placedBlockState, level, blockPos, overrideOrigin);
                    if (particleToSpawn == null) {
                        continue;
                    }
                    level.addParticle(
                        particleToSpawn,
                        (double) blockPos.getX() + MathHelpers.expandWhenOutOfBound(particleXOffset, 0, 1),
                        (double) blockPos.getY() + MathHelpers.expandWhenOutOfBound(particleYOffset, 0, 1),
                        (double) blockPos.getZ() + MathHelpers.expandWhenOutOfBound(particleZOffset, 0, 1),
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
        if (ConfigHandler.underwaterBubbles_onBreak) spawnUnderwaterBubbles(ConfigHandler.maxUnderwaterBubbles_onBreak, level, brokenBlockPos);

        if (particleOverride == BlockParticleOverride.NONE) {
            return;
        }

        int maxParticlesPerLength = BlockParticleOverride.getParticleMultiplierForOverride(particleOverride, false);
        if (maxParticlesPerLength <= 0) return;

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

                for (int i_W = 0; i_W < amountAlongWidth; ++i_W) {
                    for (int i_H = 0; i_H < amountAlongHeight; ++i_H) {
                        for (int i_D = 0; i_D < amountAlongDepth; ++i_D) {
                            double particleXOffset = (((double) i_W + 0.5) / (double) amountAlongWidth);
                            double particleYOffset = (((double) i_H + 0.5) / (double) amountAlongHeight);
                            double particleZOffset = (((double) i_D + 0.5) / (double) amountAlongDepth);

                            ParticleOptions particleToSpawn = particleOverride.getParticleOptionForState(brokenBlockState, level, brokenBlockPos, BlockParticleOverride.ORIGIN_BLOCK_BROKEN);
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
        if (!FluidHelpers.probablyPlacedUnderwater(level, blockPos)) return;
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

    public static void spawnFallingBlockRandomFallParticles(ClientLevel level, BlockState blockState, double x, double y, double z, Vec3 deltaMovement) {
        if(blockState.isAir()) return;
        int overrideOrigin = BlockParticleOverride.ORIGIN_FALLING_BLOCK_FALLING;
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(blockState, overrideOrigin);

        if(particleOverride == BlockParticleOverride.NONE || particleOverride == BlockParticleOverride.VANILLA) return;

        for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 4); i++) {
            ParticleOptions particleOptions = particleOverride.getParticleOptionForState(blockState, level, BlockPos.containing(x, y, z), overrideOrigin);
            if(particleOptions == null) continue;
            level.addParticle(
                particleOptions,
                x - 0.5 + level.random.nextFloat(),
                y       + level.random.nextFloat(),
                z - 0.5 + level.random.nextFloat(),
                (deltaMovement.x * 3) * -particleOverride.getParticleVelocityMultiplier(),
                (deltaMovement.y * 3) * -particleOverride.getParticleVelocityMultiplier(),
                (deltaMovement.z * 3) * -particleOverride.getParticleVelocityMultiplier()
            );
        }
    }

    public static void spawnFallingBlockLandParticles(ClientLevel level, BlockState blockState, double x, double y, double z, Vec3 deltaMovement) {
        int overrideOrigin = BlockParticleOverride.ORIGIN_FALLING_BLOCK_LANDED;
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(blockState, overrideOrigin);

        if(particleOverride == BlockParticleOverride.NONE) return;

        BlockPos blockPos = BlockPos.containing(x, y, z);
        double movementSpeed = deltaMovement.length();

        double particleY = Math.round((y + (deltaMovement.y / 2)) - 0.1) + 0.0625;

        SpawnParticlesUtil.spawnParticleInCircle(
            particleOverride == BlockParticleOverride.VANILLA ? TintedParticleOption.BRUSH_OPTION : particleOverride.getParticleOptionForState(blockState, level, blockPos, overrideOrigin),
            level,
            new Vec3(x, particleY, z),
            16,
            0.4f,
            0.9f,
            1.7f * (float) (movementSpeed * 2) * (particleOverride == BlockParticleOverride.VANILLA ? 0.1f : particleOverride.getParticleVelocityMultiplier()),
            0.035f,
            0
        );

        SpawnParticlesUtil.spawnParticleInCircle(
            particleOverride == BlockParticleOverride.VANILLA ? TintedParticleOption.BRUSH_OPTION : particleOverride.getParticleOptionForState(blockState, level, blockPos, overrideOrigin),
            level,
            new Vec3(x, particleY + 0.7f, z),
            16,
            0.3f,
            0.95f,
            0.2f,
            -0.4f * (float) (movementSpeed * 2) * (particleOverride == BlockParticleOverride.VANILLA ? 0.1f : particleOverride.getParticleVelocityMultiplier()),
            0
        );
    }

    public static void spawnSparksAtMinecartWheels(double minecartX, double minecartY, double minecartZ, double minecartHorizontalRot, double minecartVerticalRot, boolean isOnRails, boolean hasPassenger, boolean hasBlock, Vec3 deltaMovement, double maxSpeed, Level level) {
        if (!ConfigHandler.minecart_enabled) return;
        if (!isOnRails) return;
        if (!(hasBlock || hasPassenger) && ConfigHandler.minecart_onlyWithPassenger) return;

        double speed = MathHelpers.maxVec3(deltaMovement.toVector3f(), true);
        if (speed < 0.05) return;

        float sparksChancePerWheel = (float) (Math.clamp(speed, 0, maxSpeed) / maxSpeed) - 0.75f;
        sparksChancePerWheel *= ConfigHandler.minecart_spawnChance / 50f;

        float rotX = (float) ((minecartHorizontalRot) * (Math.PI / 180));
        float rotY = (float) ((minecartVerticalRot) * (Math.PI / 180));
        float sparkDeltaX = (float) Math.clamp(-deltaMovement.x / 3, -0.7, 0.7);
        float sparkDeltaZ = (float) Math.clamp(-deltaMovement.z / 3, -0.7, 0.7);

        minecartY += 0.0425;
        if (level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos1 = minecartWheelPoint(rotX, rotY, 0.45f, 0.35f, 0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos1.x + minecartX, wheelPos1.y + minecartY, wheelPos1.z + minecartZ, sparkDeltaX, 0.17, sparkDeltaZ);
        }
        if (level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos2 = minecartWheelPoint(rotX, rotY, -0.45f, -0.35f, 0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos2.x + minecartX, wheelPos2.y + minecartY, wheelPos2.z + minecartZ, sparkDeltaX, 0.17, sparkDeltaZ);
        }
        if (level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos3 = minecartWheelPoint(rotX, rotY, 0.45f, 0.35f, -0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos3.x + minecartX, wheelPos3.y + minecartY, wheelPos3.z + minecartZ, sparkDeltaX, 0.17, sparkDeltaZ);
        }
        if (level.random.nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos4 = minecartWheelPoint(rotX, rotY, -0.45f, -0.35f, -0.45f);
            level.addParticle(ModParticleTypes.FLYING_SPARK, wheelPos4.x + minecartX, wheelPos4.y + minecartY, wheelPos4.z + minecartZ, sparkDeltaX, 0.17, sparkDeltaZ);
        }
    }

    private static Vector3f minecartWheelPoint(float rotationX, float rotationY, float pointX, float pointY, float pointZ) {
        return new Vector3f((float) (pointX * Math.cos(rotationX) - pointZ * Math.sin(rotationX)), pointY * rotationY, (float) (pointZ * Math.cos(rotationX) + pointX * Math.sin(rotationX)));
    }

    public static void spawnFlintAndSteelSparkParticle(Level level, BlockPos particlePos) {
        if (!ConfigHandler.flintAndSteelSpark_onUse) return;
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
        if (!ConfigHandler.campfireSpark_enabled) return;
        double sparkIntensity = 5 / 12.;
        if (level.random.nextFloat() * 101 <= ConfigHandler.campfireSpark_spawnChance) {
            for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 3) + 1; i++) {
                SpawnParticlesUtil.spawnMostlyUpwardsMotionParticleOption(
                    level,
                    campfireState.is(Blocks.SOUL_CAMPFIRE) ? ModParticleTypes.FLOATING_SOUL_SPARK : ModParticleTypes.FLOATING_SPARK,
                    (double) particlePos.getX() + 0.5,
                    (double) particlePos.getY() + 0.5,
                    (double) particlePos.getZ() + 0.5,
                    sparkIntensity
                );
            }
        }
    }

    public static void spawnAmbientFireSparks(Level level, BlockState fireState, BlockPos particlePos, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (!ConfigHandler.fireSpark_enabled) return;
        double sparkIntensity = 5 / 12.;
        double width = Math.abs(minX - maxX);
        double height = Math.abs(minY - maxY);
        double depth = Math.abs(minZ - maxZ);
        if (level.random.nextFloat() * 101 <= ConfigHandler.fireSpark_spawnChance) {
            for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 3) + 1; i++) {
                SpawnParticlesUtil.spawnMostlyUpwardsMotionParticleOption(
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
        if (!ConfigHandler.fireCharge_onUse) return;
        double lavaIntensity = ConfigHandler.fireCharge_intensity / 24.;
        double smokeIntensity = ConfigHandler.fireCharge_intensity / 58.;
        for (int i = 0; i < ConfigHandler.maxFireCharge_onUse; i++) {
            double x = particlePos.getX() + 0.25 + (level.random.nextDouble() / 2);
            double y = particlePos.getY() + 0.25 + (level.random.nextDouble() / 2);
            double z = particlePos.getZ() + 0.25 + (level.random.nextDouble() / 2);
            if (level.random.nextFloat() > 0.2) {
                level.addParticle(level.random.nextFloat() > 0.3 ? ParticleTypes.SMOKE : ParticleTypes.LARGE_SMOKE, x, y, z, (level.random.nextDouble() - 0.5) * smokeIntensity, (level.random.nextDouble() + 0.5) * smokeIntensity, (level.random.nextDouble() - 0.5) * smokeIntensity);
                continue;
            }
            level.addParticle(ParticleTypes.LAVA, x, y, z, (level.random.nextDouble() - 0.5) * lavaIntensity, (level.random.nextDouble() + 0.5) * lavaIntensity, (level.random.nextDouble() - 0.5) * lavaIntensity);
        }
    }

    public static void spawnHoeTillParticle(Level level, BlockPos blockPos, UseOnContext context) {
        if (!ConfigHandler.hoeTill_onUse) return;
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
        if (!ConfigHandler.shovelFlatten_onUse) return;
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
        if (!ConfigHandler.axeStrip_onUse) return;
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
        } else if (particleOverride.isBlockStateParticle()) {
            particleOption = particleOverride.getBlockParticleOption(placedFluid.defaultFluidState().createLegacyBlock());
        } else {
            particleOption = particleOverride.getParticleOption();
        }

        if (particleOption == null) return;

        int maxParticles = FluidPlacementParticle.getParticleMultiplier(particleOverride, true);

        for (int i = 0; i < maxParticles; i++) {
            double x = particlePos.getX() + levelAccessor.getRandom().nextDouble();
            double y = particlePos.getY() + (levelAccessor.getRandom().nextDouble() / 1.5) + 0.6;
            double z = particlePos.getZ() + levelAccessor.getRandom().nextDouble();
            levelAccessor.addParticle(particleOption, x, y, z, 0.0, 0.21, 0.0);
        }
    }

    public static void spawnAnvilUseSparkParticles(ClientLevel level, BlockPos blockPos) {
        if (!ConfigHandler.anvilUseSparks_enabled) return;
        double x = blockPos.getX() + 0.5f;
        double y = blockPos.getY() + 1. + (level.random.nextDouble() / 16f);
        double z = blockPos.getZ() + 0.5f;
        ParticleEmitterOptions emitter = new ParticleEmitterOptions(
            ModParticleTypes.FLYING_SPARK_EMITTER,
            3,
            7,
            1,
            0.25f,
            0,
            0.25f
        );
        SpawnParticlesUtil.spawnParticleInCircle(
            emitter,
            level,
            new Vec3(x, y, z),
            ConfigHandler.maxAnvilUseSparks_onUse,
            0.32f,
            0.16f,
            2f,
            0.2f,
            2f
        );
    }

    public static void spawnGrindstoneUseSparkParticles(ClientLevel level, BlockPos blockPos) {
        if (!ConfigHandler.grindstoneUseSparks_enabled) return;
        BlockState grindstoneState = level.getBlockState(blockPos);
        if (!(grindstoneState.getBlock() instanceof GrindstoneBlock)) return;
        Direction facing = grindstoneState.getValue(GrindstoneBlock.FACING);
        AttachFace attachFace = grindstoneState.getValue(GrindstoneBlock.FACE);

        double x;
        double y;
        double z;
        if (attachFace == AttachFace.WALL) {
            x = blockPos.getX() + 0.5f + (facing.getStepX() / 1.9);
            y = blockPos.getY() + 0.5f;
            z = blockPos.getZ() + 0.5f + (facing.getStepZ() / 1.9);
        } else {
            x = blockPos.getX() + 0.5f;
            y = blockPos.getY() + (attachFace == AttachFace.CEILING ? 0 : 1.05f);
            z = blockPos.getZ() + 0.5f;
        }

        ParticleEmitterOptions emitter = getGrindstoneSparkEmitter(attachFace, facing);
        final float HORIZONTAL_MIN_SPEED = 0.05f;
        final float HORIZONTAL_MAX_SPEED = 0.3f;
        final float UPWARDS_SPEED = 0.5f;
        final float DOWNWARDS_SPEED = 0.1f;
        level.addParticle(
            emitter,
            x,
            y,
            z,
            facing.getStepX() * (attachFace == AttachFace.WALL ? HORIZONTAL_MIN_SPEED : HORIZONTAL_MAX_SPEED),
            attachFace == AttachFace.WALL ? UPWARDS_SPEED : 0,
            facing.getStepZ() * (attachFace == AttachFace.WALL ? HORIZONTAL_MIN_SPEED : HORIZONTAL_MAX_SPEED)
        );
        level.addParticle(
            emitter,
            x,
            y,
            z,
            facing.getStepX() * (attachFace == AttachFace.WALL ? -HORIZONTAL_MIN_SPEED : -HORIZONTAL_MAX_SPEED),
            attachFace == AttachFace.WALL ? -DOWNWARDS_SPEED : 0,
            facing.getStepZ() * (attachFace == AttachFace.WALL ? -HORIZONTAL_MIN_SPEED : -HORIZONTAL_MAX_SPEED)
        );
    }

    private static @NotNull ParticleEmitterOptions getGrindstoneSparkEmitter(AttachFace attachFace, Direction facing) {
        final float EMITTER_BOUND_WIDTH = 0.1f;
        final float EMITTER_BOUND_LENGTH = 0.8f;

        float width = attachFace == AttachFace.WALL ? 0 : EMITTER_BOUND_WIDTH;
        float height = attachFace == AttachFace.WALL ? EMITTER_BOUND_LENGTH : 0;
        float depth = attachFace == AttachFace.WALL ? 0 : EMITTER_BOUND_WIDTH;

        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            width = EMITTER_BOUND_WIDTH;
            depth = attachFace == AttachFace.WALL ? depth : EMITTER_BOUND_LENGTH;
        } else if (facing == Direction.EAST || facing == Direction.WEST) {
            width = attachFace == AttachFace.WALL ? width : EMITTER_BOUND_LENGTH;
            depth = EMITTER_BOUND_WIDTH;
        }

        return new ParticleEmitterOptions(
            ModParticleTypes.FLYING_SPARK_EMITTER,
            ConfigHandler.maxGrindstoneUseSparks_onUse < 6 ? ConfigHandler.maxGrindstoneUseSparks_onUse : 6,
            1,
            (int) Math.ceil((double) ConfigHandler.maxGrindstoneUseSparks_onUse / 6),
            width,
            height,
            depth
        );
    }

    public static void spawnBrushingParticles(ClientLevel level, BlockParticleOverride override, BlockState blockState, Direction brushDirection, Vec3 particlePos, int armDirection, int amountOfParticles, double baseDeltaX, double baseDeltaY, double baseDeltaZ) {
        final double outwardVelocity = 0.05;

        for (int i = 0; i < amountOfParticles; i++) {
            ParticleOptions particleOption;
            float velocityMultiplier;

            // use dust particles if brush particle behaviour is "block override + dust" and particle override is none or vanilla,
            // otherwise spawn block override particles
            if (
                ConfigHandler.brushParticleBehaviour == BrushParticleBehaviour.BLOCK_OVERRIDE_OR_VANILLA ||
                    (ConfigHandler.brushParticleBehaviour == BrushParticleBehaviour.BLOCK_OVERRIDE_OR_DUST && !(override == BlockParticleOverride.VANILLA || override == BlockParticleOverride.NONE))
            ) {
                particleOption = override.getParticleOptionForState(blockState, level, BlockPos.containing(particlePos), BlockParticleOverride.ORIGIN_BLOCK_BRUSHED);
                velocityMultiplier = override.getParticleVelocityMultiplier();
            } else {
                particleOption = TintedParticleOption.BRUSH_OPTION;
                velocityMultiplier = 0.1f;
            }

            if (particleOption == null) continue;

            level.addParticle(
                particleOption,
                particlePos.x + (brushDirection.getStepX() * 0.05),
                particlePos.y + (brushDirection.getStepY() * 0.05),
                particlePos.z + (brushDirection.getStepZ() * 0.05),
                (baseDeltaX * (double) armDirection * level.getRandom().nextDouble() * velocityMultiplier) + (brushDirection.getStepX() * outwardVelocity),
                (baseDeltaY + 1) * level.getRandom().nextDouble() * velocityMultiplier * brushDirection.getStepY(),
                (baseDeltaZ * (double) armDirection * level.getRandom().nextDouble() * velocityMultiplier) + (brushDirection.getStepZ() * outwardVelocity)
            );
        }
    }

    public static void spawnBlazeAmbientParticles(ClientLevel level, double x, double y, double z) {
        if (level.random.nextFloat() < (float) ConfigHandler.blaze_spawnChance / 100) {
            float xVel = MathHelpers.randomBetween(-0.2f, 0.2f);
            float yVel = MathHelpers.randomBetween(0.3f, 0.6f);
            float zVel = MathHelpers.randomBetween(-0.2f, 0.2f);
            level.addParticle(ModParticleTypes.FLOATING_SPARK, x, y, z, xVel, yVel, zVel);
        }
    }

    public static void spawnBlazeHurtParticles(ClientLevel level, double x, double y, double z) {
        if(!ConfigHandler.blaze_spawnOnHurt) return;
        for (int i = 0; i < level.random.nextIntBetweenInclusive(
            ConfigHandler.blaze_amountToSpawnOnHurt <= 1 ? 1 : ConfigHandler.blaze_amountToSpawnOnHurt - 1,
            ConfigHandler.blaze_amountToSpawnOnHurt + 2
        ); i++) {
            float xVel = (float) MathHelpers.clampOutside(MathHelpers.randomBetween(-0.5f, 0.5f), -0.2, 0.2);
            float yVel = MathHelpers.randomBetween(0.4f, 0.6f);
            float zVel = (float) MathHelpers.clampOutside(MathHelpers.randomBetween(-0.5f, 0.5f), -0.2, 0.2);
            level.addParticle(ModParticleTypes.FLYING_SPARK, x, y, z, xVel, yVel, zVel);
        }
    }
}
