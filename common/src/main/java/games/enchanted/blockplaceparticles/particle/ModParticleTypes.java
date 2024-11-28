package games.enchanted.blockplaceparticles.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.particle.bubble.UnderwaterRisingBubble;
import games.enchanted.blockplaceparticles.particle.dust.FallingDust;
import games.enchanted.blockplaceparticles.particle.dust.FallingColouredDust;
import games.enchanted.blockplaceparticles.particle.option.ParticleEmitterOptions;
import games.enchanted.blockplaceparticles.particle.petal.FallingPetal;
import games.enchanted.blockplaceparticles.particle.petal.FallingColouredPetal;
import games.enchanted.blockplaceparticles.particle.spark.FlyingSpark;
import games.enchanted.blockplaceparticles.particle.spark.SparkEmitter;
import games.enchanted.blockplaceparticles.particle.spark.SparkFlash;
import games.enchanted.blockplaceparticles.particle.splash.BlockSplash;
import games.enchanted.blockplaceparticles.particle.splash.ColouredBucketSplash;
import games.enchanted.blockplaceparticles.particle.splash.LavaSplash;
import games.enchanted.blockplaceparticles.platform.Services;
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

@SuppressWarnings({"unchecked","rawtypes"})
public class ModParticleTypes {
    public static SimpleParticleType FALLING_CHERRY_PETAL;
    public static ParticleType<BlockParticleOption> FALLING_TINTED_LEAF;
    public static SimpleParticleType FALLING_AZALEA_LEAF;
    public static SimpleParticleType FALLING_FLOWERING_AZALEA_LEAF;
    public static ParticleType<BlockParticleOption> GRASS_BLADE;
    public static ParticleType<BlockParticleOption> HEAVY_GRASS_BLADE;
    public static SimpleParticleType MOSS_CLUMP;
    public static SimpleParticleType PALE_MOSS_CLUMP;
    public static SimpleParticleType DUST;
    public static SimpleParticleType DUST_SPECK;
    public static ParticleType<BlockParticleOption> TINTED_DUST;
    public static ParticleType<BlockParticleOption> TINTED_DUST_SPECK;

    public static ParticleType<BlockParticleOption> WATER_BUCKET_TINTED_SPLASH;
    public static SimpleParticleType LAVA_BUCKET_SPLASH;
    public static ParticleType<BlockParticleOption> GENERIC_FLUID_BUCKET_SPLASH;

    public static SimpleParticleType FLYING_SPARK;
    public static SimpleParticleType FLOATING_SPARK;
    public static SimpleParticleType FLYING_SOUL_SPARK;
    public static SimpleParticleType FLOATING_SOUL_SPARK;

    public static SimpleParticleType SPARK_FLASH;
    public static SimpleParticleType SOUL_SPARK_FLASH;

    public static SimpleParticleType UNDERWATER_RISING_BUBBLE;

    public static ParticleType<ParticleEmitterOptions> FLYING_SPARK_EMITTER;

    public static void registerParticles() {
        FALLING_CHERRY_PETAL = register((SpriteProviderReg) FallingPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "falling_cherry_leaves"), false);
        FALLING_TINTED_LEAF = register((SpriteProviderReg) FallingColouredPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "falling_tinted_leaves"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        FALLING_AZALEA_LEAF = register((SpriteProviderReg) FallingPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "falling_azalea_leaves"), false);
        FALLING_FLOWERING_AZALEA_LEAF = register((SpriteProviderReg) FallingPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "falling_flowering_azalea_leaves"), false);
        GRASS_BLADE = register((SpriteProviderReg) FallingColouredPetal.LargerSpriteProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "grass_blade"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        HEAVY_GRASS_BLADE = register((SpriteProviderReg) FallingColouredPetal.LargerSpriteMoreGravityProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "heavy_grass_blade"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        MOSS_CLUMP = register((SpriteProviderReg) FallingPetal.RandomisedSizeMoreGravityProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "moss_clump"), false);
        PALE_MOSS_CLUMP = register((SpriteProviderReg) FallingPetal.RandomisedSizeMoreGravityProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "pale_moss_clump"), false);
        DUST = register((SpriteProviderReg) FallingDust.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "dust"), false);
        DUST_SPECK = register((SpriteProviderReg) FallingDust.SpeckProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "dust_speck"), false);
        TINTED_DUST = register((SpriteProviderReg) FallingColouredDust.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "tinted_dust"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        TINTED_DUST_SPECK = register((SpriteProviderReg) FallingColouredDust.SpeckProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "tinted_dust_speck"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);

        WATER_BUCKET_TINTED_SPLASH = register((SpriteProviderReg) ColouredBucketSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "water_bucket_tinted_splash"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        LAVA_BUCKET_SPLASH = register((SpriteProviderReg) LavaSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "lava_bucket_splash"), false);
        GENERIC_FLUID_BUCKET_SPLASH = register((SpriteProviderReg) BlockSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "generic_fluid_bucket_splash"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);

        FLYING_SPARK = register((SpriteProviderReg) FlyingSpark.LongLifeSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "flying_spark"), false);
        FLOATING_SPARK = register((SpriteProviderReg) FlyingSpark.ShortLifeSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "floating_spark"), false);
        FLYING_SOUL_SPARK = register((SpriteProviderReg) FlyingSpark.LongLifeSoulSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "flying_soul_spark"), false);
        FLOATING_SOUL_SPARK = register((SpriteProviderReg) FlyingSpark.ShortLifeSoulSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "floating_soul_spark"), false);

        SPARK_FLASH = register((SpriteProviderReg) SparkFlash.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "spark_flash"), false);
        SOUL_SPARK_FLASH = register((SpriteProviderReg) SparkFlash.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "soul_spark_flash"), false);

        UNDERWATER_RISING_BUBBLE = register((SpriteProviderReg) UnderwaterRisingBubble.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "underwater_rising_bubble"), false);

        FLYING_SPARK_EMITTER = register((SpriteProviderReg) SparkEmitter.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "flying_spark_emitter"), true, ParticleEmitterOptions::codec, ParticleEmitterOptions::streamCodec);
    }

    private static SimpleParticleType register(SpriteProviderReg<SimpleParticleType> provider, ResourceLocation particleID, boolean alwaysShow) {
        SimpleParticleType registeredParticleType = Registry.register(BuiltInRegistries.PARTICLE_TYPE, particleID, Services.PLATFORM.createNewSimpleParticle(alwaysShow));
        Services.PLATFORM.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }

    private static <T extends ParticleOptions> ParticleType<T> register(SpriteProviderReg<T> provider, ResourceLocation particleID, boolean alwaysShow, final Function<ParticleType<T>, MapCodec<T>> codecGetter, final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter) {
        ParticleType<T> registeredParticleType = ParticleInteractionsMod.register(Registries.PARTICLE_TYPE, () -> new ParticleType<T>(alwaysShow) {
            public @NotNull MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return packetCodecGetter.apply(this);
            }
        }, particleID);
        Services.PLATFORM.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }

    @FunctionalInterface
    public interface SpriteProviderReg<T extends ParticleOptions> {
        ParticleProvider<T> create(SpriteSet spriteSet);
    }
}
