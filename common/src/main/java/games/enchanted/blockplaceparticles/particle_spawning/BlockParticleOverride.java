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
    NONE("none", new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState())),
    SNOW_POWDER("snow_powder", ParticleTypes.SNOWFLAKE),
    AZALEA_LEAF("azalea_leaf", ModParticleTypes.FALLING_CHERRY_PETAL),
    FLOWERING_AZALEA_LEAF("flowering_azalea_leaf", ModParticleTypes.FALLING_CHERRY_PETAL),
    TINTED_LEAF("tinted_leaf", ModParticleTypes.FALLING_CHERRY_PETAL),
    CHERRY_LEAF("cherry_leaf", ModParticleTypes.FALLING_CHERRY_PETAL);

    private final String name;
    private final ParticleOptions particleType;

    BlockParticleOverride(String overrideName, ParticleOptions particle) {
        this.name = overrideName;
        this.particleType = particle;
    }

    public ParticleOptions getParticleType() {
        return this.particleType;
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
            return CHERRY_LEAF;
        }
        return NONE;
    }
}
