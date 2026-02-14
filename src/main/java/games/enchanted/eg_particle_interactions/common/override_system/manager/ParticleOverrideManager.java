package games.enchanted.eg_particle_interactions.common.override_system.manager;

import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.override_system.preset.unbaked.UnbakedPreset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class ParticleOverrideManager {
    private static final List<UnbakedPreset<BlockState>> UNBAKED_BLOCK_PRESETS = new ArrayList<>();
    private static final Map<BlockState, OverridePreset> BLOCKSTATE_OVERRIDES = new HashMap<>();

    public static void registerOverrideWeights(BlockState state, OverridePreset override) {
        BLOCKSTATE_OVERRIDES.put(state, override);
    }

    public static void registerUnbakedBlockPreset(UnbakedPreset<BlockState> preset) {
        UNBAKED_BLOCK_PRESETS.add(preset);
    }

    public static void addBlockOverride(BlockState state, OverridePreset override) {
        BLOCKSTATE_OVERRIDES.put(state, override);
    }

    public static OverridePreset getOverrideForBlock(BlockState state) {
        if(BLOCKSTATE_OVERRIDES.containsKey(state)) {
            return BLOCKSTATE_OVERRIDES.get(state);
        }
        OverridePreset preset = buildPresetForState(state);
        BLOCKSTATE_OVERRIDES.put(state, preset);
        return preset;
    }

    private static OverridePreset buildPresetForState(BlockState state) {
        List<Map<Identifier, Integer>> overrideWeightMaps = new ArrayList<>();
        // get all override weights
        for (UnbakedPreset<BlockState> preset : UNBAKED_BLOCK_PRESETS) {
            overrideWeightMaps.add(preset.getOverridesForObject(state));
        }

        // collapse weights
        Map<Identifier, Integer> overrideWeights = new HashMap<>();
        for (Map<Identifier, Integer> map : overrideWeightMaps) {
            for (Map.Entry<Identifier, Integer> idToWeight : map.entrySet()) {
                Identifier overrideId = idToWeight.getKey();
                int weight = idToWeight.getValue();

                if(overrideWeights.containsKey(overrideId)) {
                    int oldWeight = overrideWeights.get(overrideId);
                    overrideWeights.put(overrideId, oldWeight + weight);
                } else {
                    overrideWeights.put(overrideId, weight);
                }
            }
        }

        List<OverridePreset.OverrideAndWeight> finalWeights = overrideWeights.entrySet().stream().map(
            e -> new OverridePreset.OverrideAndWeight(e.getKey(), e.getValue())
        ).toList();

        if(finalWeights.isEmpty()) {
            return OverridePreset.DEFAULT;
        }

        return OverridePreset.getOrCreate(finalWeights);
    }
}
