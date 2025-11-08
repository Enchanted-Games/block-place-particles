package games.enchanted.eg_particle_interactions.common.particle_spawning;

import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import games.enchanted.eg_particle_interactions.common.config.categories.BlockInteractionOptions;
import games.enchanted.eg_particle_interactions.common.config.categories.EntityOptions;
import games.enchanted.eg_particle_interactions.common.config.categories.FluidAmbientOptions;
import games.enchanted.eg_particle_interactions.common.config.categories.ItemInteractionOptions;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import games.enchanted.eg_particle_interactions.common.particle.options.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.DripParticleOption;
import games.enchanted.eg_particle_interactions.common.particle.options.RandomDistributionEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.TintedParticleOption;
import games.enchanted.eg_particle_interactions.common.particle.overrides.BlockParticleOverride;
import games.enchanted.eg_particle_interactions.common.particle.overrides.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.particle.overrides.FluidPlacementParticle;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import games.enchanted.eg_particle_interactions.common.registry.TagUtil;
import games.enchanted.eg_particle_interactions.common.util.FluidHelpers;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class SpawnParticles {
    public static void spawnBlockPlaceParticle(ClientLevel level, BlockPos blockPos) {
        BlockState placedBlockState = level.getBlockState(blockPos);
        spawnBlockPlaceParticle(level, blockPos, placedBlockState);
    }

    public static void spawnBlockPlaceParticle(ClientLevel level, BlockPos blockPos, BlockState placedBlockState) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.BLOCK_PLACE_OR_BREAK, blockPos)) return;
        if (BlockInteractionOptions.UNDERWATER_BUBBLES_ON_PLACE_ENABLED.getValue()) spawnUnderwaterBubbles(BlockInteractionOptions.UNDERWATER_BUBBLES_MAX_ON_PLACE.getValue(), level, blockPos);

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
            if(blockShape.isEmpty()) return;
            Vec3 blockCenter = blockShape.bounds().getCenter();
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
                if (amountOfParticlesAlongEdge < 1)
                    amountOfParticlesAlongEdge = 1; // always try to spawn at least 1 particle per edge

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
                        (particleXOffset - blockCenter.x()) * particleOutwardVelocityAdjustment,
                        (particleYOffset - blockCenter.y()) * particleOutwardVelocityAdjustment,
                        (particleZOffset - blockCenter.z()) * particleOutwardVelocityAdjustment
                    );
                }
            });
        }
    }

    public static void spawnBlockBreakParticle(ClientLevel level, BlockState brokenBlockState, BlockPos brokenBlockPos, BlockParticleOverride particleOverride) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.BLOCK_PLACE_OR_BREAK, brokenBlockPos)) return;
        if (BlockInteractionOptions.UNDERWATER_BUBBLES_ON_BREAK_ENABLED.getValue()) spawnUnderwaterBubbles(BlockInteractionOptions.UNDERWATER_BUBBLES_MAX_ON_BREAK.getValue(), level, brokenBlockPos);

        if (particleOverride == BlockParticleOverride.NONE) {
            return;
        }

        int maxParticlesPerLength = BlockParticleOverride.getParticleMultiplierForOverride(particleOverride, false);
        if (maxParticlesPerLength <= 0) return;

        double particleOutwardVelocityAdjustment = particleOverride.getParticleVelocityMultiplier();

        if (!brokenBlockState.isAir() && brokenBlockState.shouldSpawnTerrainParticles()) {
            VoxelShape blockShape = brokenBlockState.getShape(level, brokenBlockPos);
            if(blockShape.isEmpty()) return;
            Vec3 blockCenter = blockShape.bounds().getCenter();
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
                                (particleXOffset - blockCenter.x()) * particleOutwardVelocityAdjustment,
                                (particleYOffset - blockCenter.y()) * particleOutwardVelocityAdjustment,
                                (particleZOffset - blockCenter.z()) * particleOutwardVelocityAdjustment
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
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, x, y, z)) return;
        if (!BlockInteractionOptions.BLOCK_FALLING_EFFECT_ENABLED.getValue()) return;
        if (blockState.isAir()) return;

        int overrideOrigin = BlockParticleOverride.ORIGIN_FALLING_BLOCK_FALLING;
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(blockState, overrideOrigin);

        if (particleOverride == BlockParticleOverride.NONE || particleOverride == BlockParticleOverride.VANILLA) return;

        for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 4); i++) {
            ParticleOptions particleOptions = particleOverride.getParticleOptionForState(blockState, level, BlockPos.containing(x, y, z), overrideOrigin);
            if (particleOptions == null) continue;
            level.addParticle(
                particleOptions,
                x - 0.5 + level.random.nextFloat(),
                y + level.random.nextFloat(),
                z - 0.5 + level.random.nextFloat(),
                (deltaMovement.x * 3) * -particleOverride.getParticleVelocityMultiplier(),
                (deltaMovement.y * 3) * -particleOverride.getParticleVelocityMultiplier(),
                (deltaMovement.z * 3) * -particleOverride.getParticleVelocityMultiplier()
            );
        }
    }

    public static void spawnFallingBlockLandParticles(ClientLevel level, BlockState blockState, double x, double y, double z, Vec3 deltaMovement) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, x, y, z)) return;
        if (!BlockInteractionOptions.BLOCK_FALLING_EFFECT_ENABLED.getValue()) return;

        int overrideOrigin = BlockParticleOverride.ORIGIN_FALLING_BLOCK_LANDED;
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(blockState, overrideOrigin);

        if (particleOverride == BlockParticleOverride.NONE) return;

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
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, minecartX, minecartY, minecartZ)) return;
        if (!EntityOptions.MINECART_SPARKS_ENABLED.getValue()) return;
        if (!isOnRails) return;
        if (!(hasBlock || hasPassenger) && EntityOptions.MINECART_SPARKS_ONLY_WITH_PASSENGER.getValue()) return;

        double speed = MathHelpers.maxVec3(deltaMovement.toVector3f(), true);
        if (speed < 0.05) return;

        float sparksChancePerWheel = (float) (Math.clamp(speed, 0, maxSpeed) / maxSpeed) - 0.75f;
        sparksChancePerWheel *= EntityOptions.MINECART_SPARKS_SPAWN_CHANCE.getValue() / 50f;

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
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, particlePos)) return;
        if (!ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_ENABLED.getValue()) return;
        BlockState fireOrLitBlock = level.getBlockState(particlePos);
        boolean isSoulBlock = level.getBlockState(particlePos.below()).is(BlockTags.SOUL_FIRE_BASE_BLOCKS) || fireOrLitBlock.is(Blocks.SOUL_CAMPFIRE);
        double sparkIntensity = ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_INTENSITY.getValue() / 12.;
        for (int i = 0; i < ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_AMOUNT.getValue(); i++) {
            double x = particlePos.getX() + 0.25 + (level.random.nextDouble() / 2);
            double y = particlePos.getY() + 0.25 + (level.random.nextDouble() / 2);
            double z = particlePos.getZ() + 0.25 + (level.random.nextDouble() / 2);
            level.addParticle(isSoulBlock ? ModParticleTypes.FLYING_SOUL_SPARK : ModParticleTypes.FLYING_SPARK, x, y, z, (level.random.nextDouble() - 0.5) * sparkIntensity, (level.random.nextDouble() + 0.5) * sparkIntensity, (level.random.nextDouble() - 0.5) * sparkIntensity);
        }
    }

    public static void spawnAmbientCampfireSparks(Level level, BlockPos particlePos, BlockState campfireState) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, particlePos)) return;
        if (BlockInteractionOptions.CAMPFIRE_SPARK_ENABLED.getValue()) {
            double sparkIntensity = 5 / 12.;
            if (level.random.nextFloat() * 101 <= BlockInteractionOptions.CAMPFIRE_SPARK_SPAWN_CHANCE.getValue()) {
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
        if (BlockInteractionOptions.CAMPFIRE_EMBER_ENABLED.getValue()) {
            if (level.random.nextFloat() * 101 <= BlockInteractionOptions.CAMPFIRE_EMBER_SPAWN_CHANCE.getValue()) {
                for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 4); i++) {
                    level.addParticle(
                        campfireState.is(Blocks.SOUL_CAMPFIRE) ? ModParticleTypes.FLOATING_SOUL_EMBER : ModParticleTypes.FLOATING_EMBER,
                        (double) particlePos.getX() + (level.random.nextFloat() * 0.75) + 0.125f,
                        (double) particlePos.getY() + (level.random.nextFloat() * 0.75) + 0.125f,
                        (double) particlePos.getZ() + (level.random.nextFloat() * 0.75) + 0.125f,
                        0,
                        0,
                        0
                    );
                }
            }
        }
    }

    public static void spawnAmbientFireSparks(Level level, BlockState fireState, BlockPos particlePos, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, particlePos)) return;
        double width = Math.abs(minX - maxX);
        double height = Math.abs(minY - maxY);
        double depth = Math.abs(minZ - maxZ);
        if (BlockInteractionOptions.FIRE_SPARK_ENABLED.getValue()) {
            double sparkIntensity = 5 / 12.;
            if (level.random.nextFloat() * 101 <= BlockInteractionOptions.FIRE_SPARK_SPAWN_CHANCE.getValue()) {
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
        if (BlockInteractionOptions.FIRE_EMBER_ENABLED.getValue()) {
            if (level.random.nextFloat() * 101 <= BlockInteractionOptions.FIRE_EMBER_SPAWN_CHANCE.getValue()) {
                for (int i = 0; i < level.random.nextIntBetweenInclusive(1, 4); i++) {
                    level.addParticle(
                        fireState.is(Blocks.SOUL_FIRE) ? ModParticleTypes.FLOATING_SOUL_EMBER : ModParticleTypes.FLOATING_EMBER,
                        particlePos.getX() + minX + (level.random.nextFloat() * width),
                        particlePos.getY() + minY + (level.random.nextFloat() * height),
                        particlePos.getZ() + minZ + (level.random.nextFloat() * depth),
                        0,
                        0,
                        0
                    );
                }
            }
        }
    }

    public static void spawnFireChargeSmokeParticle(Level level, BlockPos particlePos) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, particlePos)) return;
        if (!ItemInteractionOptions.FIRE_CHARGE_PARTICLES_ENABLED.getValue()) return;
        double lavaIntensity = ItemInteractionOptions.FIRE_CHARGE_PARTICLES_INTENSITY.getValue() / 24.;
        double smokeIntensity = ItemInteractionOptions.FIRE_CHARGE_PARTICLES_INTENSITY.getValue() / 58.;
        for (int i = 0; i < ItemInteractionOptions.FIRE_CHARGE_PARTICLES_AMOUNT.getValue(); i++) {
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
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!ItemInteractionOptions.HOE_TILL_ENABLED.getValue()) return;
        Vec3 clickedPosition = context.getClickLocation();
        Direction clickDirection = context.getClickedFace();
        for (int i = 0; i < ItemInteractionOptions.HOE_TILL_AMOUNT.getValue(); i++) {
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
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!ItemInteractionOptions.SHOVEL_FLATTEN_ENABLED.getValue()) return;
        Vec3 clickedPosition = context.getClickLocation();
        Direction clickDirection = context.getClickedFace();
        for (int i = 0; i < ItemInteractionOptions.SHOVEL_FLATTEN_AMOUNT.getValue(); i++) {
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
        // TODO: reimplement this
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!ItemInteractionOptions.AXE_STRIP_ENABLED.getValue()) return;
        Vec3 clickedPosition = context.getClickLocation();
        Direction clickDirection = context.getClickedFace();
        for (int i = 0; i < ItemInteractionOptions.AXE_STRIP_AMOUNT.getValue(); i++) {
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
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.BLOCK_PLACE_OR_BREAK, particlePos)) return;
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
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!BlockInteractionOptions.ANVIL_USE_SPARKS_ENABLED.getValue()) return;
        double x = blockPos.getX() + 0.5f;
        double y = blockPos.getY() + 1. + (level.random.nextDouble() / 16f);
        double z = blockPos.getZ() + 0.5f;
        RandomDistributionEmitterOptions emitter = new RandomDistributionEmitterOptions(
            ModParticleTypes.FLYING_SPARK_EMITTER,
            3,
            7,
            1,
            new Vector3f(0.25f, 0, 0.25f)
        );
        SpawnParticlesUtil.spawnParticleInCircle(
            emitter,
            level,
            new Vec3(x, y, z),
            BlockInteractionOptions.ANVIL_USE_SPARKS_MAX_ON_USE.getValue(),
            0.32f,
            0.16f,
            2f,
            0.2f,
            2f
        );
    }

    public static void spawnGrindstoneUseSparkParticles(ClientLevel level, BlockPos blockPos) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!BlockInteractionOptions.GRINDSTONE_USE_SPARKS_ENABLED.getValue()) return;
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

        RandomDistributionEmitterOptions emitter = getGrindstoneSparkEmitter(attachFace, facing);
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

    private static @NotNull RandomDistributionEmitterOptions getGrindstoneSparkEmitter(AttachFace attachFace, Direction facing) {
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

        int amount = BlockInteractionOptions.GRINDSTONE_USE_SPARKS_MAX_ON_USE.getValue();
        return new RandomDistributionEmitterOptions(
            ModParticleTypes.FLYING_SPARK_EMITTER,
            amount < 6 ? amount : 6,
            1,
            (int) Math.ceil((double) amount / 6),
            new Vector3f(width, height, depth)
        );
    }

    public static void spawnBrushingParticles(ClientLevel level, BlockParticleOverride override, BlockState blockState, Direction brushDirection, Vec3 particlePos, int armDirection, int amountOfParticles, double baseDeltaX, double baseDeltaY, double baseDeltaZ) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, particlePos.x(), particlePos.y(), particlePos.z())) return;
        final double outwardVelocity = 0.05;

        for (int i = 0; i < amountOfParticles; i++) {
            ParticleOptions particleOption;
            float velocityMultiplier;

            // use dust particles if brush particle behaviour is "block override + dust" and particle override is none or vanilla,
            // otherwise spawn block override particles
            if (
                ItemInteractionOptions.BRUSH_PARTICLE_BEHAVIOUR.getValue() == BrushParticleBehaviour.BLOCK_OVERRIDE_OR_VANILLA ||
                (ItemInteractionOptions.BRUSH_PARTICLE_BEHAVIOUR.getValue() == BrushParticleBehaviour.BLOCK_OVERRIDE_OR_DUST && !(override == BlockParticleOverride.VANILLA || override == BlockParticleOverride.NONE))
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
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, x, y, z)) return;
        if (level.random.nextFloat() < (float) EntityOptions.BLAZE_SPARKS_SPAWN_CHANCE.getValue() / 100) {
            float xVel = MathHelpers.randomBetween(-0.2f, 0.2f);
            float yVel = MathHelpers.randomBetween(0.3f, 0.6f);
            float zVel = MathHelpers.randomBetween(-0.2f, 0.2f);
            level.addParticle(ModParticleTypes.FLOATING_SPARK, x, y, z, xVel, yVel, zVel);
        }
    }

    public static void spawnBlazeHurtParticles(ClientLevel level, double x, double y, double z) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, x, y, z)) return;
        if (!EntityOptions.BLAZE_SPARKS_SPAWN_ON_HURT.getValue()) return;
        int amount = EntityOptions.BLAZE_SPARKS_AMOUNT_ON_HURT.getValue();
        for (int i = 0; i < level.random.nextIntBetweenInclusive(
            amount <= 1 ? 1 : amount - 1,
            amount + 2
        ); i++) {
            float xVel = (float) MathHelpers.clampOutside(MathHelpers.randomBetween(-0.5f, 0.5f), -0.2, 0.2);
            float yVel = MathHelpers.randomBetween(0.4f, 0.6f);
            float zVel = (float) MathHelpers.clampOutside(MathHelpers.randomBetween(-0.5f, 0.5f), -0.2, 0.2);
            level.addParticle(ModParticleTypes.FLYING_SPARK, x, y, z, xVel, yVel, zVel);
        }
    }

    public static void spawnRedstoneInteractionParticles(ClientLevel level, BlockState blockState, double interactionX, double interactionY, double interactionZ, float spreadX, float spreadY, float spreadZ) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, interactionX, interactionY, interactionZ)) return;
        if(!BlockInteractionOptions.REDSTONE_INTERACTION_DUST_ENABLED.getValue()) return;
        BlockPos pos = BlockPos.containing(interactionX, interactionY, interactionZ);
        for (int i = 0; i < BlockInteractionOptions.REDSTONE_INTERACTION_DUST_AMOUNT.getValue(); i++) {
            double particleX = interactionX + MathHelpers.randomBetween(-spreadX / 2, spreadX / 2);
            double particleY = interactionY + MathHelpers.randomBetween(-spreadY / 2, spreadY / 2);
            double particleZ = interactionZ + MathHelpers.randomBetween(-spreadZ / 2, spreadZ / 2);
            ParticleOptions particleOptions = BlockParticleOverrides.REDSTONE_DUST.getParticleOptionForState(blockState, level, pos, BlockParticleOverride.ORIGIN_BLOCK_INTERACTED_WITH);
            if (particleOptions == null) continue;
            level.addParticle(
                particleOptions,
                particleX,
                particleY,
                particleZ,
                MathHelpers.randomBetween(-0.05f, 0.05f),
                0.2f,
                MathHelpers.randomBetween(-0.05f, 0.05f)
            );
        }
    }

    public static void spawnLavaBubblePopParticles(ClientLevel level, BlockPos fluidPos, FluidState fluidState) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, fluidPos)) return;
        if (!FluidAmbientOptions.LAVA_BUBBLE_POP_ENABLED.getValue()) return;
        if (level.random.nextFloat() < (float) FluidAmbientOptions.LAVA_BUBBLE_POP_SPAWN_CHANCE.getValue() / 2500) {
            double d0 = (double) fluidPos.getX() + level.random.nextDouble();
            double d1 = (double) fluidPos.getY() + fluidState.getOwnHeight();
            double d2 = (double) fluidPos.getZ() + level.random.nextDouble();
            level.addParticle(ModParticleTypes.LAVA_POP, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        }
    }

    public static void spawnRandomUnderwaterBubbleStreams(ClientLevel level, BlockPos blockPos, BlockState blockState) {
        if (!FluidAmbientOptions.UNDERWATER_BUBBLE_STREAM_ENABLED.getValue()) return;
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, blockPos)) return;

        ResourceLocation blockLocation = RegistryHelpers.getLocationFromBlock(blockState.getBlock());
        if(!TagUtil.doesListContainBlock(FluidAmbientOptions.UNDERWATER_BUBBLE_STREAM_BLOCKS.getValue(), blockLocation)) return;

        if (!FluidHelpers.probablyPlacedUnderwater(level, blockPos)) return;

        if (level.random.nextFloat() < (float) FluidAmbientOptions.UNDERWATER_BUBBLE_STREAM_SPAWN_CHANCE.getValue() / 2500) {
            double x = (double) blockPos.getX() + level.random.nextDouble();
            double y = (double) blockPos.getY() + (blockState.isSolid() ? 1.05 : level.random.nextDouble());
            double z = (double) blockPos.getZ() + level.random.nextDouble();
            RandomDistributionEmitterOptions emitter = new RandomDistributionEmitterOptions(
                ModParticleTypes.UNDERWATER_RISING_BUBBLE_SMALL_EMITTER,
                MathHelpers.randomBetween(9, 30),
                MathHelpers.randomBetween(2, 4),
                1
            );
            level.addParticle(emitter, x, y, z, 0.0f, 0.0f, 0.0f);
        }
    }

    public static void spawnBlockDisturbanceParticles(ClientLevel level, BlockPos blockPos, BlockState blockState, double entityX, double entityY, double entityZ, Vec3 deltaMovement, boolean isSprinting) {
        if(!BlockInteractionOptions.BLOCK_RUSTLE_ENABLED.getValue()) return;
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        double speed = deltaMovement.length();
        if(speed <= 0.1 && !isSprinting) return;

        ResourceLocation blockLocation = RegistryHelpers.getLocationFromBlock(blockState.getBlock());
        if(!TagUtil.doesListContainBlock(BlockInteractionOptions.BLOCK_RUSTLE_BLOCKS.getValue(), blockLocation)) return;

        int overrideOrigin = BlockParticleOverride.ORIGIN_BLOCK_WALKED_THROUGH;

        int particlesAmount =  (speed > 0.25 || isSprinting ? 3 : 1);

        for (int i = 0; i < particlesAmount; i++) {
            double particleX = entityX + ((level.random.nextFloat() * 0.5) - 0.25);
            double particleY = entityY + 0.35 + ((level.random.nextFloat() * 0.5) - 0.25);
            double particleZ = entityZ + ((level.random.nextFloat() * 0.5) - 0.25);

            // skip spawning if the particle is out of the block bounds
            BlockPos entityBlockPos = BlockPos.containing(particleX, blockPos.getY(), particleZ);
            if(level.getBlockState(entityBlockPos).isAir()) continue;

            BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(blockState, overrideOrigin);
            if (particleOverride == BlockParticleOverride.NONE) continue;

            ParticleOptions particleToSpawn = particleOverride.getParticleOptionForState(blockState, level, blockPos, overrideOrigin);
            if(particleToSpawn == null) continue;

            level.addParticle(
                particleToSpawn,
                particleX,
                particleY,
                particleZ,
                deltaMovement.x * 3 * particleOverride.getParticleVelocityMultiplier(),
                Math.min(deltaMovement.y, 0.1) * 3 * particleOverride.getParticleVelocityMultiplier() + 0.1,
                deltaMovement.z * 3 * particleOverride.getParticleVelocityMultiplier()
            );
        }
    }

    public static void spawnItemFrameInteractionParticles(ClientLevel level, double x, double y, double z, AABB boundingBox, Direction itemFrameDirection, ItemFrameParticleOrigin particleOrigin, boolean glowingItemFrame) {
        if(!EntityOptions.ITEM_FRAME_INTERACTION_ENABLED.getValue()) return;
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, x, y, z)) return;

        double particleSpeed = 0.2;

        ParticleOptions particleOptionToSpawn;
        if(particleOrigin == ItemFrameParticleOrigin.FRAME_KILLED) {
            return;
        } else {
            particleOptionToSpawn = glowingItemFrame ? TintedParticleOption.GLOW_ITEM_FRAME_DUST_OPTION : TintedParticleOption.ITEM_FRAME_DUST_OPTION;
        }

        for (int i = 0; i < EntityOptions.ITEM_FRAME_INTERACTION_AMOUNT.getValue(); i++) {
            double randomX = boundingBox.minX + (boundingBox.getXsize() * level.random.nextDouble());
            double randomY = boundingBox.minY + (boundingBox.getYsize() * level.random.nextDouble());
            double randomZ = boundingBox.minZ + (boundingBox.getZsize() * level.random.nextDouble());

            level.addParticle(
                particleOptionToSpawn,
                (itemFrameDirection.getStepX() * 0.15) + x,
                (itemFrameDirection.getStepY() * 0.15) + y,
                (itemFrameDirection.getStepZ() * 0.15) + z,
                (itemFrameDirection.getStepX() * 0.03) + (randomX - x) * 2 * particleSpeed,
                (itemFrameDirection.getStepY() * 0.03) + (randomY - y) * 2 * particleSpeed,
                (itemFrameDirection.getStepZ() * 0.03) + (randomZ - z) * 2 * particleSpeed
            );
        }
    }

    public enum ItemFrameParticleOrigin {
        FRAME_KILLED(),
        HELD_ITEM_REMOVED(),
        ITEM_ROTATED(),
        ITEM_PLACED(),
    }

    public static void spawnSmokerSmokeParticles(ClientLevel level, BlockPos blockPos) {
        if(!BlockInteractionOptions.SMOKER_SMOKE_ENABLED.getValue()) return;
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, blockPos)) return;

        if(level.random.nextFloat() > 0.3) {
            Vec3 centerPos = blockPos.getCenter();
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, centerPos.x, blockPos.getY() + .8, centerPos.z, 0, 0.07f, 0);
        }
    }

    public static void spawnAdditionalFurnaceParticles(ClientLevel level, BlockPos blockPos, BlockState furnaceState) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, blockPos)) return;
        if(!BlockInteractionOptions.FURNACE_EMBERS_ENABLED.getValue()) return;

        double[] positions = ParticlePositionHelpers.getRandomFurnaceParticlePosition(blockPos, furnaceState);
        if( level.getBlockState(BlockPos.containing(positions[0], positions[1], positions[2])).isSuffocating(level, blockPos) ) return;

        Direction furnaceDirection = furnaceState.getValue(FurnaceBlock.FACING);
        final boolean spawnSpark = level.random.nextFloat() < 0.7;
        final float outwardVelocity = MathHelpers.randomBetween(0.01f, 0.03f) * (spawnSpark ? 1 : 5);
        level.addParticle(spawnSpark ? ModParticleTypes.FLOATING_EMBER : ModParticleTypes.FLOATING_SPARK, positions[0], positions[1], positions[2], furnaceDirection.getStepX() * outwardVelocity, 0.05f, furnaceDirection.getStepZ() * outwardVelocity);
    }

    public static void spawnAdditionalBlastFurnaceParticles(ClientLevel level, BlockPos blockPos, BlockState furnaceState) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, blockPos)) return;
        if(!BlockInteractionOptions.BLAST_FURNACE_SPARKS_ENABLED.getValue()) return;

        double[] positions = ParticlePositionHelpers.getRandomFurnaceParticlePosition(blockPos, furnaceState);
        if( level.getBlockState(BlockPos.containing(positions[0], positions[1], positions[2])).isSuffocating(level, blockPos) ) return;

        Direction furnaceDirection = furnaceState.getValue(FurnaceBlock.FACING);
        final boolean spawnSpark = level.random.nextFloat() < 0.2;
        final float outwardVelocity = MathHelpers.randomBetween(0.01f, 0.03f) * (spawnSpark ? 1 : 5);
        level.addParticle(spawnSpark ? ModParticleTypes.FLOATING_EMBER : ModParticleTypes.FLOATING_SPARK, positions[0], positions[1] + 0.125, positions[2], furnaceDirection.getStepX() * outwardVelocity, 0.05f, furnaceDirection.getStepZ() * outwardVelocity);
    }

    public static void spawnLightningImpactSparks(ClientLevel level, double x, double y, double z) {
        if(SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, x, y, z)) return;
        if(!EntityOptions.LIGHTNING_STRIKE_ENABLED.getValue()) return;

        SpawnParticlesUtil.spawnParticleInCircle(
            () -> new ArcEmitterOptions(
                ModParticleTypes.ARC_EMITTER,
                MathHelpers.randomBetween(7, 14),
                MathHelpers.randomBetween(3, 5),
                40,
                MathHelpers.randomBetween(4, 6),
                ArcEmitterOptions.TICK_INTERVAL_DEFAULT,
                MathHelpers.randomBetween(160, 380),
                null
            ),
            level,
            new Vec3(x, y + 0.5, z),
            EntityOptions.LIGHTNING_STRIKE_AMOUNT_OF_ARCS.getValue(),
            0.2f,
            0.8f,
            3f,
            0.3f,
            1.0f
        );

        int amountOfSparks = EntityOptions.LIGHTNING_STRIKE_AMOUNT_OF_SPARKS.getValue();
        SpawnParticlesUtil.spawnParticleInCircle(
            ModParticleTypes.FLYING_SPARK,
            level,
            new Vec3(x, y + 0.01, z),
            MathHelpers.randomBetween(Math.max(0, amountOfSparks - 4), amountOfSparks),
            0.3f,
            0.8f,
            0.25f,
            0.25f,
            1.1f
        );
    }

    public static void spawnHoneyCollectionParticles(ClientLevel level, double x, double y, double z, Direction faceDirection) {
        if(!ItemInteractionOptions.HONEY_COLLECTION_ENABLED.getValue()) return;
        int amount = ItemInteractionOptions.HONEY_COLLECTION_AMOUNT.getValue();
        for (int i = 0; i < level.random.nextIntBetweenInclusive(Math.max(amount - 2, 0), Math.max(amount, 1)); i++) {
            double xOffset = (level.random.nextDouble() - 0.5) * 0.5 * (1 + Math.abs(faceDirection.getStepX()));
            double yOffset = (level.random.nextDouble() - 0.5) * 0.5 * (1 + Math.abs(faceDirection.getStepY()));
            double zOffset = (level.random.nextDouble() - 0.5) * 0.5 * (1 + Math.abs(faceDirection.getStepZ()));
            level.addParticle(
                DripParticleOption.FALLING_HONEY_DROP,
                x + xOffset,
                y + yOffset,
                z + zOffset,
                0,
                0,
                0
            );
        }
    }
    public static void spawnHoneyCollectionParticlesOnPlayer(ClientLevel level, Player player) {
        if(!ItemInteractionOptions.HONEY_COLLECTION_ENABLED.getValue()) return;
        int amount = ItemInteractionOptions.HONEY_COLLECTION_AMOUNT.getValue();
        for (int i = 0; i < level.random.nextIntBetweenInclusive(Math.max(amount / 2, 0), Math.max(amount / 2, 1)); i++) {
            level.addParticle(
                DripParticleOption.FALLING_HONEY_DROP,
                player.getX() - 0.25 + (level.random.nextDouble() / 2),
                player.getY() + 0.85 + (level.random.nextDouble() / 5),
                player.getZ() - 0.25 + (level.random.nextDouble() / 2),
                0,
                0,
                0
            );
        }
    }
}