package games.enchanted.eg_particle_interactions.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

public record ParticleContext(ClientLevel level, BlockPos pos, @Nullable BlockContext blockContext, @Nullable FluidContext fluidContext, @Nullable ItemContext stack) {
    public static ParticleContext plain(ClientLevel level, BlockPos pos) {
        return new ParticleContext(level, pos, null, null, null);
    }

    public static ParticleContext fluid(ClientLevel level, FluidState state, BlockPos pos) {
        return new ParticleContext(level, pos, null, new FluidContext(state), null);
    }

    public static ParticleContext block(ClientLevel level, BlockState state, BlockPos pos) {
        return new ParticleContext(level, pos, new BlockContext(state), null, null);
    }

    public record BlockContext(BlockState state) {
    }

    public record FluidContext(FluidState state) {
    }

    public record ItemContext(ItemStack stack) {
    }
}
