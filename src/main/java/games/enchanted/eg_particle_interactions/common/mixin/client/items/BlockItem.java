package games.enchanted.eg_particle_interactions.common.mixin.client.items;

import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.BlockItem.class)
public abstract class BlockItem {
    @Inject(
        at = @At("RETURN"),
        method = "updateBlockStateFromTag"
    )
    private void spawnParticlesOnBlockPlace(BlockPos pos, Level level, ItemStack itemStack, BlockState placedState, CallbackInfoReturnable<BlockState> cir) {
        BlockState updatedState = cir.getReturnValue();
        if(level instanceof ClientLevel clientLevel) {
            SpawnParticles.spawnBlockPlaceParticle(clientLevel, pos, updatedState);
        }
    }
}