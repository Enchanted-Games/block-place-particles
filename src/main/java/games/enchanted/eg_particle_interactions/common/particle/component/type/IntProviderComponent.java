package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.particle.value.RandomIntProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record IntProviderComponent(RandomIntProvider provider) {
    public static final IntProviderComponent ZERO = new IntProviderComponent(new RandomIntProvider(0, 0));
    public static final IntProviderComponent ONE = new IntProviderComponent(new RandomIntProvider(1, 1));

    public static final Codec<IntProviderComponent> CODEC = RandomIntProvider.CODEC.xmap(
        IntProviderComponent::new,
        IntProviderComponent::provider
    );
    public static final StreamCodec<FriendlyByteBuf, IntProviderComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(RandomIntProvider.CODEC),
        IntProviderComponent::provider,
        IntProviderComponent::new
    );
}
