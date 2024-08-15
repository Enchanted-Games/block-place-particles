package games.enchanted.blockplaceparticles.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.blockplaceparticles.BlockPlaceParticlesConstants;
import games.enchanted.blockplaceparticles.CommonEntrypoint;
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

@SuppressWarnings("unchecked")
public class ModParticleTypes {
    public static SimpleParticleType FALLING_CHERRY_PETAL;
    public static ParticleType<BlockParticleOption> FALLING_TINTED_LEAF;

    public static void registerParticles() {
        FALLING_CHERRY_PETAL = register((SpriteParticleProviderRegistration) FallingPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(BlockPlaceParticlesConstants.MOD_ID, "falling_cherry_leaves"), false);
        FALLING_TINTED_LEAF = register((SpriteParticleProviderRegistration) FallingTintedPetal.Provider::new, ResourceLocation.fromNamespaceAndPath(BlockPlaceParticlesConstants.MOD_ID, "falling_tinted_leaves"), false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
    }

    private static SimpleParticleType register(SpriteParticleProviderRegistration<SimpleParticleType> provider, ResourceLocation particleID, boolean alwaysShow) {
        SimpleParticleType registeredParticleType = Registry.register(BuiltInRegistries.PARTICLE_TYPE, particleID, Services.PLATFORM.createNewSimpleParticle(alwaysShow));
        Services.PLATFORM.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }

    private static <T extends ParticleOptions> ParticleType<T> register(SpriteParticleProviderRegistration<T> provider, ResourceLocation particleID, boolean alwaysShow, final Function<ParticleType<T>, MapCodec<T>> codecGetter, final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter) {
        ParticleType<T> registeredParticleType = CommonEntrypoint.register(Registries.PARTICLE_TYPE, () -> new ParticleType<T>(alwaysShow) {
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
