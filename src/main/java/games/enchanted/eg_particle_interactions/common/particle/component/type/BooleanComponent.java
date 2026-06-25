package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BooleanComponent(boolean value) {
    public static final Codec<BooleanComponent> CODEC = Codec.BOOL.xmap(
        BooleanComponent::new,
        BooleanComponent::value
    );
    public static final StreamCodec<FriendlyByteBuf, BooleanComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(Codec.BOOL),
        BooleanComponent::value,
        BooleanComponent::new
    );
}
