package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public class OnBounceEventType extends ParticleEventType {
    public static final MapCodec<OnBounceEventType> CODEC = MapCodec.unit(OnBounceEventType::new);

    @Override
    public void onParticleSpawn(ParticleInteractionsParticle particle) {
        particle.registerOnBounceConsumer(this::fire);
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
