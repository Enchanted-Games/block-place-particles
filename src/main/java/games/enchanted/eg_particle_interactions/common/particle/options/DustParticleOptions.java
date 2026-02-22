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
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class DustParticleOptions implements PIParticleOptions {
    public static final float DEFAULT_GRAVITY = 1.0f;

    public static final DustParticleOptions SNOWFLAKE_SPECK = new DustParticleOptions(
        ParticleTypesRegistry.SNOWFLAKE_SPECK,
        0.7f,
        null
    );
    public static final DustParticleOptions SNOWFLAKE = new DustParticleOptions(
        ParticleTypesRegistry.SNOWFLAKE,
        1.1f,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, SNOWFLAKE_SPECK)
    );

    public static final DustParticleOptions TINTED_DUST_SPECK = new DustParticleOptions(
        ParticleTypesRegistry.TINTED_DUST_SPECK,
        0.35f,
        null
    );
    public static final DustParticleOptions TINTED_DUST = new DustParticleOptions(
        ParticleTypesRegistry.TINTED_DUST,
        0.7f,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, TINTED_DUST_SPECK)
    );

    public static final DustParticleOptions REDSTONE = new DustParticleOptions(
        ParticleTypesRegistry.REDSTONE_DUST,
        0f,
        null
    );

    public static final DustParticleOptions BRUSH_DUST_SPECK = new DustParticleOptions(
        ParticleTypesRegistry.BRUSH_DUST_SPECK,
        0.35f,
        null
    );
    public static final DustParticleOptions BRUSH_DUST = new DustParticleOptions(
        ParticleTypesRegistry.BRUSH_DUST,
        0.7f,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, BRUSH_DUST_SPECK)
    );

    public static final DustParticleOptions ITEM_FRAME_DUST_SPECK = new DustParticleOptions(
        ParticleTypesRegistry.ITEM_FRAME_DUST_SPECK,
        0.35f,
        null
    );
    public static final DustParticleOptions ITEM_FRAME_DUST = new DustParticleOptions(
        ParticleTypesRegistry.ITEM_FRAME_DUST,
        0.7f,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, ITEM_FRAME_DUST_SPECK)
    );

    public static final DustParticleOptions GLOW_ITEM_FRAME_DUST_SPECK = new DustParticleOptions(
        ParticleTypesRegistry.GLOW_ITEM_FRAME_DUST_SPECK,
        0.35f,
        null
    );
    public static final DustParticleOptions GLOW_ITEM_FRAME_DUST = new DustParticleOptions(
        ParticleTypesRegistry.GLOW_ITEM_FRAME_DUST,
        0.7f,
        ParticleInteractionsEmitter.defaultAppearance(Emitter.VELOCITY_MULTIPLIER_DEFAULT, GLOW_ITEM_FRAME_DUST_SPECK)
    );

    private final PIParticleType<DustParticleOptions> type;
    private final float gravity;
    private final @Nullable Emitter speckEmitter;

    public DustParticleOptions(PIParticleType<DustParticleOptions> type, float gravity, @Nullable Emitter speckEmitter) {
        this.type = type;
        this.gravity = gravity;
        this.speckEmitter = speckEmitter;
    }

    private static Codec<DustParticleOptions> createCodec(PIParticleType<DustParticleOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<DustParticleOptions> instance) ->
            instance.group(
                Codec.FLOAT.optionalFieldOf("gravity", DEFAULT_GRAVITY).forGetter(DustParticleOptions::getGravity),
                Emitters.CODEC.optionalFieldOf("speck_emitter").forGetter(dustParticleOptions -> Optional.ofNullable(dustParticleOptions.getSpeckEmitter()))
            ).apply(
                instance,
                (
                    gravity,
                    emitter
                ) -> new DustParticleOptions(
                    type,
                    gravity,
                    emitter.orElse(null)
                )
            )
        );
    }

    public static MapCodec<DustParticleOptions> codec(PIParticleType<DustParticleOptions> type) {
        return createCodec(type).fieldOf("dust_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, DustParticleOptions> streamCodec(PIParticleType<DustParticleOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull PIParticleType<?> type() {
        return this.type;
    }

    public float getGravity() {
        return this.gravity;
    }

    public @Nullable Emitter getSpeckEmitter() {
        return this.speckEmitter;
    }
}
