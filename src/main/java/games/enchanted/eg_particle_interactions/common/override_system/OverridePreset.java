package games.enchanted.eg_particle_interactions.common.override_system;

import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverridePreset {
    private static final Map<List<OverrideAndWeight>, OverridePreset> EXISTING_PRESETS = new HashMap<>();

    public static final OverridePreset DEFAULT = OverridePreset.getOrCreate(List.of(
        new OverrideAndWeight(ParticleOverrides.VANILLA_OVERRIDE_ID, 1)
    ));

    private final int totalWeights;
    private final List<OverrideAndWeight> overrides;

    private OverridePreset(List<OverrideAndWeight> overrides) {
        this.overrides = overrides;
        final int[] x = {0};
        this.overrides.forEach(overrideAndWeight -> x[0] += overrideAndWeight.weight());
        this.totalWeights = x[0];
    }

    protected boolean isEmpty() {
        return this.overrides.isEmpty();
    }

    public ParticleOverride getRandom() {
        if(this.isEmpty()) {
            return ParticleOverrides.getOverrideOrFallback(ParticleOverrides.FALLBACK_OVERRIDE_ID);
        }

        int rand = (int) Math.round(Math.random() * this.totalWeights);
        Identifier overrideID = this.overrides.getFirst().overrideID();
        for (OverrideAndWeight randomOverride : this.overrides) {
            if (rand < randomOverride.weight()) {
                overrideID = randomOverride.overrideID();
                break;
            }
            rand -= randomOverride.weight();
        }

        return ParticleOverrides.getOverrideOrFallback(overrideID);
    }

    public static OverridePreset getOrCreate(List<OverrideAndWeight> overrides) {
        if (OverridePreset.EXISTING_PRESETS.containsKey(overrides)) {
            return OverridePreset.EXISTING_PRESETS.get(overrides);
        }

        var preset = new OverridePreset(overrides);
        OverridePreset.EXISTING_PRESETS.put(overrides, preset);
        return preset;
    }

    public record OverrideAndWeight(Identifier overrideID, int weight) {
    }
}
