package games.enchanted.eg_particle_interactions.common.particle_spawning;

import games.enchanted.eg_particle_interactions.common.config.categories.BlockInteractionOptions;
import games.enchanted.eg_particle_interactions.common.config.categories.EntityOptions;
import games.enchanted.eg_particle_interactions.common.config.categories.FluidInteractionOptions;
import games.enchanted.eg_particle_interactions.common.config.categories.ItemInteractionOptions;
import games.enchanted.eg_particle_interactions.common.override_system.OverridePreset;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.FluidOverrideManager;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleIDs;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle.types.options.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.options.RandomDistributionEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.ParticleSpawner;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import games.enchanted.eg_particle_interactions.common.util.BiomeHelpers;
import games.enchanted.eg_particle_interactions.common.util.FluidHelpers;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class SpawnParticles {
    public static void spawnBlockPlaceParticle(ClientLevel level, BlockPos blockPos, BlockState placedBlockState) {
        int maxParticlesPerEdge = BlockInteractionOptions.BLOCK_MAX_ON_PLACE.getValue();

        if (maxParticlesPerEdge <= 0) return;
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.BLOCK_PLACE_OR_BREAK, blockPos)) return;

        ParticleContext context = ParticleContext.block(level, placedBlockState, blockPos);

        int underwaterBubbleAmount = BlockInteractionOptions.UNDERWATER_BUBBLES_MAX_ON_PLACE.getValue();
        if (underwaterBubbleAmount > 0) spawnUnderwaterBubbles(underwaterBubbleAmount, level, blockPos, context, EmitterRuleSetIds.BLOCK_PLACED_UNDERWATER.get());

        ParticleOrigin origin = ParticleOrigin.BLOCK_PLACED;
        OverridePreset override = BlockOverrideManager.getForBlock(placedBlockState, origin);

        if (!placedBlockState.isAir() && placedBlockState.shouldSpawnTerrainParticles()) {
            VoxelShape blockShape = placedBlockState.getShape(level, blockPos);
            if (blockShape.isEmpty()) return;
            Vec3 blockCenter = blockShape.bounds().getCenter();
            //noinspection deprecation
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

                    override.getRandom().spawnParticle(
                        origin,
                        context,
                        (double) blockPos.getX() + MathHelper.expandWhenOutOfBound(particleXOffset, 0, 1),
                        (double) blockPos.getY() + MathHelper.expandWhenOutOfBound(particleYOffset, 0, 1),
                        (double) blockPos.getZ() + MathHelper.expandWhenOutOfBound(particleZOffset, 0, 1),
                        (particleXOffset - blockCenter.x()),
                        (particleYOffset - blockCenter.y()),
                        (particleZOffset - blockCenter.z())
                    );
                }
            });
        }
    }

    public static void spawnBlockBreakParticle(ClientLevel level, BlockState brokenBlockState, BlockPos blockPos) {
        int maxParticlesPerLength = BlockInteractionOptions.BLOCK_MAX_ON_BREAK.getValue();

        if (maxParticlesPerLength <= 0) return;
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.BLOCK_PLACE_OR_BREAK, blockPos)) return;

        ParticleContext context = ParticleContext.block(level, brokenBlockState, blockPos);

        int underwaterBubbleAmount = BlockInteractionOptions.UNDERWATER_BUBBLES_MAX_ON_BREAK.getValue();
        if (underwaterBubbleAmount > 0) spawnUnderwaterBubbles(underwaterBubbleAmount, level, blockPos, context, EmitterRuleSetIds.BLOCK_BROKEN_UNDERWATER.get());

        ParticleOrigin origin = ParticleOrigin.BLOCK_BROKEN;
        OverridePreset override = BlockOverrideManager.getForBlock(brokenBlockState, origin);

        if (!brokenBlockState.isAir() && brokenBlockState.shouldSpawnTerrainParticles()) {
            VoxelShape blockShape = brokenBlockState.getShape(level, blockPos);
            if (blockShape.isEmpty()) return;
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

                            override.getRandom().spawnParticle(
                                origin,
                                context,
                                blockPos.getX() + (particleXOffset * width + x1),
                                blockPos.getY() + (particleYOffset * height + y1),
                                blockPos.getZ() + (particleZOffset * depth + z1),
                                (particleXOffset - blockCenter.x()),
                                (particleYOffset - blockCenter.y()),
                                (particleZOffset - blockCenter.z())
                            );
                        }
                    }
                }
            });
        }
    }

    private static void spawnUnderwaterBubbles(int amountOfBubbles, ClientLevel level, BlockPos blockPos, ParticleContext context, EmitterRuleSet emitterRuleSet) {
        if (!FluidHelpers.probablyPlacedUnderwater(level, blockPos)) return;
        var random = level.getRandom();
        for (int i = 0; i < Math.max(amountOfBubbles + random.nextIntBetweenInclusive(-2, 0), 1); i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            double z = random.nextDouble();
            boolean blockAboveIsWater = level.getFluidState(blockPos.above()).is(FluidTags.WATER);
            double verticalVelocity = (y - 0.5) * (blockAboveIsWater ? 0.2 : 0);
            double horizontalVelocityMul = !blockAboveIsWater ? 0.35 : 0.25;
            emitterRuleSet.getEmitter(context).spawnParticle(
                context,
                blockPos.getX() + x,
                blockPos.getY() + y,
                blockPos.getZ() + z,
                (x - 0.5) * horizontalVelocityMul,
                level.getBlockState(blockPos.below()).isSolid() ? Math.abs(verticalVelocity) + 0.1 : verticalVelocity,
                (z - 0.5) * horizontalVelocityMul
            );
        }
    }

    public static void spawnFallingBlockRandomFallParticles(ClientLevel level, BlockState blockState, double x, double y, double z, Vec3 deltaMovement) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, x, y, z)) return;
        if (!BlockInteractionOptions.BLOCK_FALLING_EFFECT_ENABLED.getValue()) return;
        if (blockState.isAir()) return;

        ParticleOrigin origin = ParticleOrigin.FALLING_BLOCK_FALLING;
        OverridePreset override = BlockOverrideManager.getForBlock(blockState, origin);
        ParticleContext context = ParticleContext.block(level, blockState, BlockPos.containing(x, y, z));

        var random = level.getRandom();
        for (int i = 0; i < random.nextIntBetweenInclusive(1, 4); i++) {
            override.getRandom().spawnParticle(
                origin,
                context,
                x - 0.5 + random.nextFloat(),
                y + random.nextFloat(),
                z - 0.5 + random.nextFloat(),
                deltaMovement.x * -3,
                deltaMovement.y * -3,
                deltaMovement.z * -3
            );
        }
    }

    public static void spawnFallingBlockLandParticles(ClientLevel level, BlockState blockState, double x, double y, double z, Vec3 deltaMovement) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, x, y, z)) return;
        if (!BlockInteractionOptions.BLOCK_FALLING_EFFECT_ENABLED.getValue()) return;

        ParticleOrigin origin = ParticleOrigin.FALLING_BLOCK_LANDED;
        OverridePreset override = BlockOverrideManager.getForBlock(blockState, origin);

        BlockPos blockPos = BlockPos.containing(x, y, z);
        double movementSpeed = deltaMovement.length();

        double particleY = Math.round((y + (deltaMovement.y / 2)) - 0.1) + 0.0625;

        ParticleContext context = ParticleContext.block(level, blockState, blockPos);

        SpawnParticlesUtil.spawnParticleInCircle(
            (x1, y1, z1, xSpeed, ySpeed, zSpeed) -> override.getRandom().spawnParticle(origin, context, x1, y1, z1, xSpeed, ySpeed, zSpeed),
            new Vec3(x, particleY, z),
            16,
            0.4f,
            0.9f,
            1.7f * (float) (movementSpeed * 2) * 0.1f,
            0.035f,
            0
        );

        SpawnParticlesUtil.spawnParticleInCircle(
            (x1, y1, z1, xSpeed, ySpeed, zSpeed) -> override.getRandom().spawnParticle(origin, context, x1, y1, z1, xSpeed, ySpeed, zSpeed),
            new Vec3(x, particleY + 0.7f, z),
            16,
            0.3f,
            0.95f,
            1.9f,
            -0.4f * (float) (movementSpeed * 2) * 0.3f,
            0
        );
    }

    public static void spawnSparksAtMinecartWheels(double minecartX, double minecartY, double minecartZ, double minecartHorizontalRot, double minecartVerticalRot, boolean isOnRails, boolean hasPassenger, boolean hasBlock, Vec3 deltaMovement, double maxSpeed, ClientLevel level) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, minecartX, minecartY, minecartZ)) return;
        if (!EntityOptions.MINECART_SPARKS_ENABLED.getValue()) return;
        if (!isOnRails) return;
        if (!(hasBlock || hasPassenger) && EntityOptions.MINECART_SPARKS_ONLY_WITH_PASSENGER.getValue()) return;

        double speed = MathHelper.maxVec3(deltaMovement.toVector3f(), true);
        if (speed < 0.05) return;

        float sparksChancePerWheel = (float) (Math.clamp(speed, 0, maxSpeed) / maxSpeed) - 0.75f;
        sparksChancePerWheel *= EntityOptions.MINECART_SPARKS_SPAWN_CHANCE.getValue() / 50f;

        float rotX = (float) ((minecartHorizontalRot) * (Math.PI / 180));
        float rotY = (float) ((minecartVerticalRot) * (Math.PI / 180));
        float sparkDeltaX = (float) Math.clamp(-deltaMovement.x / 3, -0.7, 0.7);
        float sparkDeltaZ = (float) Math.clamp(-deltaMovement.z / 3, -0.7, 0.7);

        minecartY += 0.0425;
        ParticleContext context = ParticleContext.plain(level, BlockPos.containing(minecartX, minecartY, minecartZ));
        if (level.getRandom().nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos1 = minecartWheelPoint(rotX, rotY, 0.45f, 0.35f, 0.45f);
            ParticleSpawner.spawnWithDefaultComponents(
                ParticleIDs.FLYING_SPARK.get(),
                context,
                wheelPos1.x + minecartX,
                wheelPos1.y + minecartY,
                wheelPos1.z + minecartZ,
                sparkDeltaX,
                0.17,
                sparkDeltaZ
            );
        }
        if (level.getRandom().nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos2 = minecartWheelPoint(rotX, rotY, -0.45f, -0.35f, 0.45f);
            ParticleSpawner.spawnWithDefaultComponents(
                ParticleIDs.FLYING_SPARK.get(),
                context,
                wheelPos2.x + minecartX,
                wheelPos2.y + minecartY,
                wheelPos2.z + minecartZ,
                sparkDeltaX,
                0.17,
                sparkDeltaZ
            );
        }
        if (level.getRandom().nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos3 = minecartWheelPoint(rotX, rotY, 0.45f, 0.35f, -0.45f);
            ParticleSpawner.spawnWithDefaultComponents(
                ParticleIDs.FLYING_SPARK.get(),
                context,
                wheelPos3.x + minecartX,
                wheelPos3.y + minecartY,
                wheelPos3.z + minecartZ,
                sparkDeltaX,
                0.17,
                sparkDeltaZ
            );
        }
        if (level.getRandom().nextFloat() < sparksChancePerWheel) {
            Vector3f wheelPos4 = minecartWheelPoint(rotX, rotY, -0.45f, -0.35f, -0.45f);
            ParticleSpawner.spawnWithDefaultComponents(
                ParticleIDs.FLYING_SPARK.get(),
                context,
                wheelPos4.x + minecartX,
                wheelPos4.y + minecartY,
                wheelPos4.z + minecartZ,
                sparkDeltaX,
                0.17,
                sparkDeltaZ
            );
        }
    }

    private static Vector3f minecartWheelPoint(float rotationX, float rotationY, float pointX, float pointY, float pointZ) {
        return new Vector3f((float) (pointX * Math.cos(rotationX) - pointZ * Math.sin(rotationX)), pointY * rotationY, (float) (pointZ * Math.cos(rotationX) + pointX * Math.sin(rotationX)));
    }

    public static void spawnFlintAndSteelSparkParticle(ClientLevel level, BlockPos particlePos, boolean litSomething) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, particlePos)) return;
        if (!ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_ENABLED.getValue()) return;

        BlockState blockState = level.getBlockState(particlePos);
        ParticleContext context = ParticleContext.block(level, blockState, particlePos);
        EmitterRuleSet emitterRule = litSomething ? EmitterRuleSetIds.FLINT_AND_STEEL_USE.get() : EmitterRuleSetIds.FIRE_PLACED.get();

        VoxelShape shape = blockState.getCollisionShape(level, particlePos);
        boolean spawnLess = false;
        if(!shape.isEmpty() && litSomething) {
            AABB bounds = shape.bounds();
            final float smallBoxSize = 0.4f;
            spawnLess =
                Math.abs(bounds.minX - bounds.maxX) < smallBoxSize ||
                Math.abs(bounds.minY - bounds.maxY) < smallBoxSize ||
                Math.abs(bounds.minZ - bounds.maxZ) < smallBoxSize;
        }

        int amount = spawnLess ? ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_AMOUNT.getValue() / 3 : ItemInteractionOptions.FLINT_AND_STEEL_SPARKS_AMOUNT.getValue();

        for (int i = 0; i < amount + 1; i++) {
            double x = particlePos.getX() + 0.5 + ((level.getRandom().nextFloat() - 0.5) * 0.1);
            double y = particlePos.getY() + 0.5 + ((level.getRandom().nextFloat() - 0.5) * 0.1);
            double z = particlePos.getZ() + 0.5 + ((level.getRandom().nextFloat() - 0.5) * 0.1);
            emitterRule.getEmitter(context).spawnParticle(
                context,
                x,
                y,
                z,
                (level.getRandom().nextDouble() - 0.5) * 0.25,
                (level.getRandom().nextDouble() + 0.8) * 0.25 * 0.8,
                (level.getRandom().nextDouble() - 0.5) * 0.25
            );
        }
    }

    public static void spawnAmbientCampfireSparks(ClientLevel level, BlockPos particlePos, BlockState campfireState) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, particlePos)) return;
        ParticleContext context = ParticleContext.block(level, campfireState, particlePos);

        if (BlockInteractionOptions.CAMPFIRE_SPARK_ENABLED.getValue()) {
            if (level.getRandom().nextFloat() * 101 <= BlockInteractionOptions.CAMPFIRE_SPARK_SPAWN_CHANCE.getValue()) {
                for (int i = 0; i < level.getRandom().nextIntBetweenInclusive(1, 3) + 1; i++) {
                    SpawnParticlesUtil.spawnMostlyUpwardsMotionParticleOption(
                        context,
                        EmitterRuleSetIds.CAMPFIRE_SPARKS.get(),
                        (double) particlePos.getX() + 0.5,
                        (double) particlePos.getY() + 0.5,
                        (double) particlePos.getZ() + 0.5,
                        0.05
                    );
                }
            }
        }
        if (BlockInteractionOptions.CAMPFIRE_EMBER_ENABLED.getValue()) {
            if (level.getRandom().nextFloat() * 101 <= BlockInteractionOptions.CAMPFIRE_EMBER_SPAWN_CHANCE.getValue()) {
                for (int i = 0; i < level.getRandom().nextIntBetweenInclusive(1, 4); i++) {
                    EmitterRuleSet emitterRuleSet = EmitterRuleSetIds.CAMPFIRE_EMBERS.get();
                    emitterRuleSet.getEmitter(context).spawnParticle(
                        context,
                        (double) particlePos.getX() + (level.getRandom().nextFloat() * 0.75) + 0.125f,
                        (double) particlePos.getY() + (level.getRandom().nextFloat() * 0.75) + 0.125f,
                        (double) particlePos.getZ() + (level.getRandom().nextFloat() * 0.75) + 0.125f,
                        0,
                        0,
                        0
                    );
                }
            }
        }
    }

    public static void spawnAmbientFireSparks(ClientLevel level, BlockState fireState, BlockPos particlePos, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, particlePos)) return;
        double width = Math.abs(minX - maxX);
        double height = Math.abs(minY - maxY);
        double depth = Math.abs(minZ - maxZ);
        ParticleContext context = ParticleContext.block(level, fireState, particlePos);

        if (BlockInteractionOptions.FIRE_SPARK_ENABLED.getValue()) {
            if (level.getRandom().nextFloat() * 101 <= BlockInteractionOptions.FIRE_SPARK_SPAWN_CHANCE.getValue()) {
                for (int i = 0; i < level.getRandom().nextIntBetweenInclusive(1, 3) + 1; i++) {
                    SpawnParticlesUtil.spawnMostlyUpwardsMotionParticleOption(
                        context,
                        EmitterRuleSetIds.FIRE_SPARKS.get(),
                        particlePos.getX() + minX + (level.getRandom().nextFloat() * width),
                        particlePos.getY() + minY + (level.getRandom().nextFloat() * height),
                        particlePos.getZ() + minZ + (level.getRandom().nextFloat() * depth),
                        0.05
                    );
                }
            }
        }
        if (BlockInteractionOptions.FIRE_EMBER_ENABLED.getValue()) {
            if (level.getRandom().nextFloat() * 101 <= BlockInteractionOptions.FIRE_EMBER_SPAWN_CHANCE.getValue()) {
                EmitterRuleSet emitterRuleSet = EmitterRuleSetIds.FIRE_EMBERS.get();
                for (int i = 0; i < level.getRandom().nextIntBetweenInclusive(1, 4); i++) {
                    emitterRuleSet.getEmitter(context).spawnParticle(
                        context,
                        particlePos.getX() + minX + (level.getRandom().nextFloat() * width),
                        particlePos.getY() + minY + (level.getRandom().nextFloat() * height),
                        particlePos.getZ() + minZ + (level.getRandom().nextFloat() * depth),
                        0,
                        0,
                        0
                    );
                }
            }
        }
    }

    public static void spawnFireChargeSmokeParticle(ClientLevel level, BlockPos particlePos) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, particlePos)) return;
        if (!ItemInteractionOptions.FIRE_CHARGE_PARTICLES_ENABLED.getValue()) return;
        EmitterRuleSet emitterRuleSet = EmitterRuleSetIds.FIRE_CHARGE_USE.get();
        ParticleContext context = ParticleContext.block(level, level.getBlockState(particlePos), particlePos);

        for (int i = 0; i < ItemInteractionOptions.FIRE_CHARGE_PARTICLES_AMOUNT.getValue(); i++) {
            double x = particlePos.getX() + 0.25 + (level.getRandom().nextDouble() / 2);
            double y = particlePos.getY() + 0.25 + (level.getRandom().nextDouble() / 2);
            double z = particlePos.getZ() + 0.25 + (level.getRandom().nextDouble() / 2);
            emitterRuleSet.getEmitter(context).spawnParticle(
                context,
                x,
                y,
                z,
                (level.getRandom().nextDouble() - 0.5),
                (level.getRandom().nextDouble() + 0.5),
                (level.getRandom().nextDouble() - 0.5)
            );
        }
    }

    public static void spawnHoeTillParticle(ClientLevel level, BlockPos blockPos, UseOnContext useOnContext) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!ItemInteractionOptions.HOE_TILL_ENABLED.getValue()) return;
        Vec3 clickedPosition = useOnContext.getClickLocation();
        Direction clickDirection = useOnContext.getClickedFace();
        BlockState state = level.getBlockState(blockPos);

        ParticleOrigin origin = ParticleOrigin.BLOCK_TILLED;

        OverridePreset override = BlockOverrideManager.getForBlock(state, origin);
        ParticleContext context = ParticleContext.block(level, state, blockPos);

        for (int i = 0; i < ItemInteractionOptions.HOE_TILL_AMOUNT.getValue(); i++) {
            double x = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepX());
            double y = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepY());
            double z = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepZ());
            override.getRandom().spawnParticle(
                origin,
                context,
                clickedPosition.x + x,
                clickedPosition.y + y,
                clickedPosition.z + z,
                clickDirection.getStepX() + (level.getRandom().nextDouble() - 0.5),
                clickDirection.getStepY() + (level.getRandom().nextDouble() - 0.5),
                clickDirection.getStepZ() + (level.getRandom().nextDouble() - 0.5)
            );
        }
    }

    public static void spawnShovelFlattenParticle(ClientLevel level, BlockPos blockPos, UseOnContext useOnContext) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!ItemInteractionOptions.SHOVEL_FLATTEN_ENABLED.getValue()) return;
        Vec3 clickedPosition = useOnContext.getClickLocation();
        Direction clickDirection = useOnContext.getClickedFace();

        BlockState state = level.getBlockState(blockPos);

        ParticleOrigin origin = ParticleOrigin.BLOCK_FLATTENED;

        OverridePreset override = BlockOverrideManager.getForBlock(state, origin);
        ParticleContext context = ParticleContext.block(level, state, blockPos);

        for (int i = 0; i < ItemInteractionOptions.SHOVEL_FLATTEN_AMOUNT.getValue(); i++) {
            double x = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepX());
            double y = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepY());
            double z = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepZ());
            override.getRandom().spawnParticle(
                origin,
                context,
                clickedPosition.x + x,
                clickedPosition.y + y,
                clickedPosition.z + z,
                clickDirection.getStepX() + (level.getRandom().nextDouble() - 0.5),
                clickDirection.getStepY() + (level.getRandom().nextDouble() - 0.5),
                clickDirection.getStepZ() + (level.getRandom().nextDouble() - 0.5)
            );
        }
    }

    public static void spawnAxeStripParticle(ClientLevel level, BlockPos blockPos, BlockState unstrippedBlockState, BlockState strippedBlockState, UseOnContext context) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!ItemInteractionOptions.AXE_STRIP_ENABLED.getValue()) return;
        Vec3 clickedPosition = context.getClickLocation();
        Direction clickDirection = context.getClickedFace();

        ParticleOrigin origin = ParticleOrigin.BLOCK_STRIPPED;

        OverridePreset strippedOverride = BlockOverrideManager.getForBlock(strippedBlockState, origin);
        ParticleContext strippedContext = ParticleContext.block(level, strippedBlockState, blockPos);

        OverridePreset unstrippedOverride = BlockOverrideManager.getForBlock(unstrippedBlockState, origin);
        ParticleContext unstrippedContext = ParticleContext.block(level, unstrippedBlockState, blockPos);

        for (int i = 0; i < ItemInteractionOptions.AXE_STRIP_AMOUNT.getValue(); i++) {
            double x = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepX());
            double y = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepY());
            double z = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 - clickDirection.getStepZ());
            boolean useStripped = level.getRandom().nextFloat() > 0.9;
            (useStripped ? strippedOverride: unstrippedOverride).getRandom().spawnParticle(
                origin,
                useStripped ? strippedContext: unstrippedContext,
                clickedPosition.x + x,
                clickedPosition.y + y,
                clickedPosition.z + z,
                clickDirection.getStepX() + (level.getRandom().nextDouble() - 0.5),
                clickDirection.getStepY() + (level.getRandom().nextDouble() - 0.5),
                clickDirection.getStepZ() + (level.getRandom().nextDouble() - 0.5)
            );
        }
    }

    public static void spawnFluidPlacedParticle(ClientLevel level, BlockPos particlePos, FluidState placedFluid) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.BLOCK_PLACE_OR_BREAK, particlePos)) return;
        if (BiomeHelpers.isWarmDimension(level.dimensionType()) && placedFluid.is(FluidTags.WATER)) return;
        if (placedFluid.is(Fluids.EMPTY)) return;

        ParticleOrigin origin = ParticleOrigin.FLUID_PLACED;
        OverridePreset override = FluidOverrideManager.getForFluid(placedFluid, origin);
        ParticleContext context = ParticleContext.fluid(level, placedFluid, particlePos);

        for (int i = 0; i < FluidInteractionOptions.AMOUNT_ON_PLACE.getValue(); i++) {
            double x = particlePos.getX() + level.getRandom().nextDouble();
            double y = particlePos.getY() + (level.getRandom().nextDouble() / 1.5) + 0.6;
            double z = particlePos.getZ() + level.getRandom().nextDouble();
            override.getRandom().spawnParticle(
                origin,
                context,
                x,
                y,
                z,
                0.0,
                0.21,
                0.0
            );
        }
    }

    public static void spawnAnvilUseSparkParticles(ClientLevel level, BlockPos blockPos) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        if (!BlockInteractionOptions.ANVIL_USE_SPARKS_ENABLED.getValue()) return;
        double x = blockPos.getX() + 0.5f;
        double y = blockPos.getY() + 1. + (level.getRandom().nextDouble() / 16f);
        double z = blockPos.getZ() + 0.5f;
        RandomDistributionEmitterOptions emitter = new RandomDistributionEmitterOptions(
            ParticleTypesRegistry.DISTRIBUTION_EMITTER,
            3,
            7,
            1,
            new Vector3f(0.25f, 0, 0.25f),
            EmitterRuleSetIds.ANVIL_USE_EMISSION.get()
        );
        SpawnParticlesUtil.spawnParticleInCircle(
            emitter,
            ParticleContext.plain(level, blockPos),
            new Vec3(x, y, z),
            BlockInteractionOptions.ANVIL_USE_SPARKS_MAX_ON_USE.getValue(),
            0.32f,
            0.16f,
            1f,
            0.2f,
            1f
        );
    }

    public static void spawnGrindstoneUseSparkParticles(ClientLevel level, BlockPos blockPos) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
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
        ParticleContext context = ParticleContext.plain(level, blockPos);
        final float HORIZONTAL_MIN_SPEED = 0.05f;
        final float HORIZONTAL_MAX_SPEED = 0.25f;
        final float UPWARDS_SPEED = 0.3f;
        final float DOWNWARDS_SPEED = 0.1f;

        ParticleSpawner.spawnWithAppearance(
            emitter,
            ParticleAppearance.MISSING_APPEARANCE.get(),
            context,
            x,
            y,
            z,
            facing.getStepX() * (attachFace == AttachFace.WALL ? HORIZONTAL_MIN_SPEED : HORIZONTAL_MAX_SPEED),
            attachFace == AttachFace.WALL ? UPWARDS_SPEED : 0,
            facing.getStepZ() * (attachFace == AttachFace.WALL ? HORIZONTAL_MIN_SPEED : HORIZONTAL_MAX_SPEED)
        );
        ParticleSpawner.spawnWithAppearance(
            emitter,
            ParticleAppearance.MISSING_APPEARANCE.get(),
            context,
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
            ParticleTypesRegistry.DISTRIBUTION_EMITTER,
            Math.min(amount, 6),
            1,
            (int) Math.ceil((double) amount / 6),
            new Vector3f(width, height, depth),
            EmitterRuleSetIds.GRINDSTONE_USE_EMISSION.get()
        );
    }

    public static void spawnRedstoneInteractionParticles(ClientLevel level, BlockState blockState, double interactionX, double interactionY, double interactionZ, float spreadX, float spreadY, float spreadZ) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, interactionX, interactionY, interactionZ)) return;
        if (!BlockInteractionOptions.REDSTONE_INTERACTION_DUST_ENABLED.getValue()) return;
        BlockPos pos = BlockPos.containing(interactionX, interactionY, interactionZ);

        ParticleOrigin origin = ParticleOrigin.BLOCK_REDSTONE_INTERACTED_WITH;
        OverridePreset override = BlockOverrideManager.getForBlock(blockState, origin);
        ParticleContext context = ParticleContext.block(level, blockState, pos);

        for (int i = 0; i < BlockInteractionOptions.REDSTONE_INTERACTION_DUST_AMOUNT.getValue(); i++) {
            double particleX = interactionX + MathHelper.randomBetween(-spreadX / 2, spreadX / 2);
            double particleY = interactionY + MathHelper.randomBetween(-spreadY / 2, spreadY / 2);
            double particleZ = interactionZ + MathHelper.randomBetween(-spreadZ / 2, spreadZ / 2);
            override.getRandom().spawnParticle(
                origin,
                context,
                particleX,
                particleY,
                particleZ,
                MathHelper.randomBetween(-0.05f, 0.05f),
                0.2f,
                MathHelper.randomBetween(-0.05f, 0.05f)
            );
        }
    }

    public static void spawnLavaBubblePopParticles(ClientLevel level, BlockPos fluidPos, FluidState fluidState) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, fluidPos)) return;
        if (!FluidInteractionOptions.LAVA_BUBBLE_POP_ENABLED.getValue()) return;
        if (level.getRandom().nextFloat() < (float) FluidInteractionOptions.LAVA_BUBBLE_POP_SPAWN_CHANCE.getValue() / 2500) {
            double d0 = (double) fluidPos.getX() + level.getRandom().nextDouble();
            double d1 = (double) fluidPos.getY() + fluidState.getOwnHeight();
            double d2 = (double) fluidPos.getZ() + level.getRandom().nextDouble();
            ParticleContext context = ParticleContext.fluid(level, fluidState, fluidPos);
            EmitterRuleSetIds.LAVA_SURFACE.get().getEmitter(context).spawnParticle(
                context,
                d0,
                d1,
                d2,
                0.0f,
                0.0f,
                0.0f
            );
        }
    }

    public static void spawnRandomUnderwaterBubbleStreams(ClientLevel level, BlockPos blockPos, BlockState blockState) {
        if (!FluidInteractionOptions.UNDERWATER_BUBBLE_STREAM_ENABLED.getValue()) return;
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, blockPos)) return;

        if (!ObjectOrTagLocation.doesListContainBlock(FluidInteractionOptions.UNDERWATER_BUBBLE_STREAM_BLOCKS.getValue(), blockState)) return;

        if (!FluidHelpers.probablyPlacedUnderwater(level, blockPos)) return;

        ParticleContext context = ParticleContext.plain(level, blockPos);
        var emitterRuleSet = EmitterRuleSetIds.UNDERWATER_BUBBLE_STREAM.get();

        if (level.getRandom().nextFloat() < (float) FluidInteractionOptions.UNDERWATER_BUBBLE_STREAM_SPAWN_CHANCE.getValue() / 2500) {
            double x = (double) blockPos.getX() + level.getRandom().nextDouble();
            double y = (double) blockPos.getY() + (blockState.isSolid() ? 1.05 : level.getRandom().nextDouble());
            double z = (double) blockPos.getZ() + level.getRandom().nextDouble();
            RandomDistributionEmitterOptions emitter = new RandomDistributionEmitterOptions(
                ParticleTypesRegistry.DISTRIBUTION_EMITTER,
                MathHelper.randomBetween(9, 20),
                MathHelper.randomBetween(2, 4),
                1,
                emitterRuleSet
            );
            ParticleSpawner.spawnWithAppearance(
                emitter,
                ParticleAppearance.MISSING_APPEARANCE.get(),
                context,
                x,
                y,
                z,
                0.0f,
                0.0f,
                0.0f
            );
        }
    }

    public static void spawnBlockDisturbanceParticles(ClientLevel level, BlockPos blockPos, BlockState blockState, double entityX, double entityY, double entityZ, Vec3 deltaMovement, boolean isSprinting) {
        if (!BlockInteractionOptions.BLOCK_RUSTLE_ENABLED.getValue()) return;
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, blockPos)) return;
        double speed = deltaMovement.length();
        if (speed <= 0.1 && !isSprinting) return;

        if (!ObjectOrTagLocation.doesListContainBlock(BlockInteractionOptions.BLOCK_RUSTLE_BLOCKS.getValue(), blockState)) return;

        ParticleOrigin origin = ParticleOrigin.BLOCK_WALKED_THROUGH;
        OverridePreset override = BlockOverrideManager.getForBlock(blockState, origin);
        ParticleContext context = ParticleContext.block(level, blockState, blockPos);

        int particlesAmount =  (speed > 0.25 || isSprinting ? 3 : 1);

        for (int i = 0; i < particlesAmount; i++) {
            double particleX = entityX + ((level.getRandom().nextFloat() * 0.5) - 0.25);
            double particleY = entityY + 0.35 + ((level.getRandom().nextFloat() * 0.5) - 0.25);
            double particleZ = entityZ + ((level.getRandom().nextFloat() * 0.5) - 0.25);

            // skip spawning if the particle is out of the block bounds
            BlockPos entityBlockPos = BlockPos.containing(particleX, blockPos.getY(), particleZ);
            if (level.getBlockState(entityBlockPos).isAir()) continue;

            override.getRandom().spawnParticle(
                origin,
                context,
                particleX,
                particleY,
                particleZ,
                deltaMovement.x * 3,
                Math.min(deltaMovement.y, 0.1) * 3 + 0.1,
                deltaMovement.z * 3
            );
        }
    }

    public static void spawnItemFrameInteractionParticles(ClientLevel level, double x, double y, double z, AABB boundingBox, Direction itemFrameDirection, ItemFrameParticleOrigin particleOrigin, ItemFrame frame) {
        if (!EntityOptions.ITEM_FRAME_INTERACTION_ENABLED.getValue()) return;
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.INTERACTION, x, y, z)) return;

        ParticleContext context = ParticleContext.entity(level, frame);

        if (particleOrigin == ItemFrameParticleOrigin.FRAME_KILLED) {
            return;
        }

        for (int i = 0; i < EntityOptions.ITEM_FRAME_INTERACTION_AMOUNT.getValue(); i++) {
            double randomX = boundingBox.minX + (boundingBox.getXsize() * level.getRandom().nextDouble());
            double randomY = boundingBox.minY + (boundingBox.getYsize() * level.getRandom().nextDouble());
            double randomZ = boundingBox.minZ + (boundingBox.getZsize() * level.getRandom().nextDouble());

            EmitterRuleSetIds.ITEM_FRAME_EMISSION.get().getEmitter(context).spawnParticle(
                context,
                (itemFrameDirection.getStepX() * 0.15) + x,
                (itemFrameDirection.getStepY() * 0.15) + y,
                (itemFrameDirection.getStepZ() * 0.15) + z,
                (itemFrameDirection.getStepX() * 0.03) + (randomX - x) * 2,
                (itemFrameDirection.getStepY() * 0.03) + (randomY - y) * 2,
                (itemFrameDirection.getStepZ() * 0.03) + (randomZ - z) * 2
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
        if (!BlockInteractionOptions.SMOKER_SMOKE_ENABLED.getValue()) return;
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, blockPos)) return;

        if (level.getRandom().nextFloat() > 0.3) {
            Vec3 centerPos = blockPos.getCenter();
            level.addParticle(net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE, centerPos.x, blockPos.getY() + .8, centerPos.z, 0, 0.07f, 0);
        }
    }

    public static void spawnAdditionalFurnaceParticles(ClientLevel level, BlockPos blockPos, BlockState furnaceState) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, blockPos)) return;
        if (!BlockInteractionOptions.FURNACE_EMBERS_ENABLED.getValue()) return;

        furnaceFrontSparks(level, furnaceState.getValue(FurnaceBlock.FACING), furnaceState, blockPos, EmitterRuleSetIds.FURNACE_FRONT.get());
    }

    public static void spawnAdditionalBlastFurnaceParticles(ClientLevel level, BlockPos blockPos, BlockState furnaceState) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, blockPos)) return;
        if (!BlockInteractionOptions.BLAST_FURNACE_SPARKS_ENABLED.getValue()) return;

        furnaceFrontSparks(level, furnaceState.getValue(FurnaceBlock.FACING), furnaceState, blockPos, EmitterRuleSetIds.BLAST_FURNACE_FRONT.get());
    }

    private static void furnaceFrontSparks(ClientLevel level, Direction direction, BlockState state, BlockPos pos, EmitterRuleSet ruleSet) {
        double[] positions = ParticlePositionHelpers.getRandomFurnaceParticlePosition(pos, state);
        if (level.getBlockState(BlockPos.containing(positions[0], positions[1], positions[2])).isSuffocating(level, pos)) return;

        ParticleContext context = ParticleContext.block(level, state, pos);
        final float outwardVelocity = MathHelper.randomBetween(0.01f, 0.03f);
        ruleSet.getEmitter(context).spawnParticle(
            context,
            positions[0],
            positions[1],
            positions[2],
            direction.getStepX() * outwardVelocity,
            0.05f,
            direction.getStepZ() * outwardVelocity
        );

    }

    public static void spawnLightningImpactSparks(ClientLevel level, double x, double y, double z) {
        if (SpawnParticlesUtil.isParticleOutsideRenderDistance(ParticleCategory.AMBIENT, x, y, z)) return;
        if (!EntityOptions.LIGHTNING_STRIKE_ENABLED.getValue()) return;
        ParticleContext context = ParticleContext.plain(level, BlockPos.containing(x, y, z));

        SpawnParticlesUtil.spawnParticleInCircle(
            new ArcEmitterOptions(
                ParticleTypesRegistry.ARC_EMITTER,
                MathHelper.randomBetween(7, 14),
                MathHelper.randomBetween(3, 5),
                40,
                MathHelper.randomBetween(4, 6),
                ArcEmitterOptions.TICK_INTERVAL_DEFAULT,
                MathHelper.randomBetween(160, 380),
                null,
                EmitterRuleSetIds.LIGHTNING_ARCS.get()
            ),
            context,
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
            ParticleIDs.FLYING_SPARK.get(),
            context,
            new Vec3(x, y + 0.01, z),
            MathHelper.randomBetween(Math.max(0, amountOfSparks - 4), amountOfSparks),
            0.3f,
            0.8f,
            0.25f,
            0.25f,
            1.1f
        );
    }

    public static void spawnHoneyCollectionParticles(ClientLevel level, double x, double y, double z, Direction faceDirection) {
        if (!ItemInteractionOptions.HONEY_COLLECTION_ENABLED.getValue()) return;
        int amount = ItemInteractionOptions.HONEY_COLLECTION_AMOUNT.getValue();
        ParticleContext context = ParticleContext.plain(level, BlockPos.containing(x, y, z));
        EmitterRuleSet emitters = EmitterRuleSetIds.HONEY_COLLECTION.get();

        for (int i = 0; i < level.getRandom().nextIntBetweenInclusive(Math.max(amount - 2, 0), Math.max(amount, 1)); i++) {
            double xOffset = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 + Math.abs(faceDirection.getStepX()));
            double yOffset = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 + Math.abs(faceDirection.getStepY()));
            double zOffset = (level.getRandom().nextDouble() - 0.5) * 0.5 * (1 + Math.abs(faceDirection.getStepZ()));
            emitters.getEmitter(context).spawnParticle(
                context,
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
        if (!ItemInteractionOptions.HONEY_COLLECTION_ENABLED.getValue()) return;
        int amount = ItemInteractionOptions.HONEY_COLLECTION_AMOUNT.getValue();
        ParticleContext context = ParticleContext.plain(level, player.blockPosition());
        EmitterRuleSet emitters = EmitterRuleSetIds.HONEY_COLLECTION.get();

        for (int i = 0; i < level.getRandom().nextIntBetweenInclusive(Math.max(amount / 2, 0), Math.max(amount / 2, 1)); i++) {
            emitters.getEmitter(context).spawnParticle(
                context,
                player.getX() - 0.25 + (level.getRandom().nextDouble() / 2),
                player.getY() + 0.85 + (level.getRandom().nextDouble() / 5),
                player.getZ() - 0.25 + (level.getRandom().nextDouble() / 2),
                0,
                0,
                0
            );
        }
    }
}