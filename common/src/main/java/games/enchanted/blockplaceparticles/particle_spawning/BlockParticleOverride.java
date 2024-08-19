package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum BlockParticleOverride {
    NONE("none"),
    BLOCK("block", ParticleTypes.BLOCK, true),
    SNOW_POWDER("snow_powder", ParticleTypes.SNOWFLAKE, false),
    AZALEA_LEAF("azalea_leaf", ModParticleTypes.FALLING_AZALEA_LEAF, false),
    FLOWERING_AZALEA_LEAF("flowering_azalea_leaf", ModParticleTypes.FALLING_FLOWERING_AZALEA_LEAF, false),
    TINTED_LEAF("tinted_leaf", ModParticleTypes.FALLING_TINTED_LEAF, true),
    CHERRY_LEAF("cherry_leaf", ModParticleTypes.FALLING_CHERRY_PETAL, false);

    private final String name;
    @Nullable private ParticleOptions particleType;
    @Nullable private ParticleType<BlockParticleOption> blockParticleType;
    private final boolean isBlockStateParticle;

    BlockParticleOverride(String overrideName, boolean isBlockStateParticle) {
        this.name = overrideName;
        this.isBlockStateParticle = isBlockStateParticle;
    }
    BlockParticleOverride(String overrideName) {
        this(overrideName, false);
        this.particleType = null;
        this.blockParticleType = null;
    }
    BlockParticleOverride(String overrideName, @Nullable ParticleOptions particle, boolean isBlockStateParticle) {
        this(overrideName, isBlockStateParticle);
        this.particleType = particle;
        this.blockParticleType = null;
    }
    BlockParticleOverride(String overrideName, @Nullable ParticleType<BlockParticleOption> particle, boolean isBlockStateParticle) {
        this(overrideName, isBlockStateParticle);
        this.particleType = null;
        this.blockParticleType = particle;
    }

    /**
     * @return the {@link ParticleOptions} or null if this particle type is a blockstate particle.
     * @see BlockParticleOverride#getBlockParticleOption
     * @see BlockParticleOverride#isBlockStateParticle
     */
    public @Nullable ParticleOptions getParticleOption() {
        return this.particleType;
    }

    /**
     * @param blockState the {@link BlockState} to use when creating the particle options
     * @return a new {@link BlockParticleOption} or null if this particle type is not a blockstate particle.
     * @see BlockParticleOverride#getParticleOption
     * @see BlockParticleOverride#isBlockStateParticle
     */
    public @Nullable BlockParticleOption getBlockParticleOption(BlockState blockState) {
        if(this.blockParticleType == null) return null;
        return new BlockParticleOption(this.blockParticleType, blockState);
    }

    public boolean isBlockStateParticle() {
        return this.isBlockStateParticle;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    public static BlockParticleOverride getOverrideForBlockState(BlockState blockState) {
        return getOverrideForBlockState(blockState, true);
    }

    public static BlockParticleOverride getOverrideForBlockState(BlockState blockState, boolean isBlockBeingPlaced) {
        Block block = blockState.getBlock();
        if (ConfigHandler.snowParticleBlockItems.contains((BlockItem) block.asItem()) && shouldHaveParticle(isBlockBeingPlaced, ConfigHandler.snowParticleOnBlockPlace, ConfigHandler.snowParticleOnBlockBreak)) {
            return SNOW_POWDER;
        } else if (ConfigHandler.cherryPetalParticleBlockItems.contains((BlockItem) block.asItem())) {
            return CHERRY_LEAF;
        } else if (ConfigHandler.azaleaLeafParticleBlockItems.contains((BlockItem) block.asItem())) {
            return AZALEA_LEAF;
        } else if (ConfigHandler.floweringAzaleaLeafParticleBlockItems.contains((BlockItem) block.asItem())) {
            return FLOWERING_AZALEA_LEAF;
        } else if (ConfigHandler.biomeTintedLeafParticleBlockItems.contains((BlockItem) block.asItem())) {
            return TINTED_LEAF;
        }
        if (shouldHaveParticle(isBlockBeingPlaced, ConfigHandler.blockParticleOnBlockPlace, ConfigHandler.blockParticleOnBlockBreak)) {
            return BLOCK;
        }
        return NONE;
    }

    private static boolean shouldHaveParticle(boolean isBlockBeingPlaced, boolean spawnOnBlockPlace, boolean spawnOnBlockBreak) {
        if(isBlockBeingPlaced) {
            return spawnOnBlockPlace;
        }
        return spawnOnBlockBreak;
    }

    public static int getParticleMultiplierForOverride(BlockParticleOverride override, boolean isBlockBeingPlaced) {
        switch (override) {
            case BLOCK -> {
                return getAppropriateMultiplier(isBlockBeingPlaced, ConfigHandler.maxBlockPlaceParticles, ConfigHandler.maxBlockBreakParticles);
            }
            case SNOW_POWDER -> {
                return getAppropriateMultiplier(isBlockBeingPlaced, ConfigHandler.maxSnowflakePlaceParticles, ConfigHandler.maxSnowflakeBreakParticles);
            }
            default -> {
                return 2;
            }
        }
    }

    private static int getAppropriateMultiplier(boolean isBlockBeingPlaced, int blockPlaceMultiplier, int blockBreakMultiplier) {
        if(isBlockBeingPlaced) {
            return blockPlaceMultiplier;
        }
        return blockBreakMultiplier;
    }
}
