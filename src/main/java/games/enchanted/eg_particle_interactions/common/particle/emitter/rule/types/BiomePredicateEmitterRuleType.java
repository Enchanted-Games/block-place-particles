package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.predicates.biome.BiomePredicate;
import games.enchanted.eg_particle_interactions.common.predicates.biome.BiomePredicates;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidPredicate;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import games.enchanted.eg_particle_interactions.common.util.BiomeHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class BiomePredicateEmitterRuleType extends EmitterRuleType {
    public static final MapCodec<BiomePredicateEmitterRuleType> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Emitters.CODEC.fieldOf(EmitterRuleType.EMITTER_FIELD).forGetter(EmitterRuleType::getEmitter),
            Vec3i.CODEC.optionalFieldOf(EmitterRuleType.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(BiomePredicateEmitterRuleType::getPosOffset),
            BiomePredicates.CODEC.fieldOf(EmitterRuleType.PREDICATE_FIELD).forGetter(BiomePredicateEmitterRuleType::getBiomePredicate)
        ).apply(
            i,
            BiomePredicateEmitterRuleType::new
        )
    );

    final Vec3i posOffset;
    final BiomePredicate biomePredicate;

    public BiomePredicateEmitterRuleType(Emitter emitter, Vec3i posOffset, BiomePredicate biomePredicate) {
        super(emitter);
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
    public MapCodec<? extends BiomePredicateEmitterRuleType> codec() {
        return CODEC;
    }
}