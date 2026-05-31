package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public class OnSpawnEventType extends ParticleEventType {
    public static final MapCodec<OnSpawnEventType> CODEC = MapCodec.unit(new OnSpawnEventType());

    OnSpawnEventType() {
    }

    @Override
    public void onParticleSpawn(ParticleInteractionsParticle particle) {
        this.fire(particle);
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
