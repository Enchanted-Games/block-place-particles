package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.override_system.override.rule.AbstractOverrideRuleLoader;
import games.enchanted.eg_particle_interactions.common.override_system.override.rule.OverrideRuleFile;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.particle.overrides.ParticleOrigin;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.world.level.block.state.BlockState;

public class BlockOverrideManager extends AbstractOverrideRuleLoader<BlockState> {
    public static final BlockOverrideManager INSTANCE = new BlockOverrideManager();

    protected BlockOverrideManager() {
        super(FileToIdConverter.json(Constants.MOD_ID + "/block_override_rules"));
    }

    @Override
    protected Codec<OverrideRuleFile<BlockState>> fileCodec() {
        return OverrideRuleFile.BLOCKSTATE_CODEC;
    }

    public static OverridePreset getForBlock(BlockState state, ParticleOrigin origin) {
        return INSTANCE.getOverrideFor(state, origin);
    }
}
