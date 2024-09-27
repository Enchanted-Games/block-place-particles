package games.enchanted.blockplaceparticles.mixin.items;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.HoeItem.class)
public abstract class HoeItem {
    @Inject(
        method = "useOn",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V")
    )
    private void spawnParticlesOnTill(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = useOnContext.getLevel();
        if(level.isClientSide()) {
            BlockPos tilledBlockPos = useOnContext.getClickedPos();
            BlockState blockState = level.getBlockState(tilledBlockPos);

            ParticleInteractionsLogging.debugInfo("Hoe used (" + this + ") at " + tilledBlockPos.toShortString() + " on " + blockState.getBlock());
            SpawnParticles.spawnHoeTillParticle(level, tilledBlockPos);
        }
    }
}
