package games.enchanted.eg_particle_interactions.common.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.particle.types.CustomMovementTerrainParticle;
import games.enchanted.eg_particle_interactions.common.particle.types.bubble.UnderwaterRisingBubble;
import games.enchanted.eg_particle_interactions.common.particle.types.constant_motion.LavaPop;
import games.enchanted.eg_particle_interactions.common.particle.types.drip.DripAndLandParticle;
import games.enchanted.eg_particle_interactions.common.particle.types.dust.BasicDust;
import games.enchanted.eg_particle_interactions.common.particle.types.dust.BasicTintedDust;
import games.enchanted.eg_particle_interactions.common.particle.types.dust.FloatingColouredDust;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.arc.ArcEmitter;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.random_distribution.UnderwaterBubbleEmitter;
import games.enchanted.eg_particle_interactions.common.particle.options.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.DripParticleOption;
import games.enchanted.eg_particle_interactions.common.particle.options.RandomDistributionEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.TintedParticleOption;
import games.enchanted.eg_particle_interactions.common.particle.types.falling_spin.FallingSpinningColouredParticle;
import games.enchanted.eg_particle_interactions.common.particle.types.falling_spin.FallingSpinningParticle;
import games.enchanted.eg_particle_interactions.common.particle.types.shatter.BlockShatter;
import games.enchanted.eg_particle_interactions.common.particle.types.spark.FlyingSpark;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.random_distribution.SparkEmitter;
import games.enchanted.eg_particle_interactions.common.particle.types.spark.SparkFlash;
import games.enchanted.eg_particle_interactions.common.particle.types.splash.BlockSplash;
import games.enchanted.eg_particle_interactions.common.particle.types.splash.ColouredBucketSplash;
import games.enchanted.eg_particle_interactions.common.particle.types.splash.LavaSplash;
import games.enchanted.eg_particle_interactions.common.particle.types.swirling.Ember;
import games.enchanted.eg_particle_interactions.common.particle.types.swirling.WaterVapour;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModParticleTypes {
    public static SimpleParticleType SNOWFLAKE;
    public static SimpleParticleType SNOWFLAKE_SPECK;
    public static SimpleParticleType FALLING_CHERRY_PETAL;
    public static ParticleType<BlockParticleOption> FALLING_TINTED_LEAF;
    public static ParticleType<BlockParticleOption> FALLING_TINTED_PINE_LEAF;
    public static SimpleParticleType FALLING_AZALEA_LEAF;
    public static SimpleParticleType FALLING_FLOWERING_AZALEA_LEAF;
    public static SimpleParticleType FALLING_PALE_OAK_LEAF;
    public static ParticleType<BlockParticleOption> FLOWER_PETAL;
    public static ParticleType<BlockParticleOption> GRASS_BLADE;
    public static ParticleType<BlockParticleOption> HEAVY_GRASS_BLADE;
    public static SimpleParticleType MOSS_CLUMP;
    public static SimpleParticleType PALE_MOSS_CLUMP;
    public static ParticleType<TintedParticleOption> BRUSH_DUST;
    public static ParticleType<TintedParticleOption> BRUSH_DUST_SPECK;
    public static ParticleType<TintedParticleOption> ITEM_FRAME_DUST;
    public static ParticleType<TintedParticleOption> ITEM_FRAME_DUST_SPECK;
    public static ParticleType<TintedParticleOption> GLOW_ITEM_FRAME_DUST;
    public static ParticleType<TintedParticleOption> GLOW_ITEM_FRAME_DUST_SPECK;
    public static ParticleType<BlockParticleOption> TINTED_DUST;
    public static ParticleType<BlockParticleOption> TINTED_DUST_SPECK;
    public static ParticleType<BlockParticleOption> REDSTONE_DUST;
    public static ParticleType<BlockParticleOption> BLOCK_SHATTER;
    public static ParticleType<BlockParticleOption> CHAIN_SNAP;
    public static ParticleType<BlockParticleOption> SUGAR_CANE;

    public static ParticleType<DripParticleOption> HONEY_DROP;

    public static ParticleType<BlockParticleOption> WATER_BUCKET_TINTED_SPLASH;
    public static SimpleParticleType LAVA_BUCKET_SPLASH;
    public static ParticleType<BlockParticleOption> GENERIC_FLUID_BUCKET_SPLASH;

    public static SimpleParticleType FLYING_SPARK;
    public static SimpleParticleType FLOATING_SPARK;
    public static SimpleParticleType FLYING_SOUL_SPARK;
    public static SimpleParticleType FLOATING_SOUL_SPARK;

    public static SimpleParticleType SPARK_FLASH;
    public static SimpleParticleType SOUL_SPARK_FLASH;
    public static SimpleParticleType LIGHTNING_FLASH;

    public static SimpleParticleType UNDERWATER_RISING_BUBBLE;
    public static SimpleParticleType UNDERWATER_RISING_BUBBLE_SMALL;

    public static SimpleParticleType FLOATING_EMBER;
    public static SimpleParticleType FLOATING_SOUL_EMBER;
    public static SimpleParticleType WATER_VAPOUR;

    public static SimpleParticleType LAVA_POP;

    public static ParticleType<RandomDistributionEmitterOptions> FLYING_SPARK_EMITTER;
    public static ParticleType<RandomDistributionEmitterOptions> UNDERWATER_RISING_BUBBLE_SMALL_EMITTER;
    public static ParticleType<ArcEmitterOptions> ARC_EMITTER;

    // this only exists because the vanilla block cracking particles are created inside the particle engine
    public static ParticleType<BlockParticleOption> BLOCK_CRACK;
    // this exists so block particles can be spawned with low velocities and still move correctly (hacky workaround
    //  for Block Particle Overrides not having a way to spawn different particles at different velocities)
    public static ParticleType<BlockParticleOption> BLOCK_HIGH_VELOCITY;

    @SuppressWarnings({"unchecked","rawtypes"})
    public static void registerParticles() {
        SNOWFLAKE = register((SpriteProviderReg) BasicDust.SnowflakeProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "snowflake"), false);
        SNOWFLAKE_SPECK = register((SpriteProviderReg) BasicDust.SnowflakeSpeckProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "snowflake_speck"), false);
        FALLING_CHERRY_PETAL = register((SpriteProviderReg) FallingSpinningParticle.GenericLeafProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "falling_cherry_leaves"), false);
        FALLING_TINTED_LEAF = register((SpriteProviderReg) FallingSpinningColouredParticle.TintedLeafProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "falling_tinted_leaves"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        FALLING_TINTED_PINE_LEAF = register((SpriteProviderReg) FallingSpinningColouredParticle.TintedLeafProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "falling_tinted_pine_leaves"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        FALLING_AZALEA_LEAF = register((SpriteProviderReg) FallingSpinningParticle.GenericLeafProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "falling_azalea_leaves"), false);
        FALLING_FLOWERING_AZALEA_LEAF = register((SpriteProviderReg) FallingSpinningParticle.GenericLeafProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "falling_flowering_azalea_leaves"), false);
        FALLING_PALE_OAK_LEAF = register((SpriteProviderReg) FallingSpinningParticle.PaleOakProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "falling_pale_oak_leaf"), false);
        FLOWER_PETAL = register((SpriteProviderReg) FallingSpinningColouredParticle.FlowerPetalProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flower_petal"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        GRASS_BLADE = register((SpriteProviderReg) FallingSpinningColouredParticle.GrassBladeProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "grass_blade"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        HEAVY_GRASS_BLADE = register((SpriteProviderReg) FallingSpinningColouredParticle.HeavyGrassBladeProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "heavy_grass_blade"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        MOSS_CLUMP = register((SpriteProviderReg) FallingSpinningParticle.RandomisedSizeMoreGravityProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "moss_clump"), false);
        PALE_MOSS_CLUMP = register((SpriteProviderReg) FallingSpinningParticle.RandomisedSizeMoreGravityProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "pale_moss_clump"), false);
        BRUSH_DUST = register((SpriteProviderReg) BasicTintedDust.BrushProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brush_dust"), false, TintedParticleOption::codec, TintedParticleOption::streamCodec);
        BRUSH_DUST_SPECK = register((SpriteProviderReg) BasicTintedDust.BrushSpeckProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brush_dust_speck"), false, TintedParticleOption::codec, TintedParticleOption::streamCodec);
        ITEM_FRAME_DUST = register((SpriteProviderReg) BasicTintedDust.ItemFrameProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "item_frame_dust"), false, TintedParticleOption::codec, TintedParticleOption::streamCodec);
        ITEM_FRAME_DUST_SPECK = register((SpriteProviderReg) BasicTintedDust.ItemFrameSpeckProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "item_frame_dust_speck"), false, TintedParticleOption::codec, TintedParticleOption::streamCodec);
        GLOW_ITEM_FRAME_DUST = register((SpriteProviderReg) BasicTintedDust.GlowItemFrameProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "glow_item_frame_dust"), false, TintedParticleOption::codec, TintedParticleOption::streamCodec);
        GLOW_ITEM_FRAME_DUST_SPECK = register((SpriteProviderReg) BasicTintedDust.GlowItemFrameSpeckProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "glow_item_frame_dust_speck"), false, TintedParticleOption::codec, TintedParticleOption::streamCodec);
        TINTED_DUST = register((SpriteProviderReg) FloatingColouredDust.TintedDustProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "tinted_dust"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        TINTED_DUST_SPECK = register((SpriteProviderReg) FloatingColouredDust.TintedDustSpeckProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "tinted_dust_speck"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        REDSTONE_DUST = register((SpriteProviderReg) FloatingColouredDust.RedstoneProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "redstone_dust"), false, TintedParticleOption::codec, TintedParticleOption::streamCodec);
        BLOCK_SHATTER = register((SpriteProviderReg) BlockShatter.BlockShatterProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block_shatter"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        CHAIN_SNAP = register((SpriteProviderReg) FallingSpinningColouredParticle.ChainSnapProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "chain_snap"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        SUGAR_CANE = register((SpriteProviderReg) FallingSpinningColouredParticle.SugarCaneProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sugar_cane"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);

        HONEY_DROP = register((SpriteProviderReg) DripAndLandParticle.UntintedDropProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "honey_drop"), false, DripParticleOption::codec, DripParticleOption::streamCodec);

        WATER_BUCKET_TINTED_SPLASH = register((SpriteProviderReg) ColouredBucketSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "water_bucket_tinted_splash"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        LAVA_BUCKET_SPLASH = register((SpriteProviderReg) LavaSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lava_bucket_splash"), false);
        GENERIC_FLUID_BUCKET_SPLASH = register((SpriteProviderReg) BlockSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "generic_fluid_bucket_splash"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);

        FLYING_SPARK = register((SpriteProviderReg) FlyingSpark.FlyingSparkProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flying_spark"), false);
        FLOATING_SPARK = register((SpriteProviderReg) FlyingSpark.FloatingSparkProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "floating_spark"), false);
        FLYING_SOUL_SPARK = register((SpriteProviderReg) FlyingSpark.FlyingSoulSparkProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flying_soul_spark"), false);
        FLOATING_SOUL_SPARK = register((SpriteProviderReg) FlyingSpark.FloatingSoulSparkProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "floating_soul_spark"), false);

        SPARK_FLASH = register((SpriteProviderReg) SparkFlash.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "spark_flash"), false);
        SOUL_SPARK_FLASH = register((SpriteProviderReg) SparkFlash.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "soul_spark_flash"), false);
        LIGHTNING_FLASH = register((SpriteProviderReg) SparkFlash.RandomAnimationProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lightning_flash"), false);

        UNDERWATER_RISING_BUBBLE = register((SpriteProviderReg) UnderwaterRisingBubble.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "underwater_rising_bubble"), false);
        UNDERWATER_RISING_BUBBLE_SMALL = register((SpriteProviderReg) UnderwaterRisingBubble.SmallProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "underwater_rising_bubble_small"), false);

        FLOATING_EMBER = register((SpriteProviderReg) Ember.EmberProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "floating_ember"), true);
        FLOATING_SOUL_EMBER = register((SpriteProviderReg) Ember.EmberProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "floating_soul_ember"), true);
        WATER_VAPOUR = register((SpriteProviderReg) WaterVapour.WaterVapourProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "water_vapour"), true);

        LAVA_POP = register((SpriteProviderReg) LavaPop.LavaPopProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lava_pop"), true);

        FLYING_SPARK_EMITTER = register((SpriteProviderReg) SparkEmitter.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flying_spark_emitter"), true, RandomDistributionEmitterOptions::codec, RandomDistributionEmitterOptions::streamCodec);
        UNDERWATER_RISING_BUBBLE_SMALL_EMITTER = register((SpriteProviderReg) UnderwaterBubbleEmitter.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "underwater_rising_bubble_small_emitter"), true, RandomDistributionEmitterOptions::codec, RandomDistributionEmitterOptions::streamCodec);
        ARC_EMITTER = register((SpriteProviderReg) ArcEmitter.Provider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "arc_emitter"), true, ArcEmitterOptions::codec, ArcEmitterOptions::streamCodec);

        BLOCK_CRACK = register((SpriteProviderReg) CustomMovementTerrainParticle.CrackingProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block_crack"), true, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        BLOCK_HIGH_VELOCITY = register((SpriteProviderReg) CustomMovementTerrainParticle.UncappedMotionProvider::new, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block_high_velocity"), true, BlockParticleOption::codec, BlockParticleOption::streamCodec);
    }

    private static SimpleParticleType register(SpriteProviderReg<SimpleParticleType> provider, ResourceLocation particleID, boolean alwaysShow) {
        SimpleParticleType registeredParticleType = Registry.register(BuiltInRegistries.PARTICLE_TYPE, particleID, PlatformHelper.createNewSimpleParticle(alwaysShow));
        PlatformHelper.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }

    private static <T extends ParticleOptions> ParticleType<T> register(SpriteProviderReg<T> provider, ResourceLocation particleID, boolean alwaysShow, final Function<ParticleType<T>, MapCodec<T>> codecGetter, final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter) {
        ParticleType<T> registeredParticleType = RegistryHelpers.register(Registries.PARTICLE_TYPE, () -> new ParticleType<T>(alwaysShow) {
            public @NotNull MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return packetCodecGetter.apply(this);
            }
        }, particleID);
        PlatformHelper.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }

    @FunctionalInterface
    public interface SpriteProviderReg<T extends ParticleOptions> {
        ParticleProvider<T> create(SpriteSet spriteSet);
    }
}
