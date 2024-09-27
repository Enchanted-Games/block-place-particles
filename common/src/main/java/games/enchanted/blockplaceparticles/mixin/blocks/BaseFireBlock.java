package games.enchanted.blockplaceparticles.mixin.blocks;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.block.BaseFireBlock.class)
public abstract class BaseFireBlock {
    @Inject(
        at = @At("HEAD"),
        method = "animateTick"
    )
    protected void spawnSparkParticleRandomlyOnTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if(!level.isClientSide()) return;
        blockState.getShape(level, blockPos).forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            SpawnParticles.spawnAmbientFireSparks(level, blockState, blockPos, x1, y1, z1, x2, y2, z2);
        });
    }
}
