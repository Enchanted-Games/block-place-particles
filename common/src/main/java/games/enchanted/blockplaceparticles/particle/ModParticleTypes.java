package games.enchanted.blockplaceparticles.particle;

import com.mojang.serialization.MapCodec;
import games.enchanted.blockplaceparticles.BlockPlaceParticlesConstants;
import games.enchanted.blockplaceparticles.CommonEntrypoint;
import games.enchanted.blockplaceparticles.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ModParticleTypes {
    public static ParticleType<SimpleParticleType> FALLING_CHERRY_PETAL;

    public static void registerParticles() {
        FALLING_CHERRY_PETAL = register(ResourceLocation.fromNamespaceAndPath(BlockPlaceParticlesConstants.MOD_ID, "falling_cherry_leaves"), false);
    }

    private static SimpleParticleType register(ResourceLocation name, boolean alwaysShow) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, name, Services.PLATFORM.createNewSimpleParticle(alwaysShow));
    }

    private static <T extends ParticleOptions> ParticleType<T> register(ResourceLocation name, boolean alwaysShow, final Function<ParticleType<T>, MapCodec<T>> codecGetter, final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter) {
        return CommonEntrypoint.register(Registries.PARTICLE_TYPE, () -> new ParticleType<T>(alwaysShow) {
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return packetCodecGetter.apply(this);
            }
        }, name);
    }
}
