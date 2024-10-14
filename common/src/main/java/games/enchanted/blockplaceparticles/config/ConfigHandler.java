package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.config.adapters.FluidTypeAdapter;
import games.enchanted.blockplaceparticles.config.type.ResourceLocationAndColour;
import games.enchanted.blockplaceparticles.platform.Services;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.awt.*;
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

    public static final ResourceLocationAndColour testBlockAndColour_DEFAULT = new ResourceLocationAndColour(ResourceLocation.withDefaultNamespace("ice"), Color.CYAN);
    public static ResourceLocationAndColour testBlockAndColour = testBlockAndColour_DEFAULT;

    // Block Interaction Particles
    // underwater bubbles
    public static final int maxUnderwaterBubbles_onPlace_DEFAULT = 12;
    @SerialEntry
    public static int maxUnderwaterBubbles_onPlace = maxUnderwaterBubbles_onPlace_DEFAULT;

    public static final boolean underwaterBubbles_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean underwaterBubbles_onPlace = underwaterBubbles_onPlace_DEFAULT;

    // underwater bubbles
    public static final int maxUnderwaterBubbles_onBreak_DEFAULT = 6;
    @SerialEntry
    public static int maxUnderwaterBubbles_onBreak = maxUnderwaterBubbles_onBreak_DEFAULT;

    public static final boolean underwaterBubbles_onBreak_DEFAULT = true;
    @SerialEntry
    public static boolean underwaterBubbles_onBreak = underwaterBubbles_onBreak_DEFAULT;

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

    public static final List<ResourceLocation> snowflake_Blocks_DEFAULT = List.of(RegistryHelper.getLocationFromBlock(Blocks.SNOW), RegistryHelper.getLocationFromBlock(Blocks.SNOW_BLOCK), RegistryHelper.getLocationFromBlock(Blocks.POWDER_SNOW));
    @SerialEntry
    public static List<ResourceLocation> snowflake_Blocks = snowflake_Blocks_DEFAULT;

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

    public static final List<ResourceLocation> cherryPetal_Blocks_DEFAULT = List.of(RegistryHelper.getLocationFromBlock(Blocks.CHERRY_LEAVES), RegistryHelper.getLocationFromBlock(Blocks.CHERRY_SAPLING), RegistryHelper.getLocationFromBlock(Blocks.PINK_PETALS));
    @SerialEntry
    public static List<ResourceLocation> cherryPetal_Blocks = cherryPetal_Blocks_DEFAULT;

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

    public static final List<ResourceLocation> azaleaLeaf_Blocks_DEFAULT = List.of(RegistryHelper.getLocationFromBlock(Blocks.AZALEA), RegistryHelper.getLocationFromBlock(Blocks.AZALEA_LEAVES));
    @SerialEntry
    public static List<ResourceLocation> azaleaLeaf_Blocks = azaleaLeaf_Blocks_DEFAULT;

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

    public static final List<ResourceLocation> floweringAzaleaLeaf_Blocks_DEFAULT = List.of(RegistryHelper.getLocationFromBlock(Blocks.FLOWERING_AZALEA), RegistryHelper.getLocationFromBlock(Blocks.FLOWERING_AZALEA_LEAVES));
    @SerialEntry
    public static List<ResourceLocation> floweringAzaleaLeaf_Blocks = floweringAzaleaLeaf_Blocks_DEFAULT;

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

    public static final List<ResourceLocation> tintedLeaves_Blocks_DEFAULT = List.of(RegistryHelper.getLocationFromBlock(Blocks.JUNGLE_LEAVES), RegistryHelper.getLocationFromBlock(Blocks.OAK_LEAVES), RegistryHelper.getLocationFromBlock(Blocks.SPRUCE_LEAVES), RegistryHelper.getLocationFromBlock(Blocks.DARK_OAK_LEAVES), RegistryHelper.getLocationFromBlock(Blocks.ACACIA_LEAVES), RegistryHelper.getLocationFromBlock(Blocks.BIRCH_LEAVES), RegistryHelper.getLocationFromBlock(Blocks.MANGROVE_LEAVES));
    @SerialEntry
    public static List<ResourceLocation> tintedLeaves_Blocks = tintedLeaves_Blocks_DEFAULT;

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

    // Block Ambient Particles
    // campfire sparks
    public static final int campfireSpark_spawnChance_DEFAULT = 20;
    @SerialEntry
    public static int campfireSpark_spawnChance = campfireSpark_spawnChance_DEFAULT;

    public static final boolean campfireSpark_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean campfireSpark_enabled = campfireSpark_enabled_DEFAULT;

    public static final int fireSpark_spawnChance_DEFAULT = 25;
    @SerialEntry
    public static int fireSpark_spawnChance = fireSpark_spawnChance_DEFAULT;

    public static final boolean fireSpark_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean fireSpark_enabled = fireSpark_enabled_DEFAULT;

    // Entity Particles
    // travelling minecarts
    public static final int minecart_spawnChance_DEFAULT = 50;
    @SerialEntry
    public static int minecart_spawnChance = minecart_spawnChance_DEFAULT;

    public static final boolean minecart_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean minecart_enabled = minecart_enabled_DEFAULT;

    public static final boolean minecart_onlyWithPassenger_DEFAULT = true;
    @SerialEntry
    public static boolean minecart_onlyWithPassenger = minecart_onlyWithPassenger_DEFAULT;

    // Fluid Placement Particles
    // water
    public static final List<Fluid> tintedWaterSplash_fluids_DEFAULT = List.of(Fluids.WATER, Fluids.FLOWING_WATER);
    @SerialEntry
    public static List<Fluid> tintedWaterSplash_fluids = tintedWaterSplash_fluids_DEFAULT;

    public static final int maxTintedWaterSplash_onPlace_DEFAULT = 12;
    @SerialEntry
    public static int maxTintedWaterSplash_onPlace = maxTintedWaterSplash_onPlace_DEFAULT;

    public static final boolean tintedWaterSplash_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean tintedWaterSplash_onPlace = tintedWaterSplash_onPlace_DEFAULT;

    // lava
    public static final List<Fluid> lavaSplash_fluids_DEFAULT = List.of(Fluids.LAVA, Fluids.FLOWING_LAVA);
    @SerialEntry
    public static List<Fluid> lavaSplash_fluids = lavaSplash_fluids_DEFAULT;

    public static final int maxLavaSplash_onPlace_DEFAULT = 7;
    @SerialEntry
    public static int maxLavaSplash_onPlace = maxLavaSplash_onPlace_DEFAULT;

    public static final boolean lavaSplash_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean lavaSplash_onPlace = lavaSplash_onPlace_DEFAULT;

    // generic
    public static final List<Fluid> genericSplash_fluids_DEFAULT = List.of();
    @SerialEntry
    public static List<Fluid> genericSplash_fluids = genericSplash_fluids_DEFAULT;

    public static final int maxGenericSplash_onPlace_DEFAULT = 10;
    @SerialEntry
    public static int maxGenericSplash_onPlace = maxGenericSplash_onPlace_DEFAULT;

    public static final boolean genericSplash_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean genericSplash_onPlace = genericSplash_onPlace_DEFAULT;

    // Item Interaction Particles
    // flint and steel sparks
    public static final int flintAndSteelSpark_intensity_DEFAULT = 5;
    @SerialEntry
    public static int flintAndSteelSpark_intensity = flintAndSteelSpark_intensity_DEFAULT;

    public static final int maxFlintAndSteelSpark_onUse_DEFAULT = 12;
    @SerialEntry
    public static int maxFlintAndSteelSpark_onUse = maxFlintAndSteelSpark_onUse_DEFAULT;

    public static final boolean flintAndSteelSpark_onUse_DEFAULT = true;
    @SerialEntry
    public static boolean flintAndSteelSpark_onUse = flintAndSteelSpark_onUse_DEFAULT;

    // axe strip
    public static final int maxAxeStrip_onUse_DEFAULT = 12;
    @SerialEntry
    public static int maxAxeStrip_onUse = maxAxeStrip_onUse_DEFAULT;

    public static final boolean axeStrip_onUse_DEFAULT = true;
    @SerialEntry
    public static boolean axeStrip_onUse = axeStrip_onUse_DEFAULT;

    // hoe till
    public static final int maxHoeTill_onUse_DEFAULT = 12;
    @SerialEntry
    public static int maxHoeTill_onUse = maxHoeTill_onUse_DEFAULT;

    public static final boolean hoeTill_onUse_DEFAULT = true;
    @SerialEntry
    public static boolean hoeTill_onUse = hoeTill_onUse_DEFAULT;

    // shovel flatten
    public static final int maxShovelFlatten_onUse_DEFAULT = 12;
    @SerialEntry
    public static int maxShovelFlatten_onUse = maxShovelFlatten_onUse_DEFAULT;

    public static final boolean shovelFlatten_onUse_DEFAULT = true;
    @SerialEntry
    public static boolean shovelFlatten_onUse = shovelFlatten_onUse_DEFAULT;
}
