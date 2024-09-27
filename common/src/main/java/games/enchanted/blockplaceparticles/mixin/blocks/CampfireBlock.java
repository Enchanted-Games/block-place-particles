package games.enchanted.blockplaceparticles.mixin.blocks;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.block.CampfireBlock.class)
public abstract class CampfireBlock {
    @Shadow @Final public static BooleanProperty LIT;
    @Shadow @Final private boolean spawnParticles;

    @Inject(
        at = @At("TAIL"),
        method = "animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"
    )
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random, CallbackInfo ci) {
        if(!level.isClientSide() && !this.spawnParticles) return;
        if (blockState.getValue(LIT)) {
            SpawnParticles.spawnAmbientCampfireSparks(level, blockPos, blockState);
        }
    }
}
