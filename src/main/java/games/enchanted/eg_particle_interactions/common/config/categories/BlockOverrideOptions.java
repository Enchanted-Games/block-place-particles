package games.enchanted.eg_particle_interactions.common.config.categories;

import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.option.BlockOrTagLocationListOption;
import games.enchanted.eg_particle_interactions.common.config.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config.option.IntOption;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockOverrideOptions {
    public static final ConfigOption<Boolean> DISABLE_ALL_PLACING_PARTICLES = registerOption(
        new BoolOption(false, "disable_all_block_placing_particles")
    );
    public static final ConfigOption<Boolean> DISABLE_ALL_BREAKING_PARTICLES = registerOption(
        new BoolOption(false, "disable_all_block_breaking_particles")
    );

    public static final BlockParticleOptionSet VANILLA_BLOCK_PARTICLE = BlockParticleOptionSet.register(
        "vanilla_block_particles",
        2,
        4
    );

    public static final BlockParticleOptionSet SNOWFLAKE_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "snowflake_block_particles",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SNOW),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SNOW_BLOCK),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.POWDER_SNOW)
        )
    );

    public static final BlockParticleOptionSet FIREFLY_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "firefly_block_particles",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.FIREFLY_BUSH)
        )
    );

    public static final BlockParticleOptionSet CHERRY_PETAL_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "cherry_petal_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.CHERRY_LEAVES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.CHERRY_SAPLING),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.PINK_PETALS)
        )
    );

    public static final BlockParticleOptionSet AZALEA_LEAVES_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "azalea_leaves_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.AZALEA),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.AZALEA_LEAVES)
        )
    );

    public static final BlockParticleOptionSet FLOWERING_AZALEA_LEAVES_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "flowering_azalea_leaves_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.FLOWERING_AZALEA),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.FLOWERING_AZALEA_LEAVES)
        )
    );

    public static final BlockParticleOptionSet PALE_LEAVES_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "pale_leaves_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.PALE_OAK_LEAVES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.PALE_OAK_SAPLING)
        )
    );

    public static final BlockParticleOptionSet GENERIC_LEAVES_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "generic_leaves_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.LEAF_LITTER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.JUNGLE_LEAVES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.OAK_LEAVES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.DARK_OAK_LEAVES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.ACACIA_LEAVES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.MANGROVE_LEAVES)
        )
    );

    public static final BlockParticleOptionSet PINE_LEAVES_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "pine_leaves_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.BIRCH_LEAVES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SPRUCE_LEAVES)
        )
    );

    public static final BlockParticleOptionSet FLOWER_PETAL_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "flower_petal_block_particle",
        3,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.TWISTING_VINES_PLANT),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.TWISTING_VINES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.WEEPING_VINES_PLANT),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.WEEPING_VINES),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.BUSH),
            new BlockOrTagLocation(BlockTags.CAVE_VINES.location(), true),
            new BlockOrTagLocation(BlockTags.FLOWERS.location(), true),
            new BlockOrTagLocation(Identifier.fromNamespaceAndPath("c", "flowers"), true),
            new BlockOrTagLocation(Identifier.fromNamespaceAndPath("c", "flowers/small"), true),
            new BlockOrTagLocation(Identifier.fromNamespaceAndPath("c", "flowers/tall"), true)
        )
    );

    public static final BlockParticleOptionSet GRASS_BLADE_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "grass_blade_block_particle",
        2,
        4,
        List.of(
            new BlockOrTagLocation(BlockTags.CROPS.location(), true),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.VINE),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SHORT_GRASS),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.TALL_GRASS),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.FERN),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.LARGE_FERN),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SWEET_BERRY_BUSH),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.DEAD_BUSH),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SEAGRASS),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.TALL_SEAGRASS),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.HAY_BLOCK),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.DIRT_PATH),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.GRASS_BLOCK),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.GLOW_LICHEN),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SHORT_DRY_GRASS),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.TALL_DRY_GRASS),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.DEAD_BUSH)
        )
    );

    public static final BlockParticleOptionSet HEAVY_GRASS_BLADE_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "heavy_grass_blade_block_particle",
        2,
        4,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.HANGING_ROOTS),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.PALE_HANGING_MOSS),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COBWEB),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.TRIPWIRE)
        )
    );

    public static final BlockParticleOptionSet MOSS_CLUMP_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "moss_clump_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.MOSS_CARPET),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.MOSS_BLOCK)
        )
    );

    public static final BlockParticleOptionSet PALE_MOSS_CLUMP_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "pale_moss_clump_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.PALE_MOSS_CARPET),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.PALE_MOSS_BLOCK)
        )
    );

    public static final BlockParticleOptionSet DUST_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "dust_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SAND),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SUSPICIOUS_SAND),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.GRAVEL),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SUSPICIOUS_GRAVEL),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.RED_SAND),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SOUL_SAND),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SOUL_SOIL),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.WHITE_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.LIGHT_GRAY_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.GRAY_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.BROWN_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.RED_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.ORANGE_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.YELLOW_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.LIME_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.GREEN_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.CYAN_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.LIGHT_BLUE_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.BLUE_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.PURPLE_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.MAGENTA_CONCRETE_POWDER),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.PINK_CONCRETE_POWDER)
        )
    );

    public static final BlockParticleOptionSet REDSTONE_DUST_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "redstone_dust_block_particle",
        3,
        4,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.REDSTONE_TORCH),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.REDSTONE_WALL_TORCH),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.REDSTONE_WIRE),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.REDSTONE_BLOCK)
        )
    );

    public static final BlockParticleOptionSet BLOCK_SHATTER_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "block_shatter_block_particle",
        3,
        4,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.NETHER_PORTAL)
        )
    );

    public static final BlockParticleOptionSet CHAIN_SNAP_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "chain_snap_block_particle",
        2,
        6,
        List.of(
            //? if minecraft: <= 1.21.8 {
            /*RegistryHelpers.getBlockLocationFromBlock(Blocks.CHAIN),
             *///?} else {
            RegistryHelpers.getBlockLocationFromBlock(Blocks.IRON_CHAIN),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COPPER_CHAIN.unaffected()),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COPPER_CHAIN.waxed()),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COPPER_CHAIN.exposed()),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COPPER_CHAIN.waxedExposed()),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COPPER_CHAIN.weathered()),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COPPER_CHAIN.waxedWeathered()),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COPPER_CHAIN.oxidized()),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.COPPER_CHAIN.waxedOxidized()),
            //?}
            new BlockOrTagLocation(Identifier.fromNamespaceAndPath("c", "chains"), true)
        )
    );

    public static final BlockParticleOptionSet SUGAR_CANE_PARTICLE_OVERRIDE = BlockParticleOptionSet.register(
        "sugar_cane_block_particle",
        2,
        3,
        List.of(
            RegistryHelpers.getBlockLocationFromBlock(Blocks.SUGAR_CANE),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.BAMBOO),
            RegistryHelpers.getBlockLocationFromBlock(Blocks.MANGROVE_ROOTS)
        )
    );

    public record BlockParticleOptionSet(ConfigOption<Boolean> enabledOption, ConfigOption<Integer> maxOnPlaceOption, ConfigOption<Integer> maxOnBreakOption, @Nullable ConfigOption<List<BlockOrTagLocation>> blocksOption) {
        static BlockParticleOptionSet register(String id, int maxOnPlaceDefault, int maxOnBreakDefault, List<BlockOrTagLocation> blocksDefault) {
            return new BlockParticleOptionSet(
                registerOption(new BoolOption(true, id + "_enabled")),
                registerOption(new IntOption(maxOnPlaceDefault, id + "_max_on_place")),
                registerOption(new IntOption(maxOnBreakDefault, id + "_max_on_break")),
                registerOption(new BlockOrTagLocationListOption(blocksDefault, id + "_blocks"))
            );
        }

        static BlockParticleOptionSet register(String id, int maxOnPlaceDefault, int maxOnBreakDefault) {
            return new BlockParticleOptionSet(
                registerOption(new BoolOption(true, id + "_enabled")),
                registerOption(new IntOption(maxOnPlaceDefault, id + "_max_on_place")),
                registerOption(new IntOption(maxOnBreakDefault, id + "_max_on_break")),
                null
            );
        }
    }

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.BLOCK_OVERRIDE, option);
    }

    public static void init() {}
}
