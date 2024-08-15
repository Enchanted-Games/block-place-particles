package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public enum BlockParticleOverride {
    NONE("none", ParticleTypes.BLOCK, true),
    SNOW_POWDER("snow_powder", ParticleTypes.SNOWFLAKE, false),
    AZALEA_LEAF("azalea_leaf", ModParticleTypes.FALLING_AZALEA_LEAF, false),
    FLOWERING_AZALEA_LEAF("flowering_azalea_leaf", ModParticleTypes.FALLING_FLOWERING_AZALEA_LEAF, false),
    TINTED_LEAF("tinted_leaf", ModParticleTypes.FALLING_TINTED_LEAF, true),
    CHERRY_LEAF("cherry_leaf", ModParticleTypes.FALLING_CHERRY_PETAL, false);

    private final String name;
    private ParticleOptions particleType;
    private ParticleType<BlockParticleOption> blockParticleType;
    private final boolean isBlockStateParticle;

    BlockParticleOverride(String overrideName, boolean isBlockStateParticle) {
        this.name = overrideName;
        this.isBlockStateParticle = isBlockStateParticle;
    }

    BlockParticleOverride(String overrideName, ParticleOptions particle, boolean isBlockStateParticle) {
        this(overrideName, isBlockStateParticle);
        this.particleType = particle;
        this.blockParticleType = null;
    }
    BlockParticleOverride(String overrideName, ParticleType<BlockParticleOption> particle, boolean isBlockStateParticle) {
        this(overrideName, isBlockStateParticle);
        this.particleType = null;
        this.blockParticleType = particle;
    }

    public ParticleOptions getParticleOption() {
        return this.particleType;
    }
    public BlockParticleOption getBlockParticleOption(BlockState blockState) {
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
        Block block = blockState.getBlock();
        if (block.equals(Blocks.POWDER_SNOW) || blockState.is(BlockTags.SNOW)) {
            return SNOW_POWDER;
        } else if (block.equals(Blocks.CHERRY_LEAVES) || block.equals(Blocks.CHERRY_SAPLING) || block.equals(Blocks.PINK_PETALS)) {
            return CHERRY_LEAF;
        } else if (block.equals(Blocks.AZALEA_LEAVES) || block.equals(Blocks.AZALEA)) {
            return AZALEA_LEAF;
        } else if (block.equals(Blocks.FLOWERING_AZALEA_LEAVES) || block.equals(Blocks.FLOWERING_AZALEA)) {
            return FLOWERING_AZALEA_LEAF;
        } else if (blockState.is(BlockTags.LEAVES)) {
            return TINTED_LEAF;
        }
        return NONE;
    }
}
