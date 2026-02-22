package games.enchanted.eg_particle_interactions.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.ParticleInteractionsEmitter;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class SparkParticleOptions implements PIParticleOptions {
    private final PIParticleType<SparkParticleOptions> type;
    private final @Nullable Emitter speckEmitter;

    public static final SparkParticleOptions FLYING_SPARK = new SparkParticleOptions(
        ParticleTypesRegistry.FLYING_SPARK,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, ParticleTypesRegistry.SPARK_FLASH)
    );
    public static final SparkParticleOptions FLOATING_SPARK = new SparkParticleOptions(
        ParticleTypesRegistry.FLOATING_SPARK,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, ParticleTypesRegistry.SPARK_FLASH)
    );

    public static final SparkParticleOptions FLYING_SOUL_SPARK = new SparkParticleOptions(
        ParticleTypesRegistry.FLYING_SOUL_SPARK,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, ParticleTypesRegistry.SOUL_SPARK_FLASH)
    );
    public static final SparkParticleOptions FLOATING_SOUL_SPARK = new SparkParticleOptions(
        ParticleTypesRegistry.FLOATING_SOUL_SPARK,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, ParticleTypesRegistry.SOUL_SPARK_FLASH)
    );

    public SparkParticleOptions(PIParticleType<SparkParticleOptions> type, @Nullable Emitter flashEmitter) {
        this.type = type;
        this.speckEmitter = flashEmitter;
    }

    private static Codec<SparkParticleOptions> createCodec(PIParticleType<SparkParticleOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<SparkParticleOptions> instance) ->
            instance.group(
                Emitters.CODEC.optionalFieldOf("flash_emitter").forGetter(dustParticleOptions -> Optional.ofNullable(dustParticleOptions.getFlashEmitter()))
            ).apply(
                instance,
                (
                    emitter
                ) -> new SparkParticleOptions(
                    type,
                    emitter.orElse(null)
                )
            )
        );
    }

    public static MapCodec<SparkParticleOptions> codec(PIParticleType<SparkParticleOptions> type) {
        return createCodec(type).fieldOf("spark_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, SparkParticleOptions> streamCodec(PIParticleType<SparkParticleOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull PIParticleType<?> type() {
        return this.type;
    }

    public @Nullable Emitter getFlashEmitter() {
        return this.speckEmitter;
    }
}
