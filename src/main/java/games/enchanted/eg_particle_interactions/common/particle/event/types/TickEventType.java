package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

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
