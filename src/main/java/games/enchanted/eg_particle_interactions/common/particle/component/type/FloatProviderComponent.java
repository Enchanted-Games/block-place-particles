package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FloatProviderComponent(RandomFloatProvider provider) {
    public static final FloatProviderComponent ZERO = new FloatProviderComponent(new RandomFloatProvider(0f, 0f));
    public static final FloatProviderComponent ONE = new FloatProviderComponent(new RandomFloatProvider(1f, 1f));

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
