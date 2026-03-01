package games.enchanted.eg_particle_interactions.common.particle.options;

import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.ParticleInteractionsEmitter;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomIntProvider;

import java.util.function.Supplier;

public class DefaultParticles {
    public static final ParticleConfig HONEY_DROP_CONFIG = new ParticleConfig(
        new RandomFloatProvider(0.02f, 0.03f),
        new RandomIntProvider(350, 500),
        new RandomFloatProvider(0.01f, 0.01f)
    );

    public static final Supplier<DripParticleOption> FALLING_HONEY_DROP = () -> new DripParticleOption(ParticleTypesRegistry.HONEY_DROP, HONEY_DROP_CONFIG, 0);
    public static final Supplier<DripParticleOption> HANGING_HONEY_DROP = () -> new DripParticleOption(ParticleTypesRegistry.HONEY_DROP, HONEY_DROP_CONFIG, 30);


    private static final RandomIntProvider DUST_LIFETIME = new RandomIntProvider(22, 82);
    public static final ParticleConfig DUST_CONFIG = new ParticleConfig(new RandomFloatProvider(0.175f, 0.266f), DUST_LIFETIME);
    public static final ParticleConfig DUST_SPECK_CONFIG = new ParticleConfig(new RandomFloatProvider(0.0875f, 0.133f), DUST_LIFETIME);
    public static final ParticleConfig REDSTONE_DUST_CONFIG = new ParticleConfig(new RandomFloatProvider(0f, 0f), DUST_LIFETIME);
    public static final ParticleConfig SNOWFLAKE_CONFIG = new ParticleConfig(new RandomFloatProvider(0.275f, 0.418f), DUST_LIFETIME);
    public static final ParticleConfig SNOWFLAKE_SPECK_CONFIG = new ParticleConfig(new RandomFloatProvider(0.175f, 0.266f), DUST_LIFETIME);

    public static final Supplier<DustParticleOptions> GLOW_ITEM_FRAME_DUST_SPECK = () -> new DustParticleOptions(
        ParticleTypesRegistry.GLOW_ITEM_FRAME_DUST_SPECK,
        DUST_SPECK_CONFIG,
        null
    );
    public static final Supplier<DustParticleOptions> GLOW_ITEM_FRAME_DUST = () -> new DustParticleOptions(
        ParticleTypesRegistry.GLOW_ITEM_FRAME_DUST,
        DUST_CONFIG,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, GLOW_ITEM_FRAME_DUST_SPECK.get())
    );
    public static final Supplier<DustParticleOptions> ITEM_FRAME_DUST_SPECK = () -> new DustParticleOptions(
        ParticleTypesRegistry.ITEM_FRAME_DUST_SPECK,
        DUST_SPECK_CONFIG,
        null
    );
    public static final Supplier<DustParticleOptions> ITEM_FRAME_DUST = () -> new DustParticleOptions(
        ParticleTypesRegistry.ITEM_FRAME_DUST,
        DUST_CONFIG,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, ITEM_FRAME_DUST_SPECK.get())
    );
    public static final Supplier<DustParticleOptions> BRUSH_DUST_SPECK = () -> new DustParticleOptions(
        ParticleTypesRegistry.BRUSH_DUST_SPECK,
        DUST_SPECK_CONFIG,
        null
    );
    public static final Supplier<DustParticleOptions> BRUSH_DUST = () -> new DustParticleOptions(
        ParticleTypesRegistry.BRUSH_DUST,
        DUST_CONFIG,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, BRUSH_DUST_SPECK.get())
    );


    public static final ParticleConfig SPARK_FLASH_CONFIG = new ParticleConfig(ParticleConfig.DEFAULT_GRAVITY, new RandomIntProvider(4, 7));

    public static final Supplier<SimpleParticleOptions> SPARK_FLASH = () -> new SimpleParticleOptions(ParticleTypesRegistry.SPARK_FLASH, SPARK_FLASH_CONFIG);
    public static final Supplier<SimpleParticleOptions> SOUL_SPARK_FLASH = () -> new SimpleParticleOptions(ParticleTypesRegistry.SOUL_SPARK_FLASH, SPARK_FLASH_CONFIG);
    public static final Supplier<SimpleParticleOptions> LIGHTNING_FLASH = () -> new SimpleParticleOptions(ParticleTypesRegistry.LIGHTNING_FLASH, SPARK_FLASH_CONFIG);

    private static final RandomFloatProvider SPARK_COLLISION_SIZE = new RandomFloatProvider(0.03f, 0.03f);
    public static final ParticleConfig FLYING_SPARK_CONFIG = new ParticleConfig(new RandomFloatProvider(0.8f, 0.9f), new RandomIntProvider(20, 60), SPARK_COLLISION_SIZE);
    public static final ParticleConfig FLOATING_SPARK_CONFIG = new ParticleConfig(new RandomFloatProvider(0.2f, 0.3f), new RandomIntProvider(4, 12), SPARK_COLLISION_SIZE);

    public static final Supplier<SparkParticleOptions> FLYING_SPARK = () -> new SparkParticleOptions(
        ParticleTypesRegistry.FLYING_SPARK,
        FLYING_SPARK_CONFIG,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, SPARK_FLASH.get())
    );
    public static final Supplier<SparkParticleOptions> FLOATING_SPARK = () -> new SparkParticleOptions(
        ParticleTypesRegistry.FLOATING_SPARK,
        FLOATING_SPARK_CONFIG,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, SPARK_FLASH.get())
    );

    public static final Supplier<SparkParticleOptions> FLYING_SOUL_SPARK = () -> new SparkParticleOptions(
        ParticleTypesRegistry.FLYING_SOUL_SPARK,
        FLYING_SPARK_CONFIG,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, SOUL_SPARK_FLASH.get())
    );
    public static final Supplier<SparkParticleOptions> FLOATING_SOUL_SPARK = () -> new SparkParticleOptions(
        ParticleTypesRegistry.FLOATING_SOUL_SPARK,
        FLOATING_SPARK_CONFIG,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, SOUL_SPARK_FLASH.get())
    );


    public static final ParticleConfig LAVA_POP_CONFIG = new ParticleConfig(ParticleConfig.DEFAULT_GRAVITY, new RandomIntProvider(30, 36));

    public static final Supplier<SimpleParticleOptions> LAVA_POP = () -> new SimpleParticleOptions(
        ParticleTypesRegistry.LAVA_POP,
        LAVA_POP_CONFIG
    );


    private static final float FALLING_SPIN_MIN_BASE_GRAVITY = 0.25f;
    private static final float FALLING_SPIN_MAX_BASE_GRAVITY = 0.38f;
    private static final RandomIntProvider FALLING_SPIN_LIFETIME = new RandomIntProvider(18, 82);
    private static final RandomFloatProvider FALLING_SPIN_COLLISION_SIZE = new RandomFloatProvider(0.1f, 0.15f);
    private static final RandomFloatProvider FALLING_SPIN_GRASS_BLADE_COLLISION_SIZE = new RandomFloatProvider(0.10f, 0.12f);

    public static final ParticleConfig GENERIC_LEAF_CONFIG = new ParticleConfig(
        new RandomFloatProvider(FALLING_SPIN_MIN_BASE_GRAVITY, FALLING_SPIN_MAX_BASE_GRAVITY),
        FALLING_SPIN_LIFETIME,
        FALLING_SPIN_COLLISION_SIZE
    );

    public static final ParticleConfig PALE_OAK_LEAF_CONFIG = new ParticleConfig(
        new RandomFloatProvider(FALLING_SPIN_MIN_BASE_GRAVITY * 0.6f, FALLING_SPIN_MAX_BASE_GRAVITY * 0.6f),
        FALLING_SPIN_LIFETIME,
        FALLING_SPIN_COLLISION_SIZE
    );

    public static final ParticleConfig MOSS_CLUMP_CONFIG = new ParticleConfig(
        new RandomFloatProvider(FALLING_SPIN_MIN_BASE_GRAVITY * 2f, FALLING_SPIN_MAX_BASE_GRAVITY * 2f),
        FALLING_SPIN_LIFETIME,
        new RandomFloatProvider(0.08f, 0.12f)
    );

    public static final ParticleConfig FLOWER_PETAL_CONFIG = new ParticleConfig(
        new RandomFloatProvider(FALLING_SPIN_MIN_BASE_GRAVITY, FALLING_SPIN_MAX_BASE_GRAVITY),
        FALLING_SPIN_LIFETIME,
        new RandomFloatProvider(0.07F, 0.08F)
    );

    public static final ParticleConfig GRASS_BLADE_CONFIG = new ParticleConfig(
        new RandomFloatProvider(FALLING_SPIN_MIN_BASE_GRAVITY, FALLING_SPIN_MAX_BASE_GRAVITY),
        FALLING_SPIN_LIFETIME,
        FALLING_SPIN_GRASS_BLADE_COLLISION_SIZE
    );

    public static final ParticleConfig HEAVY_GRASS_BLADE_CONFIG = new ParticleConfig(
        new RandomFloatProvider(FALLING_SPIN_MIN_BASE_GRAVITY * 2f, FALLING_SPIN_MAX_BASE_GRAVITY * 2f),
        FALLING_SPIN_LIFETIME,
        FALLING_SPIN_GRASS_BLADE_COLLISION_SIZE
    );

    public static final ParticleConfig CHAIN_SNAP_CONFIG = new ParticleConfig(
        new RandomFloatProvider(FALLING_SPIN_MIN_BASE_GRAVITY * 3f, FALLING_SPIN_MAX_BASE_GRAVITY * 3f),
        FALLING_SPIN_LIFETIME,
        new RandomFloatProvider(0.14F, 0.15F)
    );

    public static final ParticleConfig SUGAR_CANE_CONFIG = new ParticleConfig(
        new RandomFloatProvider(FALLING_SPIN_MIN_BASE_GRAVITY * 2.5f, FALLING_SPIN_MAX_BASE_GRAVITY * 2.5f),
        FALLING_SPIN_LIFETIME,
        new RandomFloatProvider(0.11F, 0.13F)
    );


    public static final ParticleConfig SHATTER_CONFIG = new ParticleConfig(
        new RandomFloatProvider(0.75f, 0.9f),
        new RandomIntProvider(5, 25)
    );


    private static final RandomFloatProvider BUCKET_SPLASH_GRAVITY = new RandomFloatProvider(0.94f, 0.96f);
    private static final RandomIntProvider BUCKET_SPLASH_LIFETIME = new RandomIntProvider(20, 80);

    public static final ParticleConfig BUCKET_SPLASH_CONFIG = new ParticleConfig(
        BUCKET_SPLASH_GRAVITY,
        BUCKET_SPLASH_LIFETIME
    );

    public static final ParticleConfig BLOCK_SPLASH_CONFIG = new ParticleConfig(
        BUCKET_SPLASH_GRAVITY,
        BUCKET_SPLASH_LIFETIME,
        new RandomFloatProvider(0.01f, 0.02f)
    );


    private static final RandomFloatProvider SWIRLING_COLLISION_SIZE = new RandomFloatProvider(0.03125f, 0.03125f);

    public static final ParticleConfig SWIRLING_DEFAULT_CONFIG = new ParticleConfig(
        new RandomFloatProvider(0.5f, 0.5f),
        new RandomIntProvider(100, 100),
        SWIRLING_COLLISION_SIZE
    );

    public static final ParticleConfig EMBER_CONFIG = new ParticleConfig(
        new RandomFloatProvider(-0.03f, -0.08f),
        new RandomIntProvider(20, 100),
        SWIRLING_COLLISION_SIZE
    );

    public static final ParticleConfig WATER_VAPOUR_CONFIG = new ParticleConfig(
        new RandomFloatProvider(-0.04f, -0.07f),
        new RandomIntProvider(4, 15),
        SWIRLING_COLLISION_SIZE
    );

    public static Supplier<SimpleParticleOptions> FLOATING_EMBER = () -> new SimpleParticleOptions(
        ParticleTypesRegistry.FLOATING_EMBER,
        EMBER_CONFIG
    );

    public static Supplier<SimpleParticleOptions> FLOATING_SOUL_EMBER = () -> new SimpleParticleOptions(
        ParticleTypesRegistry.FLOATING_SOUL_EMBER,
        EMBER_CONFIG
    );

    public static Supplier<SimpleParticleOptions> WATER_VAPOUR = () -> new SimpleParticleOptions(
        ParticleTypesRegistry.WATER_VAPOUR,
        WATER_VAPOUR_CONFIG
    );


    public static final ParticleConfig UNDERWATER_RISING_BUBBLE_CONFIG = new ParticleConfig(
        new RandomFloatProvider(0.35f, 0.35f),
        new RandomIntProvider(100, 600),
        new RandomFloatProvider(0.02f, 0.02f)
    );

    public static final Supplier<SimpleParticleOptions> UNDERWATER_RISING_BUBBLE = () -> new SimpleParticleOptions(
        ParticleTypesRegistry.UNDERWATER_RISING_BUBBLE,
        UNDERWATER_RISING_BUBBLE_CONFIG
    );

    public static final Supplier<SimpleParticleOptions> UNDERWATER_RISING_BUBBLE_SMALL = () -> new SimpleParticleOptions(
        ParticleTypesRegistry.UNDERWATER_RISING_BUBBLE_SMALL,
        UNDERWATER_RISING_BUBBLE_CONFIG
    );
}
