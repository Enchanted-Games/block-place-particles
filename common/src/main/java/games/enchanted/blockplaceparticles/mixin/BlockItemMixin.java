package games.enchanted.blockplaceparticles.mixin;

import games.enchanted.blockplaceparticles.Logging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Shadow public abstract Block getBlock();

    @Inject(
        at = @At("TAIL"),
        method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;"
    )
    private void spawnParticlesOnBlockPlace(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = blockPlaceContext.getLevel();
        if(level.isClientSide() && cir.getReturnValue() == InteractionResult.SUCCESS) {
            BlockPos blockPos = blockPlaceContext.getClickedPos();
            SpawnParticles.spawnBlockPlaceParticle((ClientLevel) level, blockPos);
            Logging.debugInfo(this.getBlock() + " placed at " + blockPos.toShortString() + " from '" + this + "'");
        }
    }
}