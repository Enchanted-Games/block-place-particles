package games.enchanted.eg_particle_interactions.common.particle.override.manager;

import games.enchanted.eg_particle_interactions.common.particle.override.OverridePreset;
import games.enchanted.eg_particle_interactions.common.particle.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class ParticleOverrideManager {
    private static final Map<Identifier, ParticleOverride> IDENTIFIER_TO_OVERRIDE = new HashMap<>();
    private static final Map<Identifier, OverridePreset> OVERRIDE_PRESETS = new HashMap<>();
    private static final Map<BlockState, OverridePreset> BLOCKSTATE_OVERRIDES = new HashMap<>();

    public static Identifier registerOverride(Identifier id, ParticleOverride override) {
        ParticleOverrideManager.IDENTIFIER_TO_OVERRIDE.put(id, override);
        return id;
    }

    public static ParticleOverride getOverrideFromId(Identifier id) {
        ParticleOverride override = ParticleOverrideManager.IDENTIFIER_TO_OVERRIDE.get(id);
        if(override == null) {
            throw new IllegalStateException("Tried to get unregistered particle override '" + id + "'");
        }
        return override;
    }

    static void clearOverrides() {
        ParticleOverrideManager.IDENTIFIER_TO_OVERRIDE.clear();
    }


    public static void addBlockOverride(BlockState state, OverridePreset override) {
        ParticleOverrideManager.BLOCKSTATE_OVERRIDES.put(state, override);
    }

    public static OverridePreset getOverrideForBlock(BlockState state) {
        OverridePreset preset = ParticleOverrideManager.BLOCKSTATE_OVERRIDES.get(state);
        if(preset == null) return OverridePreset.DEFAULT;
        return preset;
    }
}
