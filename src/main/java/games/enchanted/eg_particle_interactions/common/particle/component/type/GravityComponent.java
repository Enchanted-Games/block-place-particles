package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GravityComponent(RandomFloatProvider initialGravity) {
    private static final RandomFloatProvider INITIAL_GRAVITY_DEFAULT = new RandomFloatProvider(0.4f, 0.4f);

    public static final Codec<GravityComponent> CODEC = RecordCodecBuilder.create(
        i -> i.group(
            RandomFloatProvider.CODEC.optionalFieldOf("initial_gravity", INITIAL_GRAVITY_DEFAULT).forGetter(GravityComponent::initialGravity)
        ).apply(
            i,
            GravityComponent::new
        )
    );
    public static final StreamCodec<FriendlyByteBuf, GravityComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(RandomFloatProvider.CODEC),
        GravityComponent::initialGravity,
        GravityComponent::new
    );
}
