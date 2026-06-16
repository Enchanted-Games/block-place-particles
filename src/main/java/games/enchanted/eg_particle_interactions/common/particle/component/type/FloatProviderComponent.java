package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.particle.value.RandomFloatProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record FloatProviderComponent(RandomFloatProvider provider) {
    public static final FloatProviderComponent MINUS_ONE = new FloatProviderComponent(new RandomFloatProvider(List.of(-1f)));
    public static final FloatProviderComponent ZERO = new FloatProviderComponent(new RandomFloatProvider(List.of(0f)));
    public static final FloatProviderComponent ONE = new FloatProviderComponent(new RandomFloatProvider(List.of(1f)));

    public static final Codec<FloatProviderComponent> CODEC = RandomFloatProvider.CODEC.xmap(
        FloatProviderComponent::new,
        FloatProviderComponent::provider
    );
    public static final StreamCodec<FriendlyByteBuf, FloatProviderComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(RandomFloatProvider.CODEC),
        FloatProviderComponent::provider,
        FloatProviderComponent::new
    );
}
