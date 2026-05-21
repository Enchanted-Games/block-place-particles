package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.particle.event.EventStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record LifetimeEventsComponent(List<EventStack.Event> events) {
    public static final Codec<LifetimeEventsComponent> CODEC = EventStack.Event.LIFETIME_CODEC.listOf().xmap(
        LifetimeEventsComponent::new,
        LifetimeEventsComponent::events
    );
    public static final StreamCodec<FriendlyByteBuf, LifetimeEventsComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(EventStack.Event.LIFETIME_CODEC.listOf()),
        LifetimeEventsComponent::events,
        LifetimeEventsComponent::new
    );
}
