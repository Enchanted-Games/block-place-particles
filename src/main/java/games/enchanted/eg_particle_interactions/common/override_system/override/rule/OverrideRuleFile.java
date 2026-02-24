package games.enchanted.eg_particle_interactions.common.override_system.override.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.ObjectPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

public class OverrideRuleFile<T> {
    private static final String WEIGHTS_FIELD = "weights";
    private static final String EXCLUSIONS_FIELD = "exclusions";

    public static final Codec<OverrideRuleFile<BlockState>> BLOCKSTATE_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.unboundedMap(ParticleOrigin.CODEC, Codec.list(WeightsSection.codec(BlockStatePredicate.CODEC, "block_predicate")))
                .optionalFieldOf(WEIGHTS_FIELD, Map.of())
                .forGetter(OverrideRuleFile::getAdditions),
            Codec.unboundedMap(ParticleOrigin.CODEC, Codec.list(BlockStatePredicate.CODEC))
                .optionalFieldOf(EXCLUSIONS_FIELD, Map.of())
                .forGetter(OverrideRuleFile::getExclusions)
        )
        .apply(
            instance,
            OverrideRuleFile::new
        )
    );

    private final Map<ParticleOrigin, List<WeightsSection<T>>> additions;
    private final Map<ParticleOrigin, List<ObjectPredicate<T>>> exclusions;

    public OverrideRuleFile(Map<ParticleOrigin, List<WeightsSection<T>>> additions, Map<ParticleOrigin, List<ObjectPredicate<T>>> exclusions) {
        this.additions = additions;
        this.exclusions = exclusions;
    }

    Map<ParticleOrigin, List<WeightsSection<T>>> getAdditions() {
        return this.additions;
    }

    Map<ParticleOrigin, List<ObjectPredicate<T>>> getExclusions() {
        return this.exclusions;
    }


    public record WeightsSection<T>(int weight, List<ObjectPredicate<T>> predicates) {
        public static <O> Codec<WeightsSection<O>> codec(Codec<ObjectPredicate<O>> predicateCodec, String predicateFieldName) {
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
