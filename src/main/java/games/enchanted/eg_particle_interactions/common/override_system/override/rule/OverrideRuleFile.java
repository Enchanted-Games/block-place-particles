package games.enchanted.eg_particle_interactions.common.override_system.override.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.ObjectPredicate;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class OverrideRuleFile<T> {
    private static final String WEIGHTS_FIELD = "weights";
    private static final String EXCLUSIONS_FIELD = "exclusions";

    public static final Codec<OverrideRuleFile<BlockState>> BLOCKSTATE_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.list(AdditionsSection.codec(BlockStatePredicate.CODEC, "block_predicate"))
                .optionalFieldOf(WEIGHTS_FIELD, List.of())
                .forGetter(OverrideRuleFile::getAdditions),
            Codec.list(BlockStatePredicate.CODEC)
                .optionalFieldOf(EXCLUSIONS_FIELD, List.of())
                .forGetter(OverrideRuleFile::getExclusions)
        )
        .apply(
            instance,
            OverrideRuleFile::new
        )
    );

    private final List<AdditionsSection<T>> additions;
    private final List<ObjectPredicate<T>> exclusions;

    public OverrideRuleFile(List<AdditionsSection<T>> additions, List<ObjectPredicate<T>> exclusions) {
        this.additions = additions;
        this.exclusions = exclusions;
    }

    List<AdditionsSection<T>> getAdditions() {
        return this.additions;
    }

    List<ObjectPredicate<T>> getExclusions() {
        return this.exclusions;
    }


    public record AdditionsSection<T>(int weight, List<ObjectPredicate<T>> predicates) {
        public static <O> Codec<AdditionsSection<O>> codec(Codec<ObjectPredicate<O>> predicateCodec, String predicateFieldName) {
            return RecordCodecBuilder.create(instance ->
                instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 1).forGetter(AdditionsSection::weight),
                    ExtraCodecs.compactListCodec(predicateCodec).fieldOf(predicateFieldName).forGetter(AdditionsSection::predicates)
                )
                .apply(
                    instance,
                    AdditionsSection::new
                )
            );
        }
    }
}
