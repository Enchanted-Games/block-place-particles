package games.enchanted.blockplaceparticles.mixin.items;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.BlockItem.class)
public abstract class BlockItem {
    @Shadow public abstract Block getBlock();

    @Inject(
        at = @At("TAIL"),
        method = "placeBlock(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/world/level/block/state/BlockState;)Z"
    )
    private void spawnParticlesOnBlockPlace(BlockPlaceContext blockPlaceContext, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        Level level = blockPlaceContext.getLevel();
        if(level.isClientSide() && cir.getReturnValue()) {
            BlockPos blockPos = blockPlaceContext.getClickedPos();
            SpawnParticles.spawnBlockPlaceParticle((ClientLevel) level, blockPos);
            ParticleInteractionsLogging.debugInfo(this.getBlock() + " placed at " + blockPos.toShortString() + " from '" + this + "'");
        }
    }
}