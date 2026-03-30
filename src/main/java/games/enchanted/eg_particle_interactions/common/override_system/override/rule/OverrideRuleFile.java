package games.enchanted.eg_particle_interactions.common.override_system.override.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.ObjectPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.block.BlockPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.block.BlockPredicates;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.fluid.FluidPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.fluid.FluidPredicates;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.List;
import java.util.Map;

public class OverrideRuleFile<T, P extends ObjectPredicate<T>> {
    private static final String WEIGHTS_FIELD = "weights";
    private static final String EXCLUSIONS_FIELD = "exclusions";

    public static final Codec<OverrideRuleFile<BlockState, BlockPredicate>> BLOCKSTATE_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.unboundedMap(ParticleOrigin.CODEC, Codec.list(WeightsSection.codec(BlockPredicates.CODEC, "block_predicate")))
                .optionalFieldOf(WEIGHTS_FIELD, Map.of())
                .forGetter(OverrideRuleFile::getAdditions),
            Codec.unboundedMap(ParticleOrigin.CODEC, Codec.list(BlockPredicates.CODEC))
                .optionalFieldOf(EXCLUSIONS_FIELD, Map.of())
                .forGetter(OverrideRuleFile::getExclusions)
        )
        .apply(
            instance,
            OverrideRuleFile::new
        )
    );

    public static final Codec<OverrideRuleFile<FluidState, FluidPredicate>> FLUIDSTATE_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.unboundedMap(ParticleOrigin.CODEC, Codec.list(WeightsSection.codec(FluidPredicates.CODEC, "fluid_predicate")))
                .optionalFieldOf(WEIGHTS_FIELD, Map.of())
                .forGetter(OverrideRuleFile::getAdditions),
            Codec.unboundedMap(ParticleOrigin.CODEC, Codec.list(FluidPredicates.CODEC))
                .optionalFieldOf(EXCLUSIONS_FIELD, Map.of())
                .forGetter(OverrideRuleFile::getExclusions)
        )
        .apply(
            instance,
            OverrideRuleFile::new
        )
    );

    private final Map<ParticleOrigin, List<WeightsSection<T, P>>> additions;
    private final Map<ParticleOrigin, List<P>> exclusions;

    public OverrideRuleFile(Map<ParticleOrigin, List<WeightsSection<T, P>>> additions, Map<ParticleOrigin, List<P>> exclusions) {
        this.additions = additions;
        this.exclusions = exclusions;
    }

    Map<ParticleOrigin, List<WeightsSection<T, P>>> getAdditions() {
        return this.additions;
    }

    Map<ParticleOrigin, List<P>> getExclusions() {
        return this.exclusions;
    }


    public record WeightsSection<T, P extends ObjectPredicate<T>>(int weight, List<P> predicates) {
        public static <T, P extends ObjectPredicate<T>> Codec<WeightsSection<T, P>> codec(Codec<P> predicateCodec, String predicateFieldName) {
            return RecordCodecBuilder.create(instance ->
                instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 1).forGetter(WeightsSection::weight),
                    ExtraCodecs.compactListCodec(predicateCodec).fieldOf(predicateFieldName).forGetter(WeightsSection::predicates)
                )
                .apply(
                    instance,
                    WeightsSection::new
                )
            );
        }
    }
}
