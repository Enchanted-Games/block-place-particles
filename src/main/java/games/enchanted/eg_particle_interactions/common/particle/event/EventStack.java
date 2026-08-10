package games.enchanted.eg_particle_interactions.common.particle.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventActions;
import games.enchanted.eg_particle_interactions.common.particle.event.trigger.OnBounceEventType;
import games.enchanted.eg_particle_interactions.common.particle.event.trigger.ParticleEventType;
import games.enchanted.eg_particle_interactions.common.particle.event.trigger.ParticleEventTypes;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

import java.util.ArrayList;
import java.util.List;

public class EventStack {
    private final List<Event> events;
    private final ParticleInteractionsParticle particle;

    private final List<Event> onBounceEvents = new ArrayList<>();

    public EventStack(List<Event> events, ParticleInteractionsParticle particle) {
        this.events = events;
        this.particle = particle;

        for (Event event : events) {
            event.eventType().setAction(event.action());
            if(event.eventType() instanceof OnBounceEventType) {
                this.onBounceEvents.add(event);
            }
        }
    }

    public void tick() {
        this.events.forEach(event -> event.eventType().onParticleTick(this.particle));
    }

    public void particleSpawn() {
        this.events.forEach(event -> event.eventType().onParticleSpawn(this.particle));
    }

    public void particleBounce() {
        this.onBounceEvents.forEach(event -> event.eventType().fire(this.particle));
    }

    public record Event(ParticleEventType eventType, EventAction action) {
        public static Codec<Event> codec() {
            return RecordCodecBuilder.create(i ->
                i.group(
                    ParticleEventTypes.CODEC.fieldOf("trigger").forGetter(Event::eventType),
                    EventActions.CODEC.fieldOf("action").forGetter(Event::action)
                ).apply(
                    i,
                    Event::new
                )
            );
        }
    }
}
