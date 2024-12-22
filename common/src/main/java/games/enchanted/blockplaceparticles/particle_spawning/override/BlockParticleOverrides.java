package games.enchanted.blockplaceparticles.particle_spawning.override;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.option.TintedParticleOption;
import games.enchanted.blockplaceparticles.util.BiomeTemperatureHelpers;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockParticleOverrides {
    public static final BlockParticleOverride SNOW_POWDER = new BlockParticleOverride(
        "snowflake",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> {
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
        ConfigHandler.maxSnowflakes_onBreak_DEFAULT,
        0.1f
    );
    public static final BlockParticleOverride CHERRY_LEAF = new BlockParticleOverride(
        "cherry_petal",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.FALLING_CHERRY_PETAL,
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
        ConfigHandler.maxCherryPetals_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride AZALEA_LEAF = new BlockParticleOverride(
        "azalea_leaf",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.FALLING_AZALEA_LEAF,
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
        ConfigHandler.maxAzaleaLeaves_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride FLOWERING_AZALEA_LEAF = new BlockParticleOverride(
        "flowering_azalea_leaf",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.FALLING_FLOWERING_AZALEA_LEAF,
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
        ConfigHandler.maxFloweringAzaleaLeaves_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride PALE_LEAF = new BlockParticleOverride(
        "pale_leaf",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.FALLING_PALE_OAK_LEAF,
        () -> ConfigHandler.paleLeaf_Blocks,
        (val) -> ConfigHandler.paleLeaf_Blocks = val,
        ConfigHandler.paleLeaf_Blocks_DEFAULT,
        () -> ConfigHandler.paleLeaf_enabled,
        (val) -> ConfigHandler.paleLeaf_enabled = val,
        ConfigHandler.paleLeaf_enabled_DEFAULT,
        () -> ConfigHandler.maxPaleLeaves_onPlace,
        (val) -> ConfigHandler.maxPaleLeaves_onPlace = val,
        ConfigHandler.maxPaleLeaves_onPlace_DEFAULT,
        () -> ConfigHandler.maxPaleLeaves_onBreak,
        (val) -> ConfigHandler.maxPaleLeaves_onBreak = val,
        ConfigHandler.maxPaleLeaves_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride TINTED_LEAF = new BlockParticleOverride(
        "biome_leaf",
        "tinted_or_average",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.FALLING_TINTED_LEAF, blockState),
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
        ConfigHandler.maxTintedLeaves_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride GRASS_BLADE = new BlockParticleOverride(
        "grass_blade",
        "tinted_or_average",
        (int overrideOrigin) -> overrideOrigin != BlockParticleOverride.ORIGIN_ITEM_PARTICLE_OVERRIDDEN,
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> {
            if(
                blockState.getBlock() == Blocks.GRASS_BLOCK &&
                (overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_CRACK || overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_PLACED || overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_BROKEN || overrideOrigin == BlockParticleOverride.ORIGIN_ITEM_PARTICLE_OVERRIDDEN)
            ) {
                // occasionally spawn dirt particles if a grass block is placed or broken
                return level.random.nextFloat() > 0.3 ? new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()) : new BlockParticleOption(ModParticleTypes.GRASS_BLADE, blockState);
            }
            return new BlockParticleOption(ModParticleTypes.GRASS_BLADE, blockState);
        },
        () -> ConfigHandler.grassBlade_Blocks,
        (val) -> ConfigHandler.grassBlade_Blocks = val,
        ConfigHandler.grassBlade_Blocks_DEFAULT,
        () -> ConfigHandler.grassBlade_enabled,
        (val) -> ConfigHandler.grassBlade_enabled = val,
        ConfigHandler.grassBlade_enabled_DEFAULT,
        () -> ConfigHandler.maxGrassBlade_onPlace,
        (val) -> ConfigHandler.maxGrassBlade_onPlace = val,
        ConfigHandler.maxGrassBlade_onPlace_DEFAULT,
        () -> ConfigHandler.maxGrassBlade_onBreak,
        (val) -> ConfigHandler.maxGrassBlade_onBreak = val,
        ConfigHandler.maxGrassBlade_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride HEAVY_GRASS_BLADE = new BlockParticleOverride(
        "heavy_grass_blade",
        "tinted_or_average",
        (int overrideOrigin) -> overrideOrigin != BlockParticleOverride.ORIGIN_ITEM_PARTICLE_OVERRIDDEN,
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.HEAVY_GRASS_BLADE, blockState),
        () -> ConfigHandler.heavyGrassBlade_Blocks,
        (val) -> ConfigHandler.heavyGrassBlade_Blocks = val,
        ConfigHandler.heavyGrassBlade_Blocks_DEFAULT,
        () -> ConfigHandler.heavyGrassBlade_enabled,
        (val) -> ConfigHandler.heavyGrassBlade_enabled = val,
        ConfigHandler.heavyGrassBlade_enabled_DEFAULT,
        () -> ConfigHandler.maxHeavyGrassBlade_onPlace,
        (val) -> ConfigHandler.maxHeavyGrassBlade_onPlace = val,
        ConfigHandler.maxHeavyGrassBlade_onPlace_DEFAULT,
        () -> ConfigHandler.maxHeavyGrassBlade_onBreak,
        (val) -> ConfigHandler.maxHeavyGrassBlade_onBreak = val,
        ConfigHandler.maxHeavyGrassBlade_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride MOSS_CLUMP = new BlockParticleOverride(
        "moss_clump",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.MOSS_CLUMP,
        () -> ConfigHandler.mossClump_Blocks,
        (val) -> ConfigHandler.mossClump_Blocks = val,
        ConfigHandler.mossClump_Blocks_DEFAULT,
        () -> ConfigHandler.mossClump_enabled,
        (val) -> ConfigHandler.mossClump_enabled = val,
        ConfigHandler.mossClump_enabled_DEFAULT,
        () -> ConfigHandler.maxMossClump_onPlace,
        (val) -> ConfigHandler.maxMossClump_onPlace = val,
        ConfigHandler.maxMossClump_onPlace_DEFAULT,
        () -> ConfigHandler.maxMossClump_onBreak,
        (val) -> ConfigHandler.maxMossClump_onBreak = val,
        ConfigHandler.maxMossClump_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride PALE_MOSS_CLUMP = new BlockParticleOverride(
        "pale_moss_clump",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.PALE_MOSS_CLUMP,
        () -> ConfigHandler.paleMossClump_Blocks,
        (val) -> ConfigHandler.paleMossClump_Blocks = val,
        ConfigHandler.paleMossClump_Blocks_DEFAULT,
        () -> ConfigHandler.paleMossClump_enabled,
        (val) -> ConfigHandler.paleMossClump_enabled = val,
        ConfigHandler.paleMossClump_enabled_DEFAULT,
        () -> ConfigHandler.maxPaleMossClump_onPlace,
        (val) -> ConfigHandler.maxPaleMossClump_onPlace = val,
        ConfigHandler.maxPaleMossClump_onPlace_DEFAULT,
        () -> ConfigHandler.maxPaleMossClump_onBreak,
        (val) -> ConfigHandler.maxPaleMossClump_onBreak = val,
        ConfigHandler.maxPaleMossClump_onBreak_DEFAULT,
        0.13f
    );
    public static final BlockParticleOverride DUST = new BlockParticleOverride(
        "dust",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.TINTED_DUST, blockState),
        () -> ConfigHandler.dust_Blocks,
        (val) -> ConfigHandler.dust_Blocks = val,
        ConfigHandler.dust_Blocks_DEFAULT,
        () -> ConfigHandler.dust_enabled,
        (val) -> ConfigHandler.dust_enabled = val,
        ConfigHandler.dust_enabled_DEFAULT,
        () -> ConfigHandler.maxDust_onPlace,
        (val) -> ConfigHandler.maxDust_onPlace = val,
        ConfigHandler.maxDust_onPlace_DEFAULT,
        () -> ConfigHandler.maxDust_onBreak,
        (val) -> ConfigHandler.maxDust_onBreak = val,
        ConfigHandler.maxDust_onBreak_DEFAULT,
        0.1f
    );
    public static final BlockParticleOverride REDSTONE_DUST = new BlockParticleOverride(
        "redstone_dust",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> TintedParticleOption.REDSTONE_DUST_OPTION,
        () -> ConfigHandler.redstoneDust_Blocks,
        (val) -> ConfigHandler.redstoneDust_Blocks = val,
        ConfigHandler.redstoneDust_Blocks_DEFAULT,
        () -> ConfigHandler.redstoneDust_enabled,
        (val) -> ConfigHandler.redstoneDust_enabled = val,
        ConfigHandler.redstoneDust_enabled_DEFAULT,
        () -> ConfigHandler.maxRedstoneDust_onPlace,
        (val) -> ConfigHandler.maxRedstoneDust_onPlace = val,
        ConfigHandler.maxRedstoneDust_onPlace_DEFAULT,
        () -> ConfigHandler.maxRedstoneDust_onBreak,
        (val) -> ConfigHandler.maxRedstoneDust_onBreak = val,
        ConfigHandler.maxRedstoneDust_onBreak_DEFAULT,
        0.15f
    );

    public static void registerOverrides() {
        BlockParticleOverride.addBlockParticleOverride(SNOW_POWDER);
        BlockParticleOverride.addBlockParticleOverride(CHERRY_LEAF);
        BlockParticleOverride.addBlockParticleOverride(AZALEA_LEAF);
        BlockParticleOverride.addBlockParticleOverride(FLOWERING_AZALEA_LEAF);
        BlockParticleOverride.addBlockParticleOverride(PALE_LEAF);
        BlockParticleOverride.addBlockParticleOverride(TINTED_LEAF);
        BlockParticleOverride.addBlockParticleOverride(GRASS_BLADE);
        BlockParticleOverride.addBlockParticleOverride(HEAVY_GRASS_BLADE);
        BlockParticleOverride.addBlockParticleOverride(MOSS_CLUMP);
        BlockParticleOverride.addBlockParticleOverride(PALE_MOSS_CLUMP);
        BlockParticleOverride.addBlockParticleOverride(DUST);
        BlockParticleOverride.addBlockParticleOverride(REDSTONE_DUST);
    }
}
