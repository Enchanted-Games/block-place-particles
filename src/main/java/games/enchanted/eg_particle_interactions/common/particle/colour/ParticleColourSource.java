package games.enchanted.eg_particle_interactions.common.particle.colour;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public abstract class ParticleColourSource {
    public abstract int[] getARGB(ParticleColourContext context);

    public record ParticleColourContext(ClientLevel level, @Nullable BlockContext blockContext, @Nullable ItemStack stack) {
    }

    public record BlockContext(BlockState state, BlockPos pos) {
    }
}
