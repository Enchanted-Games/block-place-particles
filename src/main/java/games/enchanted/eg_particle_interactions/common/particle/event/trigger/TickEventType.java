package games.enchanted.eg_particle_interactions.common.particle.event.trigger;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public class TickEventType extends ParticleEventType {
    public static final MapCodec<TickEventType> CODEC = MapCodec.unit(TickEventType::new);

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        this.fire(particle);
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
