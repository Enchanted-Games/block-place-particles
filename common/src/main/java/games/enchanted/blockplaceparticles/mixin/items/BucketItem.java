package games.enchanted.blockplaceparticles.mixin.items;

import games.enchanted.blockplaceparticles.Logging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.BucketItem.class)
public abstract class BucketItem {
    @Inject(
        method = "playEmptySound",
        at = @At(value = "HEAD")
    )
    private void spawnFluidParticlesOnBucketEmpty(Player player, LevelAccessor levelAccessor, BlockPos fluidPos, CallbackInfo ci) {
        if(levelAccessor.isClientSide()) {
            FluidState placedFluidState = levelAccessor.getBlockState(fluidPos).getFluidState();
            Fluid placedFluid = placedFluidState.getType();

            if(!(levelAccessor.dimensionType().ultraWarm() && placedFluidState.is(FluidTags.WATER))) {
                Logging.debugInfo("Bucket of (probably) " + placedFluid.builtInRegistryHolder().key() + " placed at " + fluidPos.toShortString());
                SpawnParticles.spawnFluidPlacedParticle(levelAccessor, fluidPos, placedFluid);
            }
        }
    }
}
