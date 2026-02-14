package games.enchanted.eg_particle_interactions.common.override_system.manager;

import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.override_system.preset.unbaked.OverrideRuleFile;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class ParticleOverrideManager {
    private static final List<OverrideRuleFile<BlockState>> BLOCKSTATE_OVERRIDE_RULE_FILES = new ArrayList<>();
    private static final Map<BlockState, OverridePreset> BLOCKSTATE_TO_OVERRIDE = new HashMap<>();

    public static void registerOverrideWeights(BlockState state, OverridePreset override) {
        BLOCKSTATE_TO_OVERRIDE.put(state, override);
    }

    public static void registerBlockStateOverrideRule(OverrideRuleFile<BlockState> overrideRuleFile) {
        BLOCKSTATE_OVERRIDE_RULE_FILES.add(overrideRuleFile);
    }

    public static OverridePreset getOverrideForBlock(BlockState state) {
        if(BLOCKSTATE_TO_OVERRIDE.containsKey(state)) {
            return BLOCKSTATE_TO_OVERRIDE.get(state);
        }
        OverridePreset preset = buildPresetForState(state);
        BLOCKSTATE_TO_OVERRIDE.put(state, preset);
        return preset;
    }

    private static OverridePreset buildPresetForState(BlockState state) {
        List<OverridePreset.OverrideAndWeight> weights = new ArrayList<>();
        for (OverrideRuleFile<BlockState> overrideRule : BLOCKSTATE_OVERRIDE_RULE_FILES) {
            OverridePreset.OverrideAndWeight overrideAndWeight = overrideRule.getOverrideWeightForObject(state);
            if(overrideAndWeight.weight() == 0) continue;
            weights.add(overrideAndWeight);
        }

        if(weights.isEmpty()) {
            return OverridePreset.DEFAULT;
        }

        return OverridePreset.getOrCreate(weights);
    }
}
