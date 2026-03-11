package games.enchanted.eg_particle_interactions.common.mixin.client.items;

import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
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
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V")
    )
    private void eg_particle_interactions$spawnParticlesOnTill(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = useOnContext.getLevel();
        if(level instanceof ClientLevel clientLevel) {
            BlockPos tilledBlockPos = useOnContext.getClickedPos();
            BlockState blockState = clientLevel.getBlockState(tilledBlockPos);

            Logging.interactionDebugInfo("Hoe used (" + this + ") at " + tilledBlockPos.toShortString() + " on " + blockState.getBlock());
            SpawnParticles.spawnHoeTillParticle(clientLevel, tilledBlockPos, useOnContext);
        }
    }
}
