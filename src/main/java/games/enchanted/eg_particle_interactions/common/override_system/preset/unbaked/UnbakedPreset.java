package games.enchanted.eg_particle_interactions.common.override_system.preset.unbaked;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnbakedPreset<T> {
    public static final Codec<UnbakedPreset<BlockState>> BLOCKSTATE_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.list(AdditionsSection.codec(BlockStatePredicate.CODEC, "block_predicate"))
                .optionalFieldOf("additions", List.of())
                .forGetter(UnbakedPreset::getAdditions),
            Codec.list(RemovalsSection.codec(BlockStatePredicate.CODEC, "block_predicate"))
                .optionalFieldOf("removals", List.of())
                .forGetter(UnbakedPreset::getRemovals)
        )
        .apply(
            instance,
            UnbakedPreset::new
        )
    );

    private final List<AdditionsSection<T>> additions;
    private final List<RemovalsSection<T>> removals;

    public UnbakedPreset(List<AdditionsSection<T>> additions, List<RemovalsSection<T>> removals) {
        this.additions = additions;
        this.removals = removals;
    }

    protected List<AdditionsSection<T>> getAdditions() {
        return this.additions;
    }

    protected List<RemovalsSection<T>> getRemovals() {
        return this.removals;
    }

    public Map<Identifier, Integer> getOverridesForObject(T object) {
        Map<Identifier, Integer> idToWeight = new HashMap<>();

        for (AdditionsSection<T> addition : this.additions) {
            Identifier overrideId = addition.overrideId();
            int weight = addition.weight();

            for (ObjectPredicate<T> predicate : addition.predicates()) {
                if(!predicate.matches(object)) continue;

                if(idToWeight.containsKey(overrideId)) {
                    Integer oldWeight = idToWeight.get(overrideId);
                    idToWeight.put(overrideId, oldWeight + weight);
                } else {
                    idToWeight.put(overrideId, weight);
                }
            }
        }

        for (RemovalsSection<T> removal : this.removals) {
            Identifier overrideId = removal.overrideId();
            if(!idToWeight.containsKey(overrideId)) continue;

            for (ObjectPredicate<T> predicate : removal.predicates()) {
                if(!predicate.matches(object)) continue;
                idToWeight.remove(overrideId);
            }
        }

        return idToWeight;
    }

    public record AdditionsSection<T>(int weight, Identifier overrideId, List<ObjectPredicate<T>> predicates) {
        public static <O> Codec<AdditionsSection<O>> codec(Codec<ObjectPredicate<O>> predicateCodec, String predicateFieldName) {
            return RecordCodecBuilder.create(instance ->
                instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 1).forGetter(AdditionsSection::weight),
                    Identifier.CODEC.fieldOf("particle_override").forGetter(AdditionsSection::overrideId),
                    ExtraCodecs.compactListCodec(predicateCodec).fieldOf(predicateFieldName).forGetter(AdditionsSection::predicates)
                )
                .apply(
                    instance,
                    AdditionsSection::new
                )
            );
        }
    }

    public record RemovalsSection<T>(Identifier overrideId, List<ObjectPredicate<T>> predicates) {
        public static <O> Codec<RemovalsSection<O>> codec(Codec<ObjectPredicate<O>> predicateCodec, String predicateFieldName) {
            return RecordCodecBuilder.create(instance ->
                instance.group(
                    Identifier.CODEC.fieldOf("particle_override").forGetter(RemovalsSection::overrideId),
                    ExtraCodecs.compactListCodec(predicateCodec).fieldOf(predicateFieldName).forGetter(RemovalsSection::predicates)
                )
                .apply(
                    instance,
                    RemovalsSection::new
                )
            );
        }
    }
}
