package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public abstract class ParticleEventType {
    private EventAction action;

    public final void setAction(EventAction action) {
        this.action = action;
    }

    public void onParticleSpawn(ParticleInteractionsParticle particle) {
    }

    public void onParticleTick(ParticleInteractionsParticle particle) {
    }

    public final void fire(ParticleInteractionsParticle particle) {
        if(this.action == null) return;
        this.action.onFire(particle);
    }

    public abstract MapCodec<? extends ParticleEventType> codec();
}
