package games.enchanted.eg_particle_interactions.common.particle.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.event.action.lifetime.LifetimeEventActions;
import games.enchanted.eg_particle_interactions.common.particle.event.types.ParticleEventType;
import games.enchanted.eg_particle_interactions.common.particle.event.types.ParticleEventTypes;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

import java.util.List;

public class EventStack {
    private final List<Event> events;
    private final ParticleInteractionsParticle particle;

    public EventStack(List<Event> events, ParticleInteractionsParticle particle) {
        this.events = events;
        this.particle = particle;
        for (Event event : events) {
            event.eventType().setAction(event.action());
        }
    }

    public void tick() {
        this.events.forEach(event -> event.eventType().onParticleTick(this.particle));
    }

    public void particleSpawn() {
        this.events.forEach(event -> event.eventType().onParticleSpawn(this.particle));
    }

    public record Event(ParticleEventType eventType, EventAction action) {
        public static Codec<Event> LIFETIME_CODEC = RecordCodecBuilder.create(i ->
            i.group(
                ParticleEventTypes.CODEC.fieldOf("trigger").forGetter(Event::eventType),
                LifetimeEventActions.CODEC.fieldOf("action").forGetter(Event::action)
            ).apply(
                i,
                Event::new
            )
        );
    }
}
