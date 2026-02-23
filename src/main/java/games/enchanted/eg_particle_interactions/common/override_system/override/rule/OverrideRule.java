package games.enchanted.eg_particle_interactions.common.override_system.override.rule;

import games.enchanted.eg_particle_interactions.common.override_system.predicate.ObjectPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.particle.overrides.ParticleOrigin;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverrideRule<T> {
    private final Map<ParticleOrigin, List<OverrideRuleFile.WeightsSection<T>>> additions;
    private final Map<ParticleOrigin, List<ObjectPredicate<T>>> exclusions;
    private final Identifier overrideId;

    public OverrideRule(List<OverrideRuleFile<T>> ruleFiles, Identifier overrideId) {
        Map<ParticleOrigin, List<OverrideRuleFile.WeightsSection<T>>> combinedAdditions = new HashMap<>();
        Map<ParticleOrigin, List<ObjectPredicate<T>>> exclusionsList = new HashMap<>();

        for (OverrideRuleFile<T> ruleFile : ruleFiles) {
            appendRules(ruleFile.getAdditions(), combinedAdditions);
            appendRules(ruleFile.getExclusions(), exclusionsList);
        }

        this.additions = combinedAdditions;
        this.exclusions = exclusionsList;

        this.overrideId = overrideId;
    }

    private static <R> void appendRules(Map<ParticleOrigin, List<R>> input, Map<ParticleOrigin, List<R>> output) {
        for (Map.Entry<ParticleOrigin, List<R>> entry : input.entrySet()) {
            if(output.containsKey(entry.getKey())) {
                output.get(entry.getKey()).addAll(entry.getValue());
            }
            output.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    public OverridePreset.OverrideAndWeight getOverrideWeightForObject(T object, ParticleOrigin origin) {
        int weight = 0;

        ParticleOrigin effectiveOrigin = this.exclusions.containsKey(origin) ? origin : ParticleOrigin.DEFAULT;
        if(!this.exclusions.isEmpty() && this.exclusions.containsKey(effectiveOrigin)) {
            for (ObjectPredicate<T> exclusion : this.exclusions.get(effectiveOrigin)) {
                if(exclusion.matches(object)) return new OverridePreset.OverrideAndWeight(this.overrideId, 0);
            }
        }

        effectiveOrigin = this.additions.containsKey(origin) ? origin : ParticleOrigin.DEFAULT;
        if(!this.additions.isEmpty() && this.additions.containsKey(effectiveOrigin)) {
            for (OverrideRuleFile.WeightsSection<T> addition : this.additions.get(effectiveOrigin)) {
                int additionWeight = addition.weight();

                for (ObjectPredicate<T> predicate : addition.predicates()) {
                    if (!predicate.matches(object)) continue;
                    weight += additionWeight;
                }
            }
        }

        return new OverridePreset.OverrideAndWeight(this.overrideId, weight);
    }
}
