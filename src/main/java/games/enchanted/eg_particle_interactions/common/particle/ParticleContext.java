package games.enchanted.eg_particle_interactions.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

public record ParticleContext(ClientLevel level, @Nullable BlockContext blockContext, @Nullable FluidContext fluidContext, @Nullable ItemStack stack) {
    public static ParticleContext plain(ClientLevel level) {
        return new ParticleContext(level, null, null, null);
    }

    public static ParticleContext fluid(ClientLevel level, FluidState state, BlockPos pos) {
        return new ParticleContext(level, null, new FluidContext(state, pos), null);
    }

    public static ParticleContext block(ClientLevel level, BlockState state, BlockPos pos) {
        return new ParticleContext(level, new BlockContext(state, pos), null, null);
    }

    public record BlockContext(BlockState state, BlockPos pos) {
    }

    public record FluidContext(FluidState state, BlockPos pos) {
    }
}
