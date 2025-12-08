package games.enchanted.eg_particle_interactions.common.particle.overrides;

import games.enchanted.eg_particle_interactions.common.config.categories.BlockOverrideOptions;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import games.enchanted.eg_particle_interactions.common.util.BiomeHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class BlockParticleOverrides {
    public static final BlockParticleOverride SNOW_POWDER = new BlockParticleOverride(
        "snowflake",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> {
            if(BiomeHelpers.isWarmBiomeOrDimension(level, blockPos) && BlockOverrideOptions.SNOWFLAKE_PARTICLE_STEAM_IN_WARM_PLACES.getValue()) {
                return level.random.nextInt(5) == 0 ? ParticleTypes.POOF : ModParticleTypes.SNOWFLAKE;
            }
            return ModParticleTypes.SNOWFLAKE;
        },
        BlockOverrideOptions.SNOWFLAKE_PARTICLE_OVERRIDE,
        0.15f,
        List.of(new BlockParticleOverride.OptionToggle(ConfigTranslation.SPAWN_SNOWFLAKE_STEAM_PARTICLES, BlockOverrideOptions.SNOWFLAKE_PARTICLE_STEAM_IN_WARM_PLACES))
    );

    public static final BlockParticleOverride CHERRY_LEAF = new BlockParticleOverride(
        "cherry_petal",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.FALLING_CHERRY_PETAL,
        BlockOverrideOptions.CHERRY_PETAL_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride AZALEA_LEAF = new BlockParticleOverride(
        "azalea_leaf",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.FALLING_AZALEA_LEAF,
        BlockOverrideOptions.AZALEA_LEAVES_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride FLOWERING_AZALEA_LEAF = new BlockParticleOverride(
        "flowering_azalea_leaf",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.FALLING_FLOWERING_AZALEA_LEAF,
        BlockOverrideOptions.FLOWERING_AZALEA_LEAVES_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride PALE_LEAF = new BlockParticleOverride(
        "pale_leaf",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.FALLING_PALE_OAK_LEAF,
        BlockOverrideOptions.PALE_LEAVES_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride TINTED_PINE_LEAF = new BlockParticleOverride(
        "biome_pine_leaf",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.FALLING_TINTED_PINE_LEAF, blockState),
        BlockOverrideOptions.PINE_LEAVES_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride TINTED_LEAF = new BlockParticleOverride(
        "biome_leaf",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.FALLING_TINTED_LEAF, blockState),
        BlockOverrideOptions.GENERIC_LEAVES_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride FLOWER_PETAL = new BlockParticleOverride(
        "flower_petal",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.FLOWER_PETAL, blockState),
        BlockOverrideOptions.FLOWER_PETAL_PARTICLE_OVERRIDE,
        0.18f,
        List.of()
    );

    public static final BlockParticleOverride GRASS_BLADE = new BlockParticleOverride(
        "grass_blade",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> {
            boolean spawnDirt = false;
            boolean spawnFirefly =
                level.getMaxLocalRawBrightness(blockPos) <= 13 &&
                BiomeHelpers.isSwampyBiome(level, blockPos) &&
                level.random.nextFloat() > (overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_BROKEN || overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_PLACED ? 0.9f : 0.6f);

            if(
                (blockState.getBlock() == Blocks.GRASS_BLOCK || blockState.getBlock() == Blocks.DIRT_PATH) &&
                (overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_CRACK || overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_PLACED || overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_BROKEN || overrideOrigin == BlockParticleOverride.ORIGIN_ITEM_PARTICLE_OVERRIDDEN)
            ) {
                // occasionally spawn dirt particles if a grass block is placed or broken
                spawnDirt = level.random.nextFloat() > 0.7;
            }

            if(spawnDirt && BlockOverrideOptions.GRASS_BLADE_PARTICLE_DIRT_FOR_GRASS_BLOCKS.getValue()) {
                return new BlockParticleOption(overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_CRACK ? ModParticleTypes.BLOCK_CRACK : ModParticleTypes.BLOCK_HIGH_VELOCITY, Blocks.DIRT.defaultBlockState());
            }
            if(spawnFirefly && BlockOverrideOptions.GRASS_BLADE_PARTICLE_FIREFLY_IN_SWAMPS.getValue()) {
                return ParticleTypes.FIREFLY;
            }
            return new BlockParticleOption(ModParticleTypes.GRASS_BLADE, blockState);
        },
        (int overrideOrigin) -> overrideOrigin != BlockParticleOverride.ORIGIN_ITEM_PARTICLE_OVERRIDDEN,
        BlockOverrideOptions.GRASS_BLADE_PARTICLE_OVERRIDE,
        0.13f,
        List.of(
            new BlockParticleOverride.OptionToggle(ConfigTranslation.GRASS_BLADE_SPAWN_GRASS_BLOCK_DIRT_PARTICLES, BlockOverrideOptions.GRASS_BLADE_PARTICLE_DIRT_FOR_GRASS_BLOCKS),
            new BlockParticleOverride.OptionToggle(ConfigTranslation.GRASS_BLADE_SPAWN_FIREFLY_IN_SWAMP, BlockOverrideOptions.GRASS_BLADE_PARTICLE_FIREFLY_IN_SWAMPS)
        )
    );

    public static final BlockParticleOverride HEAVY_GRASS_BLADE = new BlockParticleOverride(
        "heavy_grass_blade",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.HEAVY_GRASS_BLADE, blockState),
        (int overrideOrigin) -> overrideOrigin != BlockParticleOverride.ORIGIN_ITEM_PARTICLE_OVERRIDDEN,
        BlockOverrideOptions.HEAVY_GRASS_BLADE_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride FIREFLY = new BlockParticleOverride(
        "firefly",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> {
            boolean firefly = level.random.nextFloat() > 0.6;
            if(overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_WALKED_THROUGH) {
                firefly = true;
            }
            return firefly ? ParticleTypes.FIREFLY : GRASS_BLADE.getParticleOptionForState(blockState, level, blockPos, overrideOrigin);
        },
        BlockOverrideOptions.FIREFLY_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride MOSS_CLUMP = new BlockParticleOverride(
        "moss_clump",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.MOSS_CLUMP,
        BlockOverrideOptions.MOSS_CLUMP_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride PALE_MOSS_CLUMP = new BlockParticleOverride(
        "pale_moss_clump",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> ModParticleTypes.PALE_MOSS_CLUMP,
        BlockOverrideOptions.PALE_MOSS_CLUMP_PARTICLE_OVERRIDE,
        0.13f,
        List.of()
    );

    public static final BlockParticleOverride DUST = new BlockParticleOverride(
        "dust",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.TINTED_DUST, blockState),
        BlockOverrideOptions.DUST_PARTICLE_OVERRIDE,
        0.1f,
        List.of()
    );

    public static final BlockParticleOverride REDSTONE_DUST = new BlockParticleOverride(
        "redstone_dust",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.REDSTONE_DUST, blockState),
        BlockOverrideOptions.REDSTONE_DUST_PARTICLE_OVERRIDE,
        0.1f,
        List.of()
    );

    public static final BlockParticleOverride NETHER_PORTAL_SHATTER = new BlockParticleOverride(
        "nether_portal_shatter",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.BLOCK_SHATTER, blockState),
        BlockOverrideOptions.BLOCK_SHATTER_PARTICLE_OVERRIDE,
        0.2f,
        List.of()
    );

    public static final BlockParticleOverride CHAIN_SNAP = new BlockParticleOverride(
        "chain_snap",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> {
            if(overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_BROKEN || overrideOrigin == BlockParticleOverride.ORIGIN_BLOCK_CRACK) {
                return level.random.nextFloat() > 0.9 ? ModParticleTypes.SPARK_FLASH : new BlockParticleOption(ModParticleTypes.CHAIN_SNAP, blockState);
            }
            return new BlockParticleOption(ModParticleTypes.CHAIN_SNAP, blockState);
        },
        BlockOverrideOptions.CHAIN_SNAP_PARTICLE_OVERRIDE,
        0.22f,
        List.of()
    );

    public static final BlockParticleOverride SUGAR_CANE = new BlockParticleOverride(
        "sugar_cane",
        "tinted_or_random_pixel",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ModParticleTypes.SUGAR_CANE, blockState),
        BlockOverrideOptions.SUGAR_CANE_PARTICLE_OVERRIDE,
        0.19f,
        List.of()
    );


    public static void registerOverrides() {
        BlockParticleOverride.addBlockParticleOverride(SNOW_POWDER);
        BlockParticleOverride.addBlockParticleOverride(FIREFLY);
        BlockParticleOverride.addBlockParticleOverride(CHERRY_LEAF);
        BlockParticleOverride.addBlockParticleOverride(AZALEA_LEAF);
        BlockParticleOverride.addBlockParticleOverride(FLOWERING_AZALEA_LEAF);
        BlockParticleOverride.addBlockParticleOverride(PALE_LEAF);
        BlockParticleOverride.addBlockParticleOverride(TINTED_PINE_LEAF);
        BlockParticleOverride.addBlockParticleOverride(TINTED_LEAF);
        BlockParticleOverride.addBlockParticleOverride(FLOWER_PETAL);
        BlockParticleOverride.addBlockParticleOverride(GRASS_BLADE);
        BlockParticleOverride.addBlockParticleOverride(HEAVY_GRASS_BLADE);
        BlockParticleOverride.addBlockParticleOverride(MOSS_CLUMP);
        BlockParticleOverride.addBlockParticleOverride(PALE_MOSS_CLUMP);
        BlockParticleOverride.addBlockParticleOverride(DUST);
        BlockParticleOverride.addBlockParticleOverride(REDSTONE_DUST);
        BlockParticleOverride.addBlockParticleOverride(NETHER_PORTAL_SHATTER);
        BlockParticleOverride.addBlockParticleOverride(CHAIN_SNAP);
        BlockParticleOverride.addBlockParticleOverride(SUGAR_CANE);
    }
}
