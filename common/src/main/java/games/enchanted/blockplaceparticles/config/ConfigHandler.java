package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.config.adapters.FluidTypeAdapter;
import games.enchanted.blockplaceparticles.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.nio.file.Path;
import java.util.List;

public class ConfigHandler {
    public static final Path CONFIG_PATH = Services.PLATFORM.getConfigPath().resolve(ParticleInteractionsMod.MOD_ID + "_config.json");

    public static final ConfigClassHandler<ConfigHandler> HANDLER = ConfigClassHandler.createBuilder(ConfigHandler.class)
        .id(ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "config"))
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .appendGsonBuilder(builder -> builder.registerTypeAdapter(Fluid.class, new FluidTypeAdapter()))
            .setPath(CONFIG_PATH)
            .build())
        .build();

    public static void load() {
        HANDLER.load();
    }

    public static void save() {
        HANDLER.save();
    }

    public static ConfigHandler instance() {
        return HANDLER.instance();
    }

    // Block Interaction Particles
    // snowflake particle
    public static final int maxSnowflakes_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxSnowflakes_onPlace = maxSnowflakes_onPlace_DEFAULT;

    public static final boolean snowflake_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean snowflake_onPlace = snowflake_onPlace_DEFAULT;

    public static final int maxSnowflakes_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxSnowflakes_onBreak = maxSnowflakes_onBreak_DEFAULT;

    public static final boolean snowflake_onBreak_DEFAULT = true;
    @SerialEntry
    public static boolean snowflake_onBreak = snowflake_onBreak_DEFAULT;

    public static final List<BlockItem> snowflake_BlockItems_DEFAULT = List.of((BlockItem) Items.SNOW, (BlockItem) Items.SNOW_BLOCK, (BlockItem) Items.POWDER_SNOW_BUCKET);
    @SerialEntry
    public static List<BlockItem> snowflake_BlockItems = snowflake_BlockItems_DEFAULT;

    // cherry petal particle
    public static final int maxCherryPetals_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxCherryPetals_onPlace = maxCherryPetals_onPlace_DEFAULT;

    public static final boolean cherryPetal_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean cherryPetal_onPlace = cherryPetal_onPlace_DEFAULT;

    public static final int maxCherryPetals_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxCherryPetals_onBreak = maxCherryPetals_onBreak_DEFAULT;

    public static final boolean cherryPetal_onBreak_DEFAULT = true;
    @SerialEntry
    public static boolean cherryPetal_onBreak = cherryPetal_onBreak_DEFAULT;

    public static final List<BlockItem> cherryPetal_BlockItems_DEFAULT = List.of((BlockItem) Items.CHERRY_LEAVES, (BlockItem) Items.CHERRY_SAPLING, (BlockItem) Items.PINK_PETALS);
    @SerialEntry
    public static List<BlockItem> cherryPetal_BlockItems = cherryPetal_BlockItems_DEFAULT;

    // azalea leaf particle
    public static final int maxAzaleaLeaves_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxAzaleaLeaves_onPlace = maxAzaleaLeaves_onPlace_DEFAULT;

    public static final boolean azaleaLeaf_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean azaleaLeaf_onPlace = azaleaLeaf_onPlace_DEFAULT;

    public static final int maxAzaleaLeaves_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxAzaleaLeaves_onBreak = maxAzaleaLeaves_onBreak_DEFAULT;

    public static final boolean azaleaLeaf_onBreak_DEFAULT = true;
    @SerialEntry
    public static boolean azaleaLeaf_onBreak = azaleaLeaf_onBreak_DEFAULT;

    public static final List<BlockItem> azaleaLeaf_BlockItems_DEFAULT = List.of((BlockItem) Items.AZALEA, (BlockItem) Items.AZALEA_LEAVES);
    @SerialEntry
    public static List<BlockItem> azaleaLeaf_BlockItems = azaleaLeaf_BlockItems_DEFAULT;

    // flowering azalea leaf particle
    public static final int maxFloweringAzaleaLeaves_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxFloweringAzaleaLeaves_onPlace = maxFloweringAzaleaLeaves_onPlace_DEFAULT;

    public static final boolean floweringAzaleaLeaf_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean floweringAzaleaLeaf_onPlace = floweringAzaleaLeaf_onPlace_DEFAULT;

    public static final int maxFloweringAzaleaLeaves_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxFloweringAzaleaLeaves_onBreak = maxFloweringAzaleaLeaves_onBreak_DEFAULT;

    public static final boolean floweringAzaleaLeaf_onBreak_DEFAULT = true;
    @SerialEntry
    public static boolean floweringAzaleaLeaf_onBreak = floweringAzaleaLeaf_onBreak_DEFAULT;

    public static final List<BlockItem> floweringAzaleaLeaf_BlockItems_DEFAULT = List.of((BlockItem) Items.FLOWERING_AZALEA, (BlockItem) Items.FLOWERING_AZALEA_LEAVES);
    @SerialEntry
    public static List<BlockItem> floweringAzaleaLeaf_BlockItems = floweringAzaleaLeaf_BlockItems_DEFAULT;

    // biome tinted leaf particle
    public static final int maxTintedLeaves_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxTintedLeaves_onPlace = maxTintedLeaves_onPlace_DEFAULT;

    public static final boolean tintedLeaves_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean tintedLeaves_onPlace = tintedLeaves_onPlace_DEFAULT;

    public static final int maxTintedLeaves_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxTintedLeaves_onBreak = maxTintedLeaves_onBreak_DEFAULT;

    public static final boolean tintedLeaves_onBreak_DEFAULT = true;
    @SerialEntry
    public static boolean tintedLeaves_onBreak = tintedLeaves_onBreak_DEFAULT;

    public static final List<BlockItem> tintedLeaves_BlockItems_DEFAULT = List.of((BlockItem) Items.JUNGLE_LEAVES, (BlockItem) Items.OAK_LEAVES, (BlockItem) Items.SPRUCE_LEAVES, (BlockItem) Items.DARK_OAK_LEAVES, (BlockItem) Items.ACACIA_LEAVES, (BlockItem) Items.BIRCH_LEAVES, (BlockItem) Items.MANGROVE_LEAVES);
    @SerialEntry
    public static List<BlockItem> tintedLeaves_BlockItems = tintedLeaves_BlockItems_DEFAULT;

    // default block particle
    public static final int maxBlock_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxBlock_onPlace = maxBlock_onPlace_DEFAULT;

    public static final boolean block_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean block_onPlace = block_onPlace_DEFAULT;

    public static final int maxBlock_onBreak_DEFAULT = 4;
    @SerialEntry
    public static int maxBlock_onBreak = maxBlock_onBreak_DEFAULT;

    public static final boolean block_onBreak_DEFAULT = true;
    @SerialEntry
    public static boolean block_onBreak = block_onBreak_DEFAULT;

    // Fluid Placement Particles
    // water
    public static final List<Fluid> waterSplash_fluids_DEFAULT = List.of(Fluids.WATER, Fluids.FLOWING_WATER);
    @SerialEntry
    public static List<Fluid> waterSplash_fluids = waterSplash_fluids_DEFAULT;

    public static final int maxWaterSplash_onPlace_DEFAULT = 10;
    @SerialEntry
    public static int maxWaterSplash_onPlace = maxWaterSplash_onPlace_DEFAULT;

    public static final boolean waterSplash_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean waterSplash_onPlace = waterSplash_onPlace_DEFAULT;

    // lava
    public static final List<Fluid> lavaSplash_fluids_DEFAULT = List.of(Fluids.LAVA, Fluids.FLOWING_LAVA);
    @SerialEntry
    public static List<Fluid> lavaSplash_fluids = lavaSplash_fluids_DEFAULT;

    public static final int maxLavaSplash_onPlace_DEFAULT = 10;
    @SerialEntry
    public static int maxLavaSplash_onPlace = maxLavaSplash_onPlace_DEFAULT;

    public static final boolean lavaSplash_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean lavaSplash_onPlace = lavaSplash_onPlace_DEFAULT;

    // lava

//    public static final List<BlockItem> blockParticleBlockItemsBlacklist_DEFAULT = List.of();
//    @SerialEntry
//    public static List<BlockItem> blockParticleBlockItemsBlacklist = blockParticleBlockItemsBlacklist_DEFAULT;
//
//    public static final boolean blockParticleBlockItemsAsWhitelist_DEFAULT = false;
//    @SerialEntry
//    public static boolean blockParticleBlockItemsAsWhitelist = blockParticleBlockItemsAsWhitelist_DEFAULT;

    @SerialEntry
    public static BlockItem testValue = (BlockItem) Items.ACACIA_FENCE;
    @SerialEntry
    public static Item testValueItem = Items.SPONGE;
}
