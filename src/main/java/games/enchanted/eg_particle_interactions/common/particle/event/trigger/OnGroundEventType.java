package games.enchanted.eg_particle_interactions.common.particle.event.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public class OnGroundEventType extends ParticleEventType {
    public static final MapCodec<OnGroundEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.BOOL.optionalFieldOf("oneshot", true).forGetter(OnGroundEventType::isOneShot)
        ).apply(
            i,
            OnGroundEventType::new
        )
    );

    final boolean oneshot;

    OnGroundEventType(boolean oneshot) {
        this.oneshot = oneshot;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        if(this.isOneShot() ? particle.isOnGroundOneshot() : particle.isOnGround()) this.fire(particle);
    }

    protected boolean isOneShot() {
        return this.oneshot;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
