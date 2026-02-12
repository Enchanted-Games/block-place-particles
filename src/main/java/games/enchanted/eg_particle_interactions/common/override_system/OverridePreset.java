package games.enchanted.eg_particle_interactions.common.override_system;

import games.enchanted.eg_particle_interactions.common.override_system.manager.ParticleOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverridePreset {
    public static final OverridePreset DEFAULT = new OverridePreset(List.of(
        new OverrideAndWeight(ParticleOverrides.VANILLA, 1)
    ));

    private static final Map<List<OverrideAndWeight>, OverridePreset> EXISTING_PRESETS = new HashMap<>();

    private final int totalWeights;
    private final List<OverrideAndWeight> overrides;

    private OverridePreset(List<OverrideAndWeight> overrides) {
        this.overrides = overrides;
        final int[] x = {0};
        this.overrides.forEach(overrideAndWeight -> x[0] += overrideAndWeight.weight());
        this.totalWeights = x[0];
    }

    public ParticleOverride getRandom() {
        int rand = (int) Math.round(Math.random() * this.totalWeights);
        Identifier overrideID = this.overrides.getFirst().overrideID();
        for (OverrideAndWeight randomOverride : this.overrides) {
            if (rand < randomOverride.weight()) {
                overrideID = randomOverride.overrideID();
                break;
            }
            rand -= randomOverride.weight();
        }

        return ParticleOverrides.getOverrideFromId(overrideID);
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
