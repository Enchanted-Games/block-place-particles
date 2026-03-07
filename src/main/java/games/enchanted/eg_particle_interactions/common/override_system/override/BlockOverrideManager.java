package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.override_system.override.rule.AbstractOverrideRuleLoader;
import games.enchanted.eg_particle_interactions.common.override_system.override.rule.OverrideRuleFile;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.block.BlockPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.block.list.BlockListManager;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;

public class BlockOverrideManager extends AbstractOverrideRuleLoader<BlockState, BlockPredicate> {
    public static final BlockOverrideManager INSTANCE = new BlockOverrideManager();

    protected BlockOverrideManager() {
        super(FileToIdConverter.json(Constants.MOD_ID + "/block_override_rules"));
    }

    @Override
    protected Preparation<BlockState, BlockPredicate> prepare(ResourceManager manager, ProfilerFiller profiler) {
        BlockListManager.INSTANCE.prepareAndApply(manager, profiler);
        return super.prepare(manager, profiler);
    }

    @Override
    protected Codec<OverrideRuleFile<BlockState, BlockPredicate>> fileCodec() {
        return OverrideRuleFile.BLOCKSTATE_CODEC;
    }

    public static OverridePreset getForBlock(BlockState state, ParticleOrigin origin) {
        return INSTANCE.getOverrideFor(state, origin);
    }
}
