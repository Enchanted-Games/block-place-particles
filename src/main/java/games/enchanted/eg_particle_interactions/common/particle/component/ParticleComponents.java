package games.enchanted.eg_particle_interactions.common.particle.component;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.component.type.GravityComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class ParticleComponents {
    public static final ParticleComponentRegistry.ComponentReference<GravityComponent> GRAVITY = register(
        ParticleInteractionsMod.id("gravity"),
        GravityComponent.CODEC,
        GravityComponent.STREAM_CODEC
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
