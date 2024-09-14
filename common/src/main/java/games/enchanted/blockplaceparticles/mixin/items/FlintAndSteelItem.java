package games.enchanted.blockplaceparticles.mixin.items;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.FlintAndSteelItem.class)
public abstract class FlintAndSteelItem {
    @Inject(
        method = "useOn",
        at = @At(value = "HEAD")
    )
    private void spawnParticlesOnUse(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = useOnContext.getLevel();
        if(level.isClientSide()) {
            BlockPos clickedPos = useOnContext.getClickedPos();
            BlockState clickedState = level.getBlockState(clickedPos);

            if(!CampfireBlock.canLight(clickedState) && !CandleBlock.canLight(clickedState) && !CandleCakeBlock.canLight(clickedState)) {
                // placed a fire
                BlockPos firePos = clickedPos.relative(useOnContext.getClickedFace());
                if(BaseFireBlock.canBePlacedAt(level, firePos, useOnContext.getHorizontalDirection())) {
                    ParticleInteractionsLogging.debugInfo("Fire placed by '" + this + "' at " + firePos.toShortString() + ". (interacted at " + clickedPos.toShortString() + ")");
                    SpawnParticles.spawnFlintAndSteelSparkParticle(level, firePos);
                }
            }else {
                // lit a block
                ParticleInteractionsLogging.debugInfo("Block lit by '" + this + "' at " + clickedPos.toShortString());
                SpawnParticles.spawnFlintAndSteelSparkParticle(level, clickedPos);
            }
        }
    }
}