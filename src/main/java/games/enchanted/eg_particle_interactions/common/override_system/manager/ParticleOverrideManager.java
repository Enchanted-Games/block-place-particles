package games.enchanted.eg_particle_interactions.common.override_system.manager;

import games.enchanted.eg_particle_interactions.common.override_system.OverridePreset;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class ParticleOverrideManager {
    private static final Map<BlockState, OverridePreset> BLOCKSTATE_OVERRIDES = new HashMap<>();

    public static void addBlockOverride(BlockState state, OverridePreset override) {
        ParticleOverrideManager.BLOCKSTATE_OVERRIDES.put(state, override);
    }

    public static OverridePreset getOverrideForBlock(BlockState state) {
        OverridePreset preset = ParticleOverrideManager.BLOCKSTATE_OVERRIDES.get(state);
        if(preset == null) return OverridePreset.DEFAULT;
        return preset;
    }
}
