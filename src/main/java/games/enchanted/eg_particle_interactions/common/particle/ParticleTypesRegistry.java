package games.enchanted.eg_particle_interactions.common.particle;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.particle.options.*;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.CustomMovementTerrainParticle;
import games.enchanted.eg_particle_interactions.common.particle.types.bubble.UnderwaterRisingBubble;
import games.enchanted.eg_particle_interactions.common.particle.types.constant_motion.LavaPop;
import games.enchanted.eg_particle_interactions.common.particle.types.drip.DripAndLandParticle;
import games.enchanted.eg_particle_interactions.common.particle.types.dust.Dust;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.arc.ArcEmitter;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.random_distribution.SparkEmitter;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.random_distribution.UnderwaterBubbleEmitter;
import games.enchanted.eg_particle_interactions.common.particle.types.falling_spin.FallingSpinningParticle;
import games.enchanted.eg_particle_interactions.common.particle.types.shatter.BlockShatter;
import games.enchanted.eg_particle_interactions.common.particle.types.spark.FlyingSpark;
import games.enchanted.eg_particle_interactions.common.particle.types.spark.SparkFlash;
import games.enchanted.eg_particle_interactions.common.particle.types.splash.BlockSplash;
import games.enchanted.eg_particle_interactions.common.particle.types.splash.ColouredBucketSplash;
import games.enchanted.eg_particle_interactions.common.particle.types.splash.LavaSplash;
import games.enchanted.eg_particle_interactions.common.particle.types.swirling.Ember;
import games.enchanted.eg_particle_interactions.common.particle.types.swirling.WaterVapour;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ParticleTypesRegistry {
    private static final BiMap<Identifier, PIParticleType<? extends PIParticleOptions>> TYPES = HashBiMap.create();
    private static final Map<PIParticleType<? extends PIParticleOptions>, PIParticleProvider<? extends PIParticleOptions>> PROVIDERS_BY_TYPE = new HashMap<>();

    private static final Codec<PIParticleType<? extends PIParticleOptions>> NAME_CODEC = Identifier.CODEC.flatXmap(
        identifier -> {
            if(TYPES.containsKey(identifier)) {
                return DataResult.success(TYPES.get(identifier));
            }
            return DataResult.error(() -> "Unregistered particle type '" + identifier + "'");
        },
        type -> {
            if(TYPES.inverse().containsKey(type)) {
                return DataResult.success(TYPES.inverse().get(type));
            }
            return DataResult.error(() -> "Failed to get id for unregistered particle type");
        }
    );

    public static final Codec<PIParticleOptions> CODEC = NAME_CODEC.dispatch(
        PIParticleOptions::type,
        PIParticleType::codec
    );

    public static PIParticleType<DustParticleOptions> SNOWFLAKE;
    public static PIParticleType<DustParticleOptions> SNOWFLAKE_SPECK;
    public static PIParticleType.Simple FALLING_CHERRY_PETAL;
    public static PIParticleType.Simple FALLING_TINTED_LEAF;
    public static PIParticleType.Simple FALLING_TINTED_PINE_LEAF;
    public static PIParticleType.Simple FALLING_AZALEA_LEAF;
    public static PIParticleType.Simple FALLING_FLOWERING_AZALEA_LEAF;
    public static PIParticleType.Simple FALLING_PALE_OAK_LEAF;
    public static PIParticleType.Simple FLOWER_PETAL;
    public static PIParticleType.Simple GRASS_BLADE;
    public static PIParticleType.Simple HEAVY_GRASS_BLADE;
    public static PIParticleType.Simple MOSS_CLUMP;
    public static PIParticleType.Simple PALE_MOSS_CLUMP;
    public static PIParticleType<DustParticleOptions> BRUSH_DUST;
    public static PIParticleType<DustParticleOptions> BRUSH_DUST_SPECK;
    public static PIParticleType<DustParticleOptions> ITEM_FRAME_DUST;
    public static PIParticleType<DustParticleOptions> ITEM_FRAME_DUST_SPECK;
    public static PIParticleType<DustParticleOptions> GLOW_ITEM_FRAME_DUST;
    public static PIParticleType<DustParticleOptions> GLOW_ITEM_FRAME_DUST_SPECK;
    public static PIParticleType<DustParticleOptions> BLOCK_DUST;
    public static PIParticleType<DustParticleOptions> BLOCK_DUST_SPECK;
    public static PIParticleType<DustParticleOptions> REDSTONE_DUST;
    public static PIParticleType.Simple BLOCK_SHATTER;
    public static PIParticleType.Simple CHAIN_SNAP;
    public static PIParticleType.Simple SUGAR_CANE;

    public static PIParticleType<DripParticleOption> HONEY_DROP;

    public static PIParticleType.Simple WATER_BUCKET_TINTED_SPLASH;
    public static PIParticleType.Simple LAVA_BUCKET_SPLASH;
    public static PIParticleType.Simple GENERIC_FLUID_BUCKET_SPLASH;

    public static PIParticleType.Simple FLYING_SPARK;
    public static PIParticleType.Simple FLOATING_SPARK;
    public static PIParticleType.Simple FLYING_SOUL_SPARK;
    public static PIParticleType.Simple FLOATING_SOUL_SPARK;

    public static PIParticleType.Simple SPARK_FLASH;
    public static PIParticleType.Simple SOUL_SPARK_FLASH;
    public static PIParticleType.Simple LIGHTNING_FLASH;

    public static PIParticleType.Simple UNDERWATER_RISING_BUBBLE;
    public static PIParticleType.Simple UNDERWATER_RISING_BUBBLE_SMALL;

    public static PIParticleType.Simple FLOATING_EMBER;
    public static PIParticleType.Simple FLOATING_SOUL_EMBER;
    public static PIParticleType.Simple WATER_VAPOUR;

    public static PIParticleType.Simple LAVA_POP;

    public static PIParticleType<RandomDistributionEmitterOptions> FLYING_SPARK_EMITTER;
    public static PIParticleType<RandomDistributionEmitterOptions> UNDERWATER_RISING_BUBBLE_SMALL_EMITTER;
    public static PIParticleType<ArcEmitterOptions> ARC_EMITTER;

    // this only exists because the vanilla block cracking particles are created inside the particle engine
    public static PIParticleType.Simple BLOCK_CRACK;
    // this exists so block particles can be spawned with low velocities and still move correctly (hacky workaround
    //  for Block Particle Overrides not having a way to spawn different particles at different velocities)
    public static PIParticleType.Simple BLOCK_HIGH_VELOCITY;

    public static void registerParticles() {
        SNOWFLAKE = register(Dust.SnowflakeProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "snowflake"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        SNOWFLAKE_SPECK = register(Dust.SnowflakeSpeckProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "snowflake_speck"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        FALLING_CHERRY_PETAL = register(FallingSpinningParticle.GenericLeafProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "falling_cherry_leaves"));
        FALLING_TINTED_LEAF = register(FallingSpinningParticle.TintedLeafProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "falling_tinted_leaves"));
        FALLING_TINTED_PINE_LEAF = register(FallingSpinningParticle.TintedLeafProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "falling_tinted_pine_leaves"));
        FALLING_AZALEA_LEAF = register(FallingSpinningParticle.GenericLeafProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "falling_azalea_leaves"));
        FALLING_FLOWERING_AZALEA_LEAF = register(FallingSpinningParticle.GenericLeafProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "falling_flowering_azalea_leaves"));
        FALLING_PALE_OAK_LEAF = register(FallingSpinningParticle.PaleOakProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "falling_pale_oak_leaf"));
        FLOWER_PETAL = register(FallingSpinningParticle.FlowerPetalProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "flower_petal"));
        GRASS_BLADE = register(FallingSpinningParticle.GrassBladeProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "grass_blade"));
        HEAVY_GRASS_BLADE = register(FallingSpinningParticle.HeavyGrassBladeProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "heavy_grass_blade"));
        MOSS_CLUMP = register(FallingSpinningParticle.RandomisedSizeMoreGravityProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "moss_clump"));
        PALE_MOSS_CLUMP = register(FallingSpinningParticle.RandomisedSizeMoreGravityProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "pale_moss_clump"));
        BRUSH_DUST = register(Dust.BrushProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "brush_dust"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        BRUSH_DUST_SPECK = register(Dust.BrushSpeckProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "brush_dust_speck"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        ITEM_FRAME_DUST = register(Dust.ItemFrameProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item_frame_dust"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        ITEM_FRAME_DUST_SPECK = register(Dust.ItemFrameSpeckProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item_frame_dust_speck"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        GLOW_ITEM_FRAME_DUST = register(Dust.GlowItemFrameProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "glow_item_frame_dust"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        GLOW_ITEM_FRAME_DUST_SPECK = register(Dust.GlowItemFrameSpeckProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "glow_item_frame_dust_speck"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        BLOCK_DUST = register(Dust.TintedDustProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block_dust"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        BLOCK_DUST_SPECK = register(Dust.TintedDustSpeckProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block_dust_speck"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        REDSTONE_DUST = register(Dust.RedstoneProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "redstone_dust"), DustParticleOptions::codec, DustParticleOptions::streamCodec);
        BLOCK_SHATTER = register(BlockShatter.BlockShatterProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block_shatter"));
        CHAIN_SNAP = register(FallingSpinningParticle.ChainSnapProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "chain_snap"));
        SUGAR_CANE = register(FallingSpinningParticle.SugarCaneProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "sugar_cane"));

        HONEY_DROP = register(DripAndLandParticle.UntintedDropProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "honey_drop"), DripParticleOption::codec, DripParticleOption::streamCodec);

        WATER_BUCKET_TINTED_SPLASH = register(ColouredBucketSplash.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "water_bucket_tinted_splash"));
        LAVA_BUCKET_SPLASH = register(LavaSplash.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "lava_bucket_splash"));
        GENERIC_FLUID_BUCKET_SPLASH = register(BlockSplash.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "generic_fluid_bucket_splash"));

        FLYING_SPARK = register(FlyingSpark.FlyingSparkProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "flying_spark"));
        FLOATING_SPARK = register(FlyingSpark.FloatingSparkProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "floating_spark"));
        FLYING_SOUL_SPARK = register(FlyingSpark.FlyingSoulSparkProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "flying_soul_spark"));
        FLOATING_SOUL_SPARK = register(FlyingSpark.FloatingSoulSparkProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "floating_soul_spark"));

        SPARK_FLASH = register(SparkFlash.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "spark_flash"));
        SOUL_SPARK_FLASH = register(SparkFlash.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "soul_spark_flash"));
        LIGHTNING_FLASH = register(SparkFlash.RandomAnimationProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "lightning_flash"));

        UNDERWATER_RISING_BUBBLE = register(UnderwaterRisingBubble.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "underwater_rising_bubble"));
        UNDERWATER_RISING_BUBBLE_SMALL = register(UnderwaterRisingBubble.SmallProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "underwater_rising_bubble_small"));

        FLOATING_EMBER = register(Ember.EmberProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "floating_ember"));
        FLOATING_SOUL_EMBER = register(Ember.EmberProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "floating_soul_ember"));
        WATER_VAPOUR = register(WaterVapour.WaterVapourProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "water_vapour"));

        LAVA_POP = register(LavaPop.LavaPopProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "lava_pop"));

        FLYING_SPARK_EMITTER = register(SparkEmitter.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "flying_spark_emitter"), RandomDistributionEmitterOptions::codec, RandomDistributionEmitterOptions::streamCodec);
        UNDERWATER_RISING_BUBBLE_SMALL_EMITTER = register(UnderwaterBubbleEmitter.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "underwater_rising_bubble_small_emitter"), RandomDistributionEmitterOptions::codec, RandomDistributionEmitterOptions::streamCodec);
        ARC_EMITTER = register(ArcEmitter.Provider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "arc_emitter"), ArcEmitterOptions::codec, ArcEmitterOptions::streamCodec);

        BLOCK_CRACK = register(CustomMovementTerrainParticle.CrackingProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block_crack"));
        BLOCK_CRACK = register(CustomMovementTerrainParticle.BlockProvider::new, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block"));
    }

    private static PIParticleType.Simple register(PIProviderCreator<PIParticleType.Simple> providerCreator, Identifier id) {
        PIParticleType.Simple type = new PIParticleType.Simple();
        registerType(type, id, providerCreator.create());
        return type;
    }

    private static <T extends PIParticleOptions> PIParticleType<T> register(
        PIProviderCreator<T> providerCreator,
        Identifier id,
        Function<PIParticleType<T>, MapCodec<T>> codecGetter,
        final Function<PIParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter
    ) {
        PIParticleType<T> type = new PIParticleType<>() {
            public @NotNull MapCodec<T> codec() {
                return codecGetter.apply(this);
            }
        };
        registerType(type, id, providerCreator.create());
        return type;
    }

    private static void registerType(PIParticleType<?> type, Identifier id, PIParticleProvider<?> provider) {
        TYPES.put(id, type);
        PROVIDERS_BY_TYPE.put(type, provider);
    }

    @FunctionalInterface
    public interface PIProviderCreator<T extends PIParticleOptions> {
        PIParticleProvider<T> create();
    }


    public static <T extends PIParticleOptions> PIParticleProvider<T> getProviderOrThrow(PIParticleType<T> type) {
        if(!PROVIDERS_BY_TYPE.containsKey(type)) {
            throw new RuntimeException("Tried to get provider for unregistered particle type");
        }
        return (PIParticleProvider<T>) PROVIDERS_BY_TYPE.get(type);
    }

    public static <T extends PIParticleOptions> @Nullable PIParticleProvider<T> getProvider(PIParticleType<T> type) {
        if(!PROVIDERS_BY_TYPE.containsKey(type)) {
            return null;
        }
        return (PIParticleProvider<T>) PROVIDERS_BY_TYPE.get(type);
    }


    public static Identifier getIdOrThrow(PIParticleType<?> type) {
        if(!TYPES.inverse().containsKey(type)) {
            throw new RuntimeException("Tried to get id for unregistered particle type");
        }
        return TYPES.inverse().get(type);
    }

    public static @Nullable Identifier getId(PIParticleType<?> type) {
        if(!TYPES.inverse().containsKey(type)) {
            return null;
        }
        return TYPES.inverse().get(type);
    }


    public static <T extends PIParticleOptions> @Nullable PIParticleType<T> getType(Identifier id) {
        if(!TYPES.containsKey(id)) {
            return null;
        }
        return (PIParticleType<T>) TYPES.get(id);
    }

    public static <T extends PIParticleOptions> PIParticleType<T> getTypeOrThrow(Identifier id) {
        if(!TYPES.containsKey(id)) {
            throw new RuntimeException("Tried to get id for unregistered particle type");
        }
        return (PIParticleType<T>) TYPES.get(id);
    }
}
