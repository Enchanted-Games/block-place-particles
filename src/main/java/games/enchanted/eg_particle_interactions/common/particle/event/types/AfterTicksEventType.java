package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public class AfterTicksEventType extends ParticleEventType {
    public static final MapCodec<AfterTicksEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.BOOL.optionalFieldOf("oneshot", true).forGetter(AfterTicksEventType::isOneShot),
            Codec.INT.fieldOf("ticks").forGetter(AfterTicksEventType::getTicks)
        ).apply(
            i,
            AfterTicksEventType::new
        )
    );

    final boolean oneshot;
    final int ticks;

    AfterTicksEventType(boolean oneshot, int ticks) {
        this.oneshot = oneshot;
        this.ticks = ticks;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        if(this.oneshot && particle.getAge() == this.ticks) {
            this.fire(particle);
        } else if(!this.oneshot && particle.getAge() >= this.ticks) {
            this.fire(particle);
        }
    }

    protected boolean isOneShot() {
        return this.oneshot;
    }

    protected int getTicks() {
        return this.ticks;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
