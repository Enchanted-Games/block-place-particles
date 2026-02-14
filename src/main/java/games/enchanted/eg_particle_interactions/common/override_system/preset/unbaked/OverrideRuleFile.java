package games.enchanted.eg_particle_interactions.common.override_system.preset.unbaked;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class OverrideRuleFile<T> {
    public static final Codec<OverrideRuleFile<BlockState>> BLOCKSTATE_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.list(AdditionsSection.codec(BlockStatePredicate.CODEC, "block_predicate"))
                .optionalFieldOf("weights", List.of())
                .forGetter(OverrideRuleFile::getAdditions),
            Codec.list(BlockStatePredicate.CODEC)
                .optionalFieldOf("removals", List.of())
                .forGetter(OverrideRuleFile::getRemovals)
        )
        .apply(
            instance,
            OverrideRuleFile::new
        )
    );

    private final List<AdditionsSection<T>> additions;
    private final List<ObjectPredicate<T>> removals;
    @Nullable private Identifier overrideId;

    public OverrideRuleFile(List<AdditionsSection<T>> additions, List<ObjectPredicate<T>> removals) {
        this.additions = additions;
        this.removals = removals;
    }

    public void setOverrideId(Identifier overrideId) {
        this.overrideId = overrideId;
    }

    protected List<AdditionsSection<T>> getAdditions() {
        return this.additions;
    }

    protected List<ObjectPredicate<T>> getRemovals() {
        return this.removals;
    }

    public OverridePreset.OverrideAndWeight getOverrideWeightForObject(T object) {
        int weight = 0;

        for (ObjectPredicate<T> removal : this.removals) {
            if(removal.matches(object)) return new OverridePreset.OverrideAndWeight(this.overrideId, 0);
        }

        for (AdditionsSection<T> addition : this.additions) {
            int additionWeight = addition.weight();

            for (ObjectPredicate<T> predicate : addition.predicates()) {
                if(!predicate.matches(object)) continue;
                weight += additionWeight;
            }
        }

        return new OverridePreset.OverrideAndWeight(this.overrideId, weight);
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
