package games.enchanted.eg_particle_interactions.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public record ParticleContext(ClientLevel level, @Nullable BlockContext blockContext, @Nullable ItemStack stack) {
    public static ParticleContext plain(ClientLevel level) {
        return new ParticleContext(level, null, null);
    }

    public record BlockContext(BlockState state, BlockPos pos) {
    }
}
