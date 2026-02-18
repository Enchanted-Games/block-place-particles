package games.enchanted.eg_particle_interactions.common.override_system.override.rule;

import games.enchanted.eg_particle_interactions.common.override_system.predicate.ObjectPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class OverrideRule<T> {
    private final List<OverrideRuleFile.AdditionsSection<T>> additions;
    private final List<ObjectPredicate<T>> exclusions;
    private final Identifier overrideId;

    public OverrideRule(List<OverrideRuleFile<T>> ruleFiles, Identifier overrideId) {
        List<OverrideRuleFile.AdditionsSection<T>> additionsList = new ArrayList<>();
        List<ObjectPredicate<T>> exclusionsList = new ArrayList<>();

        for (OverrideRuleFile<T> ruleFile : ruleFiles) {
            additionsList.addAll(ruleFile.getAdditions());
            exclusionsList.addAll(ruleFile.getExclusions());
        }

        this.additions = List.copyOf(additionsList);
        this.exclusions = List.copyOf(exclusionsList);

        this.overrideId = overrideId;
    }

    public OverridePreset.OverrideAndWeight getOverrideWeightForObject(T object) {
        int weight = 0;

        for (ObjectPredicate<T> exclusion : this.exclusions) {
            if(exclusion.matches(object)) return new OverridePreset.OverrideAndWeight(this.overrideId, 0);
        }

        for (OverrideRuleFile.AdditionsSection<T> addition : this.additions) {
            int additionWeight = addition.weight();

            for (ObjectPredicate<T> predicate : addition.predicates()) {
                if(!predicate.matches(object)) continue;
                weight += additionWeight;
            }
        }

        return new OverridePreset.OverrideAndWeight(this.overrideId, weight);
    }
}
