package games.enchanted.blockplaceparticles.mixin.items;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.item.BucketItem.class)
public abstract class BucketItem {
    @Shadow @Final private Fluid content;

    @Inject(
        method = "playEmptySound",
        at = @At(value = "HEAD")
    )
    private void spawnFluidParticlesOnBucketEmpty(Player player, LevelAccessor levelAccessor, BlockPos fluidPos, CallbackInfo ci) {
        if(levelAccessor.isClientSide()) {
            Fluid placedFluid = this.content;
            FluidState placedFluidState = content.defaultFluidState();

            if(!(levelAccessor.dimensionType().ultraWarm() && placedFluidState.is(FluidTags.WATER))) {
                ParticleInteractionsLogging.interactionDebugInfo("Bucket of " + placedFluid.builtInRegistryHolder().key().location() + " placed at " + fluidPos.toShortString());
                SpawnParticles.spawnFluidPlacedParticle(levelAccessor, fluidPos, placedFluid);
            }
        }
    }
}
