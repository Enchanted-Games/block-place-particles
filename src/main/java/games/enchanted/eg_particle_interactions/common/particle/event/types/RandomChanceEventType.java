package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import games.enchanted.eg_particle_interactions.common.util.math.range.FloatRange;

public class RandomChanceEventType extends ParticleEventType {
    public static final MapCodec<RandomChanceEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.FLOAT.fieldOf("chance").forGetter(RandomChanceEventType::getChance),
            FloatRange.CODEC.optionalFieldOf("lifetime_percentage_range", new FloatRange(0, 1)).forGetter(RandomChanceEventType::getAgePercentageRange)
        ).apply(
            i,
            RandomChanceEventType::new
        )
    );

    final float chance;
    final FloatRange agePercentRange;

    RandomChanceEventType(float chance, FloatRange agePercentRange) {
        this.chance = chance;
        this.agePercentRange = agePercentRange;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        float age = particle.getAgePercent();
        if(this.agePercentRange.inRange(age)) return;
        if(MathHelper.randomBetween(0f, 1f) <= this.chance) {
            this.fire(particle);
        }
    }

    protected float getChance() {
        return this.chance;
    }

    protected FloatRange getAgePercentageRange() {
        return this.agePercentRange;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }
}
