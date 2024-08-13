package games.enchanted.blockplaceparticles.particle_spawning;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public enum BlockParticleOverride implements StringRepresentable {
    NONE(0, "none"),
    SNOW_POWDER(1, "snow_powder");

    private final int particleOverrideType;
    private final String name;

    BlockParticleOverride(int particleOverrideType, String overrideName) {
        this.particleOverrideType = particleOverrideType;
        this.name = overrideName;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    public static BlockParticleOverride getOverrideForBlockState(BlockState blockState) {
        Block block = blockState.getBlock();
        if (block.equals(Blocks.POWDER_SNOW) || block.equals(Blocks.SNOW) || block.equals(Blocks.SNOW_BLOCK) || blockState.is(BlockTags.SNOW)) {
            return SNOW_POWDER;
        }
        return NONE;
    }
}
