package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public abstract class EventAction {
    public abstract void onFire(ParticleInteractionsParticle particle);

    public abstract MapCodec<? extends EventAction> codec();
}
