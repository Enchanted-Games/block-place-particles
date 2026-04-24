package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.predicates.biome.BiomePredicate;
import games.enchanted.eg_particle_interactions.common.predicates.biome.BiomePredicates;
import games.enchanted.eg_particle_interactions.common.util.BiomeHelpers;
import net.minecraft.core.Vec3i;

public class BiomePredicateEmitterCondition extends EmitterCondition {
    public static final MapCodec<BiomePredicateEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Vec3i.CODEC.optionalFieldOf(EmitterCondition.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(BiomePredicateEmitterCondition::getPosOffset),
            BiomePredicates.CODEC.fieldOf(EmitterCondition.PREDICATE_FIELD).forGetter(BiomePredicateEmitterCondition::getBiomePredicate)
        ).apply(
            i,
            BiomePredicateEmitterCondition::new
        )
    );

    final Vec3i posOffset;
    final BiomePredicate biomePredicate;

    public BiomePredicateEmitterCondition(Vec3i posOffset, BiomePredicate biomePredicate) {
        this.posOffset = posOffset;
        this.biomePredicate = biomePredicate;
    }

    protected Vec3i getPosOffset() {
        return this.posOffset;
    }

    protected BiomePredicate getBiomePredicate() {
        return this.biomePredicate;
    }

    @Override
    public boolean matches(ParticleContext context) {
        return this.biomePredicate.matches(new BiomePredicate.BiomeContext(
            context.level(),
            BiomeHelpers.getBiomeAtPosition(context.level(), context.pos().offset(this.posOffset))
        ));
    }

    @Override
    public MapCodec<? extends BiomePredicateEmitterCondition> codec() {
        return CODEC;
    }
}