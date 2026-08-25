package games.enchanted.eg_particle_interactions.common.particle.vanilla;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public class PIVanillaParticles {
    @Nullable
    public static ParticleType<PIVanillaParticleOptions> PI_PARTICLE_SPAWNER;

    static {
        PI_PARTICLE_SPAWNER = register(
            ParticleSpawnerVanillaParticle.Provider::new,
            ParticleInteractionsMod.id("custom"),
            PIVanillaParticleOptions::codec,
            PIVanillaParticleOptions::streamCodec
        );
    }

    private static @Nullable <T extends ParticleOptions> ParticleType<T> register(
        SpriteProviderReg<T> provider,
        Identifier particleID,
        final Function<ParticleType<T>, MapCodec<T>> codecGetter,
        final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter
    ) {
        if(GeneralOptions.PRESERVE_VANILLA_NETWORK_COMPATIBILITY.getValue()) {
            return null;
        }

        ParticleType<T> registeredParticleType = RegistryHelpers.register(BuiltInRegistries.PARTICLE_TYPE, new ParticleType<T>(false) {
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
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

    public static void init() {
    }
}
