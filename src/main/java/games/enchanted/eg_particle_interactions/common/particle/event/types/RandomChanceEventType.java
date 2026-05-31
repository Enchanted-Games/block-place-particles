package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;
import org.joml.Vector2f;

import java.util.List;

public class RandomChanceEventType extends ParticleEventType {
    public static final MapCodec<RandomChanceEventType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.FLOAT.fieldOf("chance").forGetter(RandomChanceEventType::getChance),
            AgeRange.CODEC.optionalFieldOf("lifetime_percentage_range", new AgeRange(0, 1)).forGetter(RandomChanceEventType::getAgePercentageRange)
        ).apply(
            i,
            RandomChanceEventType::new
        )
    );

    final float chance;
    final AgeRange agePercentRange;

    RandomChanceEventType(float chance, AgeRange agePercentRange) {
        this.chance = chance;
        this.agePercentRange = agePercentRange;
    }

    @Override
    public void onParticleTick(ParticleInteractionsParticle particle) {
        float age = particle.getAgePercent();
        if(age <= this.agePercentRange.min() || age >= this.agePercentRange.max()) return;
        if(MathHelpers.randomBetween(0f, 1f) <= this.chance) {
            this.fire(particle);
        }
    }

    protected float getChance() {
        return this.chance;
    }

    protected AgeRange getAgePercentageRange() {
        return this.agePercentRange;
    }

    @Override
    public MapCodec<? extends ParticleEventType> codec() {
        return CODEC;
    }

    protected record AgeRange(float min, float max) {
        static final Codec<AgeRange> CODEC = Codec.FLOAT.listOf().comapFlatMap(
            input -> Util.fixedSize(input, 2).map(
                list -> new AgeRange(list.get(0), list.get(1))
            ),
            range -> List.of(range.min(), range.max())
        );
    }
}
