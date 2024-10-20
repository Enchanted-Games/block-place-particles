package games.enchanted.blockplaceparticles.particle_spawning.override;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.util.BiomeTemperatureHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockParticleOverrides {
    public static final BlockParticleOverride SNOW_POWDER = new BlockParticleOverride(
        "snowflake",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos) -> {
        if(BiomeTemperatureHelpers.isWarmBiomeOrDimension(level, blockPos)) {
        return level.random.nextInt(5) == 0 ? ParticleTypes.POOF : ParticleTypes.SNOWFLAKE;
        }
        return ParticleTypes.SNOWFLAKE;
        },
        () -> ConfigHandler.snowflake_Blocks,
        (val) -> ConfigHandler.snowflake_Blocks = val,
        ConfigHandler.snowflake_Blocks_DEFAULT,
        () -> ConfigHandler.snowflake_enabled,
        (val) -> ConfigHandler.snowflake_enabled = val,
        ConfigHandler.snowflake_enabled_DEFAULT,
        () -> ConfigHandler.maxSnowflakes_onPlace,
        (val) -> ConfigHandler.maxSnowflakes_onPlace = val,
        ConfigHandler.maxSnowflakes_onPlace_DEFAULT,
        () -> ConfigHandler.maxSnowflakes_onBreak,
        (val) -> ConfigHandler.maxSnowflakes_onBreak = val,
        ConfigHandler.maxSnowflakes_onBreak_DEFAULT
    );
    public static final BlockParticleOverride CHERRY_LEAF = new BlockParticleOverride(
        "cherry_petal",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos) -> ModParticleTypes.FALLING_CHERRY_PETAL,
        () -> ConfigHandler.cherryPetal_Blocks,
        (val) -> ConfigHandler.cherryPetal_Blocks = val,
        ConfigHandler.cherryPetal_Blocks_DEFAULT,
        () -> ConfigHandler.cherryPetal_enabled,
        (val) -> ConfigHandler.cherryPetal_enabled = val,
        ConfigHandler.cherryPetal_enabled_DEFAULT,
        () -> ConfigHandler.maxCherryPetals_onPlace,
        (val) -> ConfigHandler.maxCherryPetals_onPlace = val,
        ConfigHandler.maxCherryPetals_onPlace_DEFAULT,
        () -> ConfigHandler.maxCherryPetals_onBreak,
        (val) -> ConfigHandler.maxCherryPetals_onBreak = val,
        ConfigHandler.maxCherryPetals_onBreak_DEFAULT
    );
    public static final BlockParticleOverride AZALEA_LEAF = new BlockParticleOverride(
        "azalea_leaf",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos) -> ModParticleTypes.FALLING_AZALEA_LEAF,
        () -> ConfigHandler.azaleaLeaf_Blocks,
        (val) -> ConfigHandler.azaleaLeaf_Blocks = val,
        ConfigHandler.azaleaLeaf_Blocks_DEFAULT,
        () -> ConfigHandler.azaleaLeaf_enabled,
        (val) -> ConfigHandler.azaleaLeaf_enabled = val,
        ConfigHandler.azaleaLeaf_enabled_DEFAULT,
        () -> ConfigHandler.maxAzaleaLeaves_onPlace,
        (val) -> ConfigHandler.maxAzaleaLeaves_onPlace = val,
        ConfigHandler.maxAzaleaLeaves_onPlace_DEFAULT,
        () -> ConfigHandler.maxAzaleaLeaves_onBreak,
        (val) -> ConfigHandler.maxAzaleaLeaves_onBreak = val,
        ConfigHandler.maxAzaleaLeaves_onBreak_DEFAULT
    );
    public static final BlockParticleOverride FLOWERING_AZALEA_LEAF = new BlockParticleOverride(
        "flowering_azalea_leaf",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos) -> ModParticleTypes.FALLING_FLOWERING_AZALEA_LEAF,
        () -> ConfigHandler.floweringAzaleaLeaf_Blocks,
        (val) -> ConfigHandler.floweringAzaleaLeaf_Blocks = val,
        ConfigHandler.floweringAzaleaLeaf_Blocks_DEFAULT,
        () -> ConfigHandler.floweringAzaleaLeaf_enabled,
        (val) -> ConfigHandler.floweringAzaleaLeaf_enabled = val,
        ConfigHandler.floweringAzaleaLeaf_enabled_DEFAULT,
        () -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace,
        (val) -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace = val,
        ConfigHandler.maxFloweringAzaleaLeaves_onPlace_DEFAULT,
        () -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak,
        (val) -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak = val,
        ConfigHandler.maxFloweringAzaleaLeaves_onBreak_DEFAULT
    );
    public static final BlockParticleOverride TINTED_LEAF = new BlockParticleOverride(
        "biome_leaf",
        "biome_leaf",
        (BlockState blockState, ClientLevel level, BlockPos blockPos) -> new BlockParticleOption(ModParticleTypes.FALLING_TINTED_LEAF, blockState),
        () -> ConfigHandler.tintedLeaves_Blocks,
        (val) -> ConfigHandler.tintedLeaves_Blocks = val,
        ConfigHandler.tintedLeaves_Blocks_DEFAULT,
        () -> ConfigHandler.tintedLeaves_enabled,
        (val) -> ConfigHandler.tintedLeaves_enabled = val,
        ConfigHandler.tintedLeaves_enabled_DEFAULT,
        () -> ConfigHandler.maxTintedLeaves_onPlace,
        (val) -> ConfigHandler.maxTintedLeaves_onPlace = val,
        ConfigHandler.maxTintedLeaves_onPlace_DEFAULT,
        () -> ConfigHandler.maxTintedLeaves_onBreak,
        (val) -> ConfigHandler.maxTintedLeaves_onBreak = val,
        ConfigHandler.maxTintedLeaves_onBreak_DEFAULT
    );

    public static void registerOverrides() {
        BlockParticleOverride.addBlockParticleOverride(SNOW_POWDER);
        BlockParticleOverride.addBlockParticleOverride(CHERRY_LEAF);
        BlockParticleOverride.addBlockParticleOverride(AZALEA_LEAF);
        BlockParticleOverride.addBlockParticleOverride(FLOWERING_AZALEA_LEAF);
        BlockParticleOverride.addBlockParticleOverride(TINTED_LEAF);
    }
}
