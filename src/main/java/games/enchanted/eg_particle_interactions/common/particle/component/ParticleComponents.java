package games.enchanted.eg_particle_interactions.common.particle.component;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.component.type.FloatProviderComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class ParticleComponents {
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> GRAVITY_INITIAL = register(
        ParticleInteractionsMod.id("gravity/initial"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> GRAVITY_DECAY = register(
        ParticleInteractionsMod.id("gravity/decay"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );

    public static <T> ParticleComponentRegistry.ComponentReference<T> register(
        Identifier id,
        Codec<T> codec,
        StreamCodec<? extends FriendlyByteBuf, T> streamCodec
    ) {
        return ParticleComponentRegistry.register(id, ParticleComponent.create(codec, streamCodec));
    }

    public static void init() {
    }
}
