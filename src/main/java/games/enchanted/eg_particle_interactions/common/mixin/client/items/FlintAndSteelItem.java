package games.enchanted.eg_particle_interactions.common.mixin.client.items;

import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.FlintAndSteelItem.class)
public abstract class FlintAndSteelItem {
    @Inject(
        method = "useOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            //? if fabric {
            ordinal = 1,
            //? } else {
            /*ordinal = 0,
            *///? }
            shift = At.Shift.AFTER
        )
    )
    private void eg_particle_interactions$spawnParticlesOnFirePlace(
        UseOnContext useOnContext,
        CallbackInfoReturnable<InteractionResult> cir,
        @Local(name = "relativePos") BlockPos relativePos
    ) {
        if(useOnContext.getLevel() instanceof ClientLevel level) {
            SpawnParticles.spawnFlintAndSteelSparkParticle(level, relativePos, false);
        }
    }

    @Inject(
        method = "useOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            //? if fabric {
            ordinal = 0,
            //? } else {
            /*ordinal = 1,
            *///? }
            shift = At.Shift.AFTER
        )
    )
    private void eg_particle_interactions$spawnParticlesOnLitSomething(
        UseOnContext useOnContext,
        CallbackInfoReturnable<InteractionResult> cir,
        @Local(name = "pos") BlockPos pos
    ) {
        if(useOnContext.getLevel() instanceof ClientLevel level) {
            SpawnParticles.spawnFlintAndSteelSparkParticle(level, pos, true);
        }
    }
}