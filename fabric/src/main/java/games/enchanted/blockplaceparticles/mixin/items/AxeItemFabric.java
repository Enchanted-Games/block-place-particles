package games.enchanted.blockplaceparticles.mixin.items;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(net.minecraft.world.item.AxeItem.class)
public abstract class AxeItemFabric {
    @Shadow
    protected abstract Optional<BlockState> getStripped(BlockState unstrippedBlockstate);

    @Inject(
        method = "useOn",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/AxeItem;evaluateNewBlockState(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;")
    )
    private void spawnParticleOnAxeStrip(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState unstrippedBlockstate = level.getBlockState(blockPos);
        Optional<BlockState> strippedBlockState = this.getStripped(unstrippedBlockstate);

        if (strippedBlockState.isPresent() && level.isClientSide()) {
            ParticleInteractionsLogging.interactionDebugInfo("Axe used (" + this + ") at " + blockPos.toShortString() + " to strip " + unstrippedBlockstate.getBlock());
            SpawnParticles.spawnAxeStripParticle(level, blockPos, unstrippedBlockstate, strippedBlockState.get(), context);
        }
    }
}
