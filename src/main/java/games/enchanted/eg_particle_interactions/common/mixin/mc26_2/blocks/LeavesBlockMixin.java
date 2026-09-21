//? if minecraft: >= 26.3 {
package games.enchanted.eg_particle_interactions.common.mixin.mc26_2.blocks;

import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingParticlesLeavesBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
    @Inject(
        at = @At("HEAD"),
        method = "animateTick"
    )
    private void spawnCustomLeafParticlesIfNecessary(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if(!(level instanceof ClientLevel clientLevel)) return;
        if((LeavesBlock) (Object) this instanceof FallingParticlesLeavesBlock) return;

        eg_particle_interactions$makeFallingLeavesParticles(clientLevel, pos, random);
    }

    @Unique
    private void eg_particle_interactions$makeFallingLeavesParticles(ClientLevel level, BlockPos pos, RandomSource random) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);

        if (random.nextFloat() >= 0.01f) return;
        if (FallingParticlesLeavesBlock.isFaceFull(belowState.getCollisionShape(level, below), Direction.UP)) return;

        SpawnParticles.spawnFallingLeavesParticles(level, pos, random);
    }
}
//? }
