package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.config.adapters.FluidTypeAdapter;
import games.enchanted.blockplaceparticles.config.adapters.ResourceLocationAndColourTypeAdapter;
import games.enchanted.blockplaceparticles.config.adapters.ResourceLocationTypeAdapter;
import games.enchanted.blockplaceparticles.config.type.BrushParticleBehaviour;
import games.enchanted.blockplaceparticles.config.type.ResourceLocationAndColour;
import games.enchanted.blockplaceparticles.platform.Services;
import games.enchanted.blockplaceparticles.util.RegistryHelpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
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
            .appendGsonBuilder(builder -> builder.registerTypeAdapter(ResourceLocation.class, new ResourceLocationTypeAdapter()))
            .appendGsonBuilder(builder -> builder.registerTypeAdapter(ResourceLocationAndColour.class, new ResourceLocationAndColourTypeAdapter()))
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

    // General options
    // pixel consistent terrain particles
    public static final boolean general_pixelConsistentTerrainParticles_DEFAULT = true;
    @SerialEntry
    public static boolean general_pixelConsistentTerrainParticles = general_pixelConsistentTerrainParticles_DEFAULT;

    // particle z-fighting fix
    public static final boolean general_particleZFightingFix_DEFAULT = true;
    @SerialEntry
    public static boolean general_particleZFightingFix = general_particleZFightingFix_DEFAULT;

    // - performance
    // advanced particle physics
    public static final boolean general_extraParticlePhysicsEnabled_DEFAULT = true;
    @SerialEntry
    public static boolean general_extraParticlePhysicsEnabled = general_extraParticlePhysicsEnabled_DEFAULT;

    // sparks: spawn additional flash effects
    public static final boolean particle_sparks_additionalFlashEffects_DEFAULT = true;
    @SerialEntry
    public static boolean particle_sparks_additionalFlashEffects = particle_sparks_additionalFlashEffects_DEFAULT;

    // sparks: water evaporation
    public static final boolean particle_sparks_waterEvaporation_DEFAULT = true;
    @SerialEntry
    public static boolean particle_sparks_waterEvaporation = particle_sparks_waterEvaporation_DEFAULT;

    // dust: spawn specks
    public static final boolean particle_dust_additionalSpecks_DEFAULT = true;
    @SerialEntry
    public static boolean particle_dust_additionalSpecks = particle_dust_additionalSpecks_DEFAULT;

    // - debug
    // debug emitter bounds
    public static final boolean debug_showEmitterBounds_DEFAULT = false;
    public static boolean debug_showEmitterBounds = debug_showEmitterBounds_DEFAULT;

    // Block Interaction Particles
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

    public static final boolean snowflake_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean snowflake_enabled = snowflake_enabled_DEFAULT;

    public static final int maxSnowflakes_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxSnowflakes_onBreak = maxSnowflakes_onBreak_DEFAULT;

    public static final List<ResourceLocation> snowflake_Blocks_DEFAULT = List.of(RegistryHelpers.getLocationFromBlock(Blocks.SNOW), RegistryHelpers.getLocationFromBlock(Blocks.SNOW_BLOCK), RegistryHelpers.getLocationFromBlock(Blocks.POWDER_SNOW));
    @SerialEntry
    public static List<ResourceLocation> snowflake_Blocks = snowflake_Blocks_DEFAULT;

    // cherry petal particle
    public static final int maxCherryPetals_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxCherryPetals_onPlace = maxCherryPetals_onPlace_DEFAULT;

    public static final boolean cherryPetal_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean cherryPetal_enabled = cherryPetal_enabled_DEFAULT;

    public static final int maxCherryPetals_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxCherryPetals_onBreak = maxCherryPetals_onBreak_DEFAULT;

    public static final List<ResourceLocation> cherryPetal_Blocks_DEFAULT = List.of(RegistryHelpers.getLocationFromBlock(Blocks.CHERRY_LEAVES), RegistryHelpers.getLocationFromBlock(Blocks.CHERRY_SAPLING), RegistryHelpers.getLocationFromBlock(Blocks.PINK_PETALS));
    @SerialEntry
    public static List<ResourceLocation> cherryPetal_Blocks = cherryPetal_Blocks_DEFAULT;

    // azalea leaf particle
    public static final int maxAzaleaLeaves_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxAzaleaLeaves_onPlace = maxAzaleaLeaves_onPlace_DEFAULT;

    public static final boolean azaleaLeaf_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean azaleaLeaf_enabled = azaleaLeaf_enabled_DEFAULT;

    public static final int maxAzaleaLeaves_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxAzaleaLeaves_onBreak = maxAzaleaLeaves_onBreak_DEFAULT;

    public static final List<ResourceLocation> azaleaLeaf_Blocks_DEFAULT = List.of(RegistryHelpers.getLocationFromBlock(Blocks.AZALEA), RegistryHelpers.getLocationFromBlock(Blocks.AZALEA_LEAVES));
    @SerialEntry
    public static List<ResourceLocation> azaleaLeaf_Blocks = azaleaLeaf_Blocks_DEFAULT;

    // flowering azalea leaf particle
    public static final int maxFloweringAzaleaLeaves_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxFloweringAzaleaLeaves_onPlace = maxFloweringAzaleaLeaves_onPlace_DEFAULT;

    public static final boolean floweringAzaleaLeaf_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean floweringAzaleaLeaf_enabled = floweringAzaleaLeaf_enabled_DEFAULT;

    public static final int maxFloweringAzaleaLeaves_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxFloweringAzaleaLeaves_onBreak = maxFloweringAzaleaLeaves_onBreak_DEFAULT;

    public static final List<ResourceLocation> floweringAzaleaLeaf_Blocks_DEFAULT = List.of(RegistryHelpers.getLocationFromBlock(Blocks.FLOWERING_AZALEA), RegistryHelpers.getLocationFromBlock(Blocks.FLOWERING_AZALEA_LEAVES));
    @SerialEntry
    public static List<ResourceLocation> floweringAzaleaLeaf_Blocks = floweringAzaleaLeaf_Blocks_DEFAULT;

    // pale leaf particle
    public static final int maxPaleLeaves_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxPaleLeaves_onPlace = maxPaleLeaves_onPlace_DEFAULT;

    public static final boolean paleLeaf_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean paleLeaf_enabled = paleLeaf_enabled_DEFAULT;

    public static final int maxPaleLeaves_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxPaleLeaves_onBreak = maxPaleLeaves_onBreak_DEFAULT;

    public static final List<ResourceLocation> paleLeaf_Blocks_DEFAULT = List.of(RegistryHelpers.getLocationFromBlock(Blocks.PALE_OAK_LEAVES), RegistryHelpers.getLocationFromBlock(Blocks.PALE_OAK_SAPLING));
    @SerialEntry
    public static List<ResourceLocation> paleLeaf_Blocks = paleLeaf_Blocks_DEFAULT;

    // biome tinted leaf particle
    public static final int maxTintedLeaves_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxTintedLeaves_onPlace = maxTintedLeaves_onPlace_DEFAULT;

    public static final boolean tintedLeaves_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean tintedLeaves_enabled = tintedLeaves_enabled_DEFAULT;

    public static final int maxTintedLeaves_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxTintedLeaves_onBreak = maxTintedLeaves_onBreak_DEFAULT;

    public static final List<ResourceLocation> tintedLeaves_Blocks_DEFAULT = List.of(RegistryHelpers.getLocationFromBlock(Blocks.JUNGLE_LEAVES), RegistryHelpers.getLocationFromBlock(Blocks.OAK_LEAVES), RegistryHelpers.getLocationFromBlock(Blocks.DARK_OAK_LEAVES), RegistryHelpers.getLocationFromBlock(Blocks.ACACIA_LEAVES), RegistryHelpers.getLocationFromBlock(Blocks.MANGROVE_LEAVES));
    @SerialEntry
    public static List<ResourceLocation> tintedLeaves_Blocks = tintedLeaves_Blocks_DEFAULT;

    // biome tinted pine leaf particle
    public static final int maxTintedPineLeaves_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxTintedPineLeaves_onPlace = maxTintedPineLeaves_onPlace_DEFAULT;

    public static final boolean tintedPineLeaves_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean tintedPineLeaves_enabled = tintedPineLeaves_enabled_DEFAULT;

    public static final int maxTintedPineLeaves_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxTintedPineLeaves_onBreak = maxTintedPineLeaves_onBreak_DEFAULT;

    public static final List<ResourceLocation> tintedPineLeaves_Blocks_DEFAULT = List.of(RegistryHelpers.getLocationFromBlock(Blocks.BIRCH_LEAVES), RegistryHelpers.getLocationFromBlock(Blocks.SPRUCE_LEAVES));
    @SerialEntry
    public static List<ResourceLocation> tintedPineLeaves_Blocks = tintedPineLeaves_Blocks_DEFAULT;

    // grass blade
    public static final int maxGrassBlade_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxGrassBlade_onPlace = maxGrassBlade_onPlace_DEFAULT;

    public static final boolean grassBlade_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean grassBlade_enabled = grassBlade_enabled_DEFAULT;

    public static final int maxGrassBlade_onBreak_DEFAULT = 4;
    @SerialEntry
    public static int maxGrassBlade_onBreak = maxGrassBlade_onBreak_DEFAULT;

    public static final List<ResourceLocation> grassBlade_Blocks_DEFAULT = List.of(
        RegistryHelpers.getLocationFromBlock(Blocks.ATTACHED_PUMPKIN_STEM),
        RegistryHelpers.getLocationFromBlock(Blocks.PUMPKIN_STEM),
        RegistryHelpers.getLocationFromBlock(Blocks.ATTACHED_MELON_STEM),
        RegistryHelpers.getLocationFromBlock(Blocks.MELON_STEM),
        RegistryHelpers.getLocationFromBlock(Blocks.SWEET_BERRY_BUSH),
        RegistryHelpers.getLocationFromBlock(Blocks.BEETROOTS),
        RegistryHelpers.getLocationFromBlock(Blocks.CARROTS),
        RegistryHelpers.getLocationFromBlock(Blocks.POTATOES),
        RegistryHelpers.getLocationFromBlock(Blocks.WHEAT),
        RegistryHelpers.getLocationFromBlock(Blocks.SHORT_GRASS),
        RegistryHelpers.getLocationFromBlock(Blocks.TALL_GRASS),
        RegistryHelpers.getLocationFromBlock(Blocks.SEAGRASS),
        RegistryHelpers.getLocationFromBlock(Blocks.TALL_SEAGRASS),
        RegistryHelpers.getLocationFromBlock(Blocks.HAY_BLOCK),
        RegistryHelpers.getLocationFromBlock(Blocks.GRASS_BLOCK)
    );
    @SerialEntry
    public static List<ResourceLocation> grassBlade_Blocks = grassBlade_Blocks_DEFAULT;

    // heavy grass blade
    public static final int maxHeavyGrassBlade_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxHeavyGrassBlade_onPlace = maxHeavyGrassBlade_onPlace_DEFAULT;

    public static final boolean heavyGrassBlade_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean heavyGrassBlade_enabled = heavyGrassBlade_enabled_DEFAULT;

    public static final int maxHeavyGrassBlade_onBreak_DEFAULT = 4;
    @SerialEntry
    public static int maxHeavyGrassBlade_onBreak = maxHeavyGrassBlade_onBreak_DEFAULT;

    public static final List<ResourceLocation> heavyGrassBlade_Blocks_DEFAULT = List.of(
        RegistryHelpers.getLocationFromBlock(Blocks.HANGING_ROOTS),
        RegistryHelpers.getLocationFromBlock(Blocks.PALE_HANGING_MOSS),
        RegistryHelpers.getLocationFromBlock(Blocks.COBWEB),
        RegistryHelpers.getLocationFromBlock(Blocks.TRIPWIRE)
    );
    @SerialEntry
    public static List<ResourceLocation> heavyGrassBlade_Blocks = heavyGrassBlade_Blocks_DEFAULT;

    // moss clump
    public static final int maxMossClump_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxMossClump_onPlace = maxMossClump_onPlace_DEFAULT;

    public static final boolean mossClump_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean mossClump_enabled = mossClump_enabled_DEFAULT;

    public static final int maxMossClump_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxMossClump_onBreak = maxMossClump_onBreak_DEFAULT;

    public static final List<ResourceLocation> mossClump_Blocks_DEFAULT = List.of(
        RegistryHelpers.getLocationFromBlock(Blocks.MOSS_CARPET),
        RegistryHelpers.getLocationFromBlock(Blocks.MOSS_BLOCK)
    );
    @SerialEntry
    public static List<ResourceLocation> mossClump_Blocks = mossClump_Blocks_DEFAULT;

    // pale moss clump
    public static final int maxPaleMossClump_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxPaleMossClump_onPlace = maxPaleMossClump_onPlace_DEFAULT;

    public static final boolean paleMossClump_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean paleMossClump_enabled = paleMossClump_enabled_DEFAULT;

    public static final int maxPaleMossClump_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxPaleMossClump_onBreak = maxPaleMossClump_onBreak_DEFAULT;

    public static final List<ResourceLocation> paleMossClump_Blocks_DEFAULT = List.of(
        RegistryHelpers.getLocationFromBlock(Blocks.PALE_MOSS_CARPET),
        RegistryHelpers.getLocationFromBlock(Blocks.PALE_MOSS_BLOCK)
    );
    @SerialEntry
    public static List<ResourceLocation> paleMossClump_Blocks = mossClump_Blocks_DEFAULT;

    // dust
    public static final int maxDust_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxDust_onPlace = maxDust_onPlace_DEFAULT;

    public static final boolean dust_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean dust_enabled = dust_enabled_DEFAULT;

    public static final int maxDust_onBreak_DEFAULT = 3;
    @SerialEntry
    public static int maxDust_onBreak = maxDust_onBreak_DEFAULT;

    public static final List<ResourceLocation> dust_Blocks_DEFAULT = List.of(
        RegistryHelpers.getLocationFromBlock(Blocks.SAND),
        RegistryHelpers.getLocationFromBlock(Blocks.SUSPICIOUS_SAND),
        RegistryHelpers.getLocationFromBlock(Blocks.GRAVEL),
        RegistryHelpers.getLocationFromBlock(Blocks.SUSPICIOUS_GRAVEL),
        RegistryHelpers.getLocationFromBlock(Blocks.RED_SAND),
        RegistryHelpers.getLocationFromBlock(Blocks.SOUL_SAND),
        RegistryHelpers.getLocationFromBlock(Blocks.SOUL_SOIL),
        RegistryHelpers.getLocationFromBlock(Blocks.WHITE_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.LIGHT_GRAY_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.GRAY_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.BROWN_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.RED_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.ORANGE_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.YELLOW_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.LIME_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.GREEN_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.CYAN_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.LIGHT_BLUE_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.BLUE_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.PURPLE_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.MAGENTA_CONCRETE_POWDER),
        RegistryHelpers.getLocationFromBlock(Blocks.PINK_CONCRETE_POWDER)
    );
    @SerialEntry
    public static List<ResourceLocation> dust_Blocks = dust_Blocks_DEFAULT;

    // redstone dust
    public static final int maxRedstoneDust_onPlace_DEFAULT = 3;
    @SerialEntry
    public static int maxRedstoneDust_onPlace = maxRedstoneDust_onPlace_DEFAULT;

    public static final boolean redstoneDust_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean redstoneDust_enabled = redstoneDust_enabled_DEFAULT;

    public static final int maxRedstoneDust_onBreak_DEFAULT = 4;
    @SerialEntry
    public static int maxRedstoneDust_onBreak = maxRedstoneDust_onBreak_DEFAULT;

    public static final List<ResourceLocation> redstoneDust_Blocks_DEFAULT = List.of(
        RegistryHelpers.getLocationFromBlock(Blocks.REDSTONE_TORCH),
        RegistryHelpers.getLocationFromBlock(Blocks.REDSTONE_WALL_TORCH),
        RegistryHelpers.getLocationFromBlock(Blocks.REDSTONE_WIRE),
        RegistryHelpers.getLocationFromBlock(Blocks.REDSTONE_BLOCK),
        RegistryHelpers.getLocationFromBlock(Blocks.REPEATER),
        RegistryHelpers.getLocationFromBlock(Blocks.COMPARATOR)
    );
    @SerialEntry
    public static List<ResourceLocation> redstoneDust_Blocks = redstoneDust_Blocks_DEFAULT;

    // nether portal shatter
    public static final int maxNetherPortalShatter_onPlace_DEFAULT = 3;
    @SerialEntry
    public static int maxNetherPortalShatter_onPlace = maxNetherPortalShatter_onPlace_DEFAULT;

    public static final boolean netherPortalShatter_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean netherPortalShatter_enabled = netherPortalShatter_enabled_DEFAULT;

    public static final int maxNetherPortalShatter_onBreak_DEFAULT = 4;
    @SerialEntry
    public static int maxNetherPortalShatter_onBreak = maxNetherPortalShatter_onBreak_DEFAULT;

    public static final List<ResourceLocation> netherPortalShatter_Blocks_DEFAULT = List.of(
        RegistryHelpers.getLocationFromBlock(Blocks.NETHER_PORTAL)
    );
    @SerialEntry
    public static List<ResourceLocation> netherPortalShatter_Blocks = netherPortalShatter_Blocks_DEFAULT;

    // vanilla block particle
    public static final int maxBlock_onPlace_DEFAULT = 2;
    @SerialEntry
    public static int maxBlock_onPlace = maxBlock_onPlace_DEFAULT;

    public static final boolean block_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean block_enabled = block_enabled_DEFAULT;

    public static final int maxBlock_onBreak_DEFAULT = 4;
    @SerialEntry
    public static int maxBlock_onBreak = maxBlock_onBreak_DEFAULT;

    // Block Interaction / Ambient Particles
    // underwater bubbles
    public static final int maxUnderwaterBubbles_onPlace_DEFAULT = 12;
    @SerialEntry
    public static int maxUnderwaterBubbles_onPlace = maxUnderwaterBubbles_onPlace_DEFAULT;

    public static final boolean underwaterBubbles_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean underwaterBubbles_onPlace = underwaterBubbles_onPlace_DEFAULT;

    // campfire sparks
    public static final int campfireSpark_spawnChance_DEFAULT = 20;
    @SerialEntry
    public static int campfireSpark_spawnChance = campfireSpark_spawnChance_DEFAULT;

    public static final boolean campfireSpark_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean campfireSpark_enabled = campfireSpark_enabled_DEFAULT;

    public static final int campfireEmber_spawnChance_DEFAULT = 45;
    @SerialEntry
    public static int campfireEmber_spawnChance = campfireEmber_spawnChance_DEFAULT;

    public static final boolean campfireEmber_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean campfireEmber_enabled = campfireEmber_enabled_DEFAULT;

    // fire sparks
    public static final int fireSpark_spawnChance_DEFAULT = 25;
    @SerialEntry
    public static int fireSpark_spawnChance = fireSpark_spawnChance_DEFAULT;

    public static final boolean fireSpark_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean fireSpark_enabled = fireSpark_enabled_DEFAULT;

    public static final int fireEmber_spawnChance_DEFAULT = 45;
    @SerialEntry
    public static int fireEmber_spawnChance = fireEmber_spawnChance_DEFAULT;

    public static final boolean fireEmber_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean fireEmber_enabled = fireEmber_enabled_DEFAULT;

    // anvil use sparks
    public static final int maxAnvilUseSparks_onUse_DEFAULT = 18;
    @SerialEntry
    public static int maxAnvilUseSparks_onUse = maxAnvilUseSparks_onUse_DEFAULT;

    public static final boolean anvilUseSparks_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean anvilUseSparks_enabled = anvilUseSparks_enabled_DEFAULT;

    // grindstone use sparks
    public static final int maxGrindstoneUseSparks_onUse_DEFAULT = 12;
    @SerialEntry
    public static int maxGrindstoneUseSparks_onUse = maxGrindstoneUseSparks_onUse_DEFAULT;

    public static final boolean grindstoneUseSparks_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean grindstoneUseSparks_enabled = grindstoneUseSparks_enabled_DEFAULT;

    // falling block effects
    public static final boolean fallingBlockEffect_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean fallingBlockEffect_enabled = fallingBlockEffect_enabled_DEFAULT;

    public static final int fallingBlockEffect_renderDistance_DEFAULT = 64;
    @SerialEntry
    public static int fallingBlockEffect_renderDistance = fallingBlockEffect_renderDistance_DEFAULT;

    // redstone interaction dust
    public static final boolean redstoneInteractionDust_enabled_DEFAULT = true;
    @SerialEntry
    public static boolean redstoneInteractionDust_enabled = redstoneInteractionDust_enabled_DEFAULT;

    public static final int redstoneInteractionDust_amount_DEFAULT = 6;
    @SerialEntry
    public static int redstoneInteractionDust_amount = redstoneInteractionDust_amount_DEFAULT;

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

    // blaze sparks
    public static final int blaze_spawnChance_DEFAULT = 25;
    @SerialEntry
    public static int blaze_spawnChance = blaze_spawnChance_DEFAULT;

    public static final boolean blaze_spawnOnHurt_DEFAULT = true;
    @SerialEntry
    public static boolean blaze_spawnOnHurt = blaze_spawnOnHurt_DEFAULT;

    public static final int blaze_amountToSpawnOnHurt_DEFAULT = 6;
    @SerialEntry
    public static int blaze_amountToSpawnOnHurt = blaze_amountToSpawnOnHurt_DEFAULT;

    // Fluid Placement Particles
    // water
    public static final List<ResourceLocation> tintedWaterSplash_fluids_DEFAULT = List.of(RegistryHelpers.getLocationFromFluid(Fluids.WATER), RegistryHelpers.getLocationFromFluid(Fluids.FLOWING_WATER));
    @SerialEntry
    public static List<ResourceLocation> tintedWaterSplash_fluids = tintedWaterSplash_fluids_DEFAULT;

    public static final int maxTintedWaterSplash_onPlace_DEFAULT = 12;
    @SerialEntry
    public static int maxTintedWaterSplash_onPlace = maxTintedWaterSplash_onPlace_DEFAULT;

    public static final boolean tintedWaterSplash_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean tintedWaterSplash_onPlace = tintedWaterSplash_onPlace_DEFAULT;

    // lava
    public static final List<ResourceLocation> lavaSplash_fluids_DEFAULT = List.of(RegistryHelpers.getLocationFromFluid(Fluids.LAVA), RegistryHelpers.getLocationFromFluid(Fluids.FLOWING_LAVA));
    @SerialEntry
    public static List<ResourceLocation> lavaSplash_fluids = lavaSplash_fluids_DEFAULT;

    public static final int maxLavaSplash_onPlace_DEFAULT = 7;
    @SerialEntry
    public static int maxLavaSplash_onPlace = maxLavaSplash_onPlace_DEFAULT;

    public static final boolean lavaSplash_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean lavaSplash_onPlace = lavaSplash_onPlace_DEFAULT;

    // generic
    public static final List<ResourceLocation> genericSplash_fluids_DEFAULT = List.of();
    @SerialEntry
    public static List<ResourceLocation> genericSplash_fluids = genericSplash_fluids_DEFAULT;

    public static final int maxGenericSplash_onPlace_DEFAULT = 10;
    @SerialEntry
    public static int maxGenericSplash_onPlace = maxGenericSplash_onPlace_DEFAULT;

    public static final boolean genericSplash_onPlace_DEFAULT = true;
    @SerialEntry
    public static boolean genericSplash_onPlace = genericSplash_onPlace_DEFAULT;

    // Item Interaction Particles
    // brush particle behaviour
    public static final BrushParticleBehaviour brushParticleBehaviour_DEFAULT = BrushParticleBehaviour.BLOCK_OVERRIDE_OR_DUST;
    @SerialEntry
    public static BrushParticleBehaviour brushParticleBehaviour = brushParticleBehaviour_DEFAULT;

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

    // fire charge particles
    public static final int fireCharge_intensity_DEFAULT = 5;
    @SerialEntry
    public static int fireCharge_intensity = fireCharge_intensity_DEFAULT;

    public static final int maxFireCharge_onUse_DEFAULT = 12;
    @SerialEntry
    public static int maxFireCharge_onUse = maxFireCharge_onUse_DEFAULT;

    public static final boolean fireCharge_onUse_DEFAULT = true;
    @SerialEntry
    public static boolean fireCharge_onUse = fireCharge_onUse_DEFAULT;

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
