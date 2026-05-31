package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public class AfterLifetimePercentEventType extends ParticleEventType {
    public static final MapCodec<AfterLifetimePercentEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.BOOL.optionalFieldOf("oneshot", true).forGetter(AfterLifetimePercentEventType::isOneShot),
            Codec.FLOAT.fieldOf("lifetime_percentage").forGetter(AfterLifetimePercentEventType::getAgePercent)
        ).apply(
            i,
            AfterLifetimePercentEventType::new
        )
    );

    final boolean oneshot;
    final float agePercent;

    AfterLifetimePercentEventType(boolean oneshot, float agePercent) {
        this.oneshot = oneshot;
        this.agePercent = agePercent;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        if(this.oneshot && particle.getAgePercent() == this.agePercent) {
            this.fire(particle);
        } else if(!this.oneshot && particle.getAgePercent() >= this.agePercent) {
            this.fire(particle);
        }
    }

    protected boolean isOneShot() {
        return this.oneshot;
    }

    protected float getAgePercent() {
        return this.agePercent;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
