package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.particle.event.EventStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record EventsComponent(List<EventStack.Event> events) {
    public static final Codec<EventsComponent> CODEC = EventStack.Event.codec().listOf().xmap(
        EventsComponent::new,
        EventsComponent::events
    );
    public static final StreamCodec<FriendlyByteBuf, EventsComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(EventStack.Event.codec().listOf()),
        EventsComponent::events,
        EventsComponent::new
    );
}
