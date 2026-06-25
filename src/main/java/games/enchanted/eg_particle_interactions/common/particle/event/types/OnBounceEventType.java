package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.MapCodec;

public class OnBounceEventType extends ParticleEventType {
    public static final MapCodec<OnBounceEventType> CODEC = MapCodec.unit(OnBounceEventType::new);

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
