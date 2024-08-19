package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.nio.file.Path;
import java.util.List;

public class ConfigHandler {
    public static final Path CONFIG_PATH = Services.PLATFORM.getConfigPath().resolve(ParticleInteractionsMod.MOD_ID + "_config.json");

    public static final ConfigClassHandler<ConfigHandler> HANDLER = ConfigClassHandler.createBuilder(ConfigHandler.class)
        .id(ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "config"))
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
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
    public static final int maxSnowflakePlaceParticles_DEFAULT = 2;
    @SerialEntry
    public static int maxSnowflakePlaceParticles = maxSnowflakePlaceParticles_DEFAULT;

    public static final boolean snowParticleOnBlockPlace_DEFAULT = true;
    @SerialEntry
    public static boolean snowParticleOnBlockPlace = snowParticleOnBlockPlace_DEFAULT;

    public static final int maxSnowflakeBreakParticles_DEFAULT = 3;
    @SerialEntry
    public static int maxSnowflakeBreakParticles = maxSnowflakeBreakParticles_DEFAULT;

    public static final boolean snowParticleOnBlockBreak_DEFAULT = true;
    @SerialEntry
    public static boolean snowParticleOnBlockBreak = snowParticleOnBlockBreak_DEFAULT;

    public static final List<BlockItem> snowParticleBlockItems_DEFAULT = List.of((BlockItem) Items.SNOW, (BlockItem) Items.SNOW_BLOCK, (BlockItem) Items.POWDER_SNOW_BUCKET);
    @SerialEntry
    public static List<BlockItem> snowParticleBlockItems = snowParticleBlockItems_DEFAULT;

    // cherry petal particle
    public static final boolean cherryPetalParticleOnBlockPlace_DEFAULT = true;
    @SerialEntry
    public static boolean cherryPetalParticleOnBlockPlace = cherryPetalParticleOnBlockPlace_DEFAULT;

    public static final boolean cherryPetalParticleOnBlockBreak_DEFAULT = true;
    @SerialEntry
    public static boolean cherryPetalParticleOnBlockBreak = cherryPetalParticleOnBlockBreak_DEFAULT;

    public static final List<BlockItem> cherryPetalParticleBlockItems_DEFAULT = List.of((BlockItem) Items.CHERRY_LEAVES, (BlockItem) Items.CHERRY_SAPLING, (BlockItem) Items.PINK_PETALS);
    @SerialEntry
    public static List<BlockItem> cherryPetalParticleBlockItems = cherryPetalParticleBlockItems_DEFAULT;

    // azalea leaf particle
    public static final boolean azaleaLeafParticleOnBlockPlace_DEFAULT = true;
    @SerialEntry
    public static boolean azaleaLeafParticleOnBlockPlace = azaleaLeafParticleOnBlockPlace_DEFAULT;

    public static final boolean azaleaLeafParticleOnBlockBreak_DEFAULT = true;
    @SerialEntry
    public static boolean azaleaLeafParticleOnBlockBreak = azaleaLeafParticleOnBlockBreak_DEFAULT;

    public static final List<BlockItem> azaleaLeafParticleBlockItems_DEFAULT = List.of((BlockItem) Items.AZALEA, (BlockItem) Items.AZALEA_LEAVES);
    @SerialEntry
    public static List<BlockItem> azaleaLeafParticleBlockItems = azaleaLeafParticleBlockItems_DEFAULT;

    // flowering azalea leaf particle
    public static final boolean floweringAzaleaLeafParticleOnBlockPlace_DEFAULT = true;
    @SerialEntry
    public static boolean floweringAzaleaLeafParticleOnBlockPlace = floweringAzaleaLeafParticleOnBlockPlace_DEFAULT;

    public static final boolean floweringAzaleaLeafParticleOnBlockBreak_DEFAULT = true;
    @SerialEntry
    public static boolean floweringAzaleaLeafParticleOnBlockBreak = floweringAzaleaLeafParticleOnBlockBreak_DEFAULT;

    public static final List<BlockItem> floweringAzaleaLeafParticleBlockItems_DEFAULT = List.of((BlockItem) Items.FLOWERING_AZALEA, (BlockItem) Items.FLOWERING_AZALEA_LEAVES);
    @SerialEntry
    public static List<BlockItem> floweringAzaleaLeafParticleBlockItems = floweringAzaleaLeafParticleBlockItems_DEFAULT;

    // biome tinted leaf particle
    public static final boolean biomeTintedLeafParticleOnBlockPlace_DEFAULT = true;
    @SerialEntry
    public static boolean biomeTintedLeafParticleOnBlockPlace = biomeTintedLeafParticleOnBlockPlace_DEFAULT;

    public static final boolean biomeTintedLeafParticleOnBlockBreak_DEFAULT = true;
    @SerialEntry
    public static boolean biomeTintedLeafParticleOnBlockBreak = biomeTintedLeafParticleOnBlockBreak_DEFAULT;

    public static final List<BlockItem> biomeTintedLeafParticleBlockItems_DEFAULT = List.of((BlockItem) Items.JUNGLE_LEAVES, (BlockItem) Items.OAK_LEAVES, (BlockItem) Items.SPRUCE_LEAVES, (BlockItem) Items.DARK_OAK_LEAVES, (BlockItem) Items.ACACIA_LEAVES, (BlockItem) Items.BIRCH_LEAVES, (BlockItem) Items.MANGROVE_LEAVES);
    @SerialEntry
    public static List<BlockItem> biomeTintedLeafParticleBlockItems = biomeTintedLeafParticleBlockItems_DEFAULT;

    // default block particle
    public static final int maxBlockPlaceParticles_DEFAULT = 2;
    @SerialEntry
    public static int maxBlockPlaceParticles = maxBlockPlaceParticles_DEFAULT;

    public static final boolean blockParticleOnBlockPlace_DEFAULT = true;
    @SerialEntry
    public static boolean blockParticleOnBlockPlace = blockParticleOnBlockPlace_DEFAULT;

    public static final int maxBlockBreakParticles_DEFAULT = 4;
    @SerialEntry
    public static int maxBlockBreakParticles = maxBlockBreakParticles_DEFAULT;

    public static final boolean blockParticleOnBlockBreak_DEFAULT = true;
    @SerialEntry
    public static boolean blockParticleOnBlockBreak = blockParticleOnBlockBreak_DEFAULT;

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
