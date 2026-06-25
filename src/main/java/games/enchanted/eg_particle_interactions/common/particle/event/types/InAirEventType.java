package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public class InAirEventType extends ParticleEventType {
    public static final MapCodec<InAirEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.BOOL.optionalFieldOf("oneshot", true).forGetter(InAirEventType::isOneShot)
        ).apply(
            i,
            InAirEventType::new
        )
    );

    final boolean oneshot;

    InAirEventType(boolean oneshot) {
        this.oneshot = oneshot;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        if(this.isOneShot() ? particle.isInAirOneshot() : !particle.isOnGround()) this.fire(particle);
    }

    protected boolean isOneShot() {
        return this.oneshot;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
