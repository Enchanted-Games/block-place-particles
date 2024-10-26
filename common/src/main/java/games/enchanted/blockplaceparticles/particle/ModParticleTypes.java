package games.enchanted.blockplaceparticles.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.particle.bubble.UnderwaterRisingBubble;
import games.enchanted.blockplaceparticles.particle.option.ParticleEmitterOptions;
import games.enchanted.blockplaceparticles.particle.petal.FallingPetal;
import games.enchanted.blockplaceparticles.particle.petal.FallingTintedOrAveragePetal;
import games.enchanted.blockplaceparticles.particle.spark.FloatingSparkEmitter;
import games.enchanted.blockplaceparticles.particle.spark.FlyingSpark;
import games.enchanted.blockplaceparticles.particle.spark.SparkEmitter;
import games.enchanted.blockplaceparticles.particle.splash.BlockSplash;
import games.enchanted.blockplaceparticles.particle.splash.BucketTintedSplash;
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

    public static ParticleType<BlockParticleOption> WATER_BUCKET_TINTED_SPLASH;
    public static SimpleParticleType LAVA_BUCKET_SPLASH;
    public static ParticleType<BlockParticleOption> GENERIC_FLUID_BUCKET_SPLASH;

    public static SimpleParticleType FLYING_SPARK;
    public static SimpleParticleType FLOATING_SPARK;
    public static SimpleParticleType FLOATING_SPARK_SHORT;
    public static SimpleParticleType FLYING_SOUL_SPARK;
    public static SimpleParticleType FLOATING_SOUL_SPARK;

    public static SimpleParticleType UNDERWATER_RISING_BUBBLE;

    public static ParticleType<ParticleEmitterOptions> FLYING_SPARK_EMITTER;
    public static ParticleType<ParticleEmitterOptions> FLOATING_SPARK_SHORT_EMITTER;

    public static void registerParticles() {
        FALLING_CHERRY_PETAL = register((SpriteParticleProviderRegistration) FallingPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "falling_cherry_leaves"), false);
        FALLING_TINTED_LEAF = register((SpriteParticleProviderRegistration) FallingTintedOrAveragePetal.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "falling_tinted_leaves"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        FALLING_AZALEA_LEAF = register((SpriteParticleProviderRegistration) FallingPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "falling_azalea_leaves"), false);
        FALLING_FLOWERING_AZALEA_LEAF = register((SpriteParticleProviderRegistration) FallingPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "falling_flowering_azalea_leaves"), false);
        GRASS_BLADE = register((SpriteParticleProviderRegistration) FallingTintedOrAveragePetal.LargerSpriteProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "grass_blade"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);

        WATER_BUCKET_TINTED_SPLASH = register((SpriteParticleProviderRegistration) BucketTintedSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "water_bucket_tinted_splash"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
        LAVA_BUCKET_SPLASH = register((SpriteParticleProviderRegistration) LavaSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "lava_bucket_splash"), false);
        GENERIC_FLUID_BUCKET_SPLASH = register((SpriteParticleProviderRegistration) BlockSplash.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "generic_fluid_bucket_splash"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);

        FLYING_SPARK = register((SpriteParticleProviderRegistration) FlyingSpark.LongLifeSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "flying_spark"), false);
        FLOATING_SPARK = register((SpriteParticleProviderRegistration) FlyingSpark.ShortLifeSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "floating_spark"), false);
        FLOATING_SPARK_SHORT = register((SpriteParticleProviderRegistration) FlyingSpark.ShortestLifeSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "floating_spark_short"), false);
        FLYING_SOUL_SPARK = register((SpriteParticleProviderRegistration) FlyingSpark.LongLifeSoulSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "flying_soul_spark"), false);
        FLOATING_SOUL_SPARK = register((SpriteParticleProviderRegistration) FlyingSpark.ShortLifeSoulSparkProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "floating_soul_spark"), false);

        UNDERWATER_RISING_BUBBLE = register((SpriteParticleProviderRegistration) UnderwaterRisingBubble.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "underwater_rising_bubble"), false);

        FLYING_SPARK_EMITTER = register((SpriteParticleProviderRegistration) SparkEmitter.Provider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "flying_spark_emitter"), false, ParticleEmitterOptions::codec, ParticleEmitterOptions::streamCodec);
        FLOATING_SPARK_SHORT_EMITTER = register((SpriteParticleProviderRegistration) FloatingSparkEmitter.ShorterLifeProvider::new, ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "floating_spark_short_emitter"), false, ParticleEmitterOptions::codec, ParticleEmitterOptions::streamCodec);
    }

    private static SimpleParticleType register(SpriteParticleProviderRegistration<SimpleParticleType> provider, ResourceLocation particleID, boolean alwaysShow) {
        SimpleParticleType registeredParticleType = Registry.register(BuiltInRegistries.PARTICLE_TYPE, particleID, Services.PLATFORM.createNewSimpleParticle(alwaysShow));
        Services.PLATFORM.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }

    private static <T extends ParticleOptions> ParticleType<T> register(SpriteParticleProviderRegistration<T> provider, ResourceLocation particleID, boolean alwaysShow, final Function<ParticleType<T>, MapCodec<T>> codecGetter, final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter) {
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
    public interface SpriteParticleProviderRegistration<T extends ParticleOptions> {
        ParticleProvider<T> create(SpriteSet spriteSet);
    }
}
