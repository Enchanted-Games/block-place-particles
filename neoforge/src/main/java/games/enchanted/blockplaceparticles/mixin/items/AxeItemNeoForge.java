package games.enchanted.blockplaceparticles.mixin.items;

import games.enchanted.blockplaceparticles.Logging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(net.minecraft.world.item.AxeItem.class)
public abstract class AxeItemNeoForge {
    @Shadow protected abstract Optional<BlockState> getStripped(BlockState unstrippedBlockstate);

    @Inject(
        method = "evaluateNewBlockState",
        at = @At(value = "HEAD"),
        remap = false
    )
    private void spawnParticleOnAxeStrip(Level level, BlockPos blockPos, @Nullable Player player, BlockState unstrippedBlockstate, UseOnContext useOnContext, CallbackInfoReturnable<Optional<BlockState>> cir) {
        Optional<BlockState> strippedBlockState = this.getStripped(unstrippedBlockstate);
        if (strippedBlockState.isPresent() && level.isClientSide() && player != null) {
            Logging.debugInfo("Axe used (" + this + ") at " + blockPos.toShortString() + " to strip " + unstrippedBlockstate.getBlock());
            SpawnParticles.spawnAxeStripParticle(level, blockPos, player);
        }
    }
}
