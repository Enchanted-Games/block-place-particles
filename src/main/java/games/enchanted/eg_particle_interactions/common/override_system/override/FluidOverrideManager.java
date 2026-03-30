package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.override_system.override.rule.AbstractOverrideRuleLoader;
import games.enchanted.eg_particle_interactions.common.override_system.override.rule.OverrideRuleFile;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.block.BlockPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.block.list.BlockListManager;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.fluid.FluidPredicate;
import games.enchanted.eg_particle_interactions.common.override_system.predicate.fluid.list.FluidListManager;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class FluidOverrideManager extends AbstractOverrideRuleLoader<FluidState, FluidPredicate> {
    public static final FluidOverrideManager INSTANCE = new FluidOverrideManager();

    protected FluidOverrideManager() {
        super(FileToIdConverter.json(Constants.MOD_ID + "/override_rules/fluids"));
    }

    @Override
    protected Preparation<FluidState, FluidPredicate> prepare(ResourceManager manager, ProfilerFiller profiler) {
        FluidListManager.INSTANCE.prepareAndApply(manager, profiler);
        return super.prepare(manager, profiler);
    }

    @Override
    protected Codec<OverrideRuleFile<FluidState, FluidPredicate>> fileCodec() {
        return OverrideRuleFile.FLUIDSTATE_CODEC;
    }

    public static OverridePreset getForFluid(FluidState state, ParticleOrigin origin) {
        return INSTANCE.getOverrideFor(state, origin);
    }
}
