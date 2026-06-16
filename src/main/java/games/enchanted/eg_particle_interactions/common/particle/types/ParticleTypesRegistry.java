package games.enchanted.eg_particle_interactions.common.particle.types;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.types.options.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.options.RandomDistributionEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.options.SimpleParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.arc.ArcEmitter;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.random_distribution.RandomDistributionEmitter;
import games.enchanted.eg_particle_interactions.common.particle.types.vanilla.BlockParticleOptionWrapper;
import games.enchanted.eg_particle_interactions.common.particle.types.vanilla.CustomMovementTerrainParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ParticleTypesRegistry {
    private static final BiMap<Identifier, PIParticleType<? extends PIParticleOptions>> TYPES = HashBiMap.create();
    private static final Map<PIParticleType<? extends PIParticleOptions>, PIParticleProvider<? extends PIParticleOptions>> PROVIDERS_BY_TYPE = new HashMap<>();

    private static final Codec<PIParticleType<? extends PIParticleOptions>> NAME_CODEC = ModCodecs.IDENTIFIER.flatXmap(
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

    public static final PIParticleType<RandomDistributionEmitterOptions> DISTRIBUTION_EMITTER = register(
        RandomDistributionEmitter.Provider::new,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "distribution_emitter"),
        ParticleConfig.DEFAULT,
        RandomDistributionEmitterOptions::codec,
        RandomDistributionEmitterOptions::streamCodec
    );
    public static final PIParticleType<ArcEmitterOptions> ARC_EMITTER = register(
        ArcEmitter.Provider::new,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "arc_emitter"),
        ParticleConfig.DEFAULT,
        ArcEmitterOptions::codec,
        ArcEmitterOptions::streamCodec
    );

    // wrappers around various vanilla particles
    public static final PIParticleType<SimpleParticleOptions> BLOCK_CRACK = register(
        CustomMovementTerrainParticle.CrackingProvider::new,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block_crack"),
        ParticleConfig.DEFAULT,
        SimpleParticleOptions::codec,
        SimpleParticleOptions::streamCodec
    );
    public static final PIParticleType<SimpleParticleOptions> BLOCK = register(
        CustomMovementTerrainParticle.BlockProvider::new,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block"),
        ParticleConfig.DEFAULT,
        SimpleParticleOptions::codec,
        SimpleParticleOptions::streamCodec
    );
    public static final PIParticleType<SimpleParticleOptions> FALLING_DUST = register(
        () -> new BlockParticleOptionWrapper(() -> ParticleTypes.FALLING_DUST),
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "falling_dust"),
        ParticleConfig.DEFAULT,
        SimpleParticleOptions::codec,
        SimpleParticleOptions::streamCodec
    );


    private static <T extends PIParticleOptions> PIParticleType<T> register(
        PIProviderCreator<T> providerCreator,
        Identifier id,
        ParticleConfig defaultConfig,
        CodecGetter<T> codecGetter,
        StreamCodecGetter<T> streamCodecGetter
    ) {
        return register(providerCreator, id, defaultConfig, ParticleComponentMap.EMPTY, codecGetter, streamCodecGetter);
    }

    private static <T extends PIParticleOptions> PIParticleType<T> register(
        PIProviderCreator<T> providerCreator,
        Identifier id,
        ParticleConfig defaultConfig,
        ParticleComponentMap components,
        CodecGetter<T> codecGetter,
        StreamCodecGetter<T> streamCodecGetter
    ) {
        PIParticleType<T> type = new PIParticleType<>(components) {
            public MapCodec<T> codec() {
                return codecGetter.create(this, defaultConfig);
            }
        };
        registerType(type, id, providerCreator.create());
        return type;
    }

    private static void registerType(PIParticleType<? extends PIParticleOptions> type, Identifier id, PIParticleProvider<?> provider) {
        TYPES.put(id, type);
        PROVIDERS_BY_TYPE.put(type, provider);
    }

    @FunctionalInterface
    public interface PIProviderCreator<T extends PIParticleOptions> {
        PIParticleProvider<T> create();
    }

    @FunctionalInterface
    public interface CodecGetter<T extends PIParticleOptions> {
        MapCodec<T> create(PIParticleType<T> type, ParticleConfig defaultConfig);
    }

    @FunctionalInterface
    public interface StreamCodecGetter<T extends PIParticleOptions> {
        StreamCodec<? super RegistryFriendlyByteBuf, T> create(PIParticleType<T> type, ParticleConfig defaultConfig);
    }


    public static <T extends PIParticleOptions> PIParticleProvider<T> getProviderOrThrow(PIParticleType<T> type) {
        if(!PROVIDERS_BY_TYPE.containsKey(type)) {
            throw new RuntimeException("Tried to get provider for unregistered particle type");
        }
        //noinspection unchecked mmm generics
        return (PIParticleProvider<T>) PROVIDERS_BY_TYPE.get(type);
    }

    public static <T extends PIParticleOptions> @Nullable PIParticleProvider<T> getProvider(PIParticleType<T> type) {
        if(!PROVIDERS_BY_TYPE.containsKey(type)) {
            return null;
        }
        //noinspection unchecked
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
        //noinspection unchecked
        return (PIParticleType<T>) TYPES.get(id);
    }

    public static <T extends PIParticleOptions> PIParticleType<T> getTypeOrThrow(Identifier id) {
        if(!TYPES.containsKey(id)) {
            throw new RuntimeException("Tried to get id for unregistered particle type");
        }
        //noinspection unchecked
        return (PIParticleType<T>) TYPES.get(id);
    }

    public static void init() {
    }
}
