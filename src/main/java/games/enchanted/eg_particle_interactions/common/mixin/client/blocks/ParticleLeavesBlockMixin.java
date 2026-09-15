package games.enchanted.eg_particle_interactions.common.mixin.client.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if minecraft: <= 26.2 {
/*@Mixin(LeavesBlock.class)
public class ParticleLeavesBlockMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/LeavesBlock;spawnFallingLeavesParticle(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"),
        method = "makeFallingLeavesParticles"
    )
    private void eg_particle_interactions$wrapFallingLeaves(LeavesBlock instance, Level level, BlockPos pos, RandomSource random, Operation<Void> original) {
        if(!(level instanceof ClientLevel clientLevel)) {
            original.call(instance, level, pos, random);
            return;
        }
        boolean spawnOriginal = SpawnParticles.spawnFallingLeavesParticles(clientLevel, pos, random);
        if(spawnOriginal) {
            original.call(instance, level, pos, random);
        }
    }
}
*///? } else {
@Mixin(FallingParticlesLeavesBlock.class)
public class ParticleLeavesBlockMixin {
    // TODO: make spruce leaves have particles again
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FallingParticlesLeavesBlock;spawnFallingLeavesParticle(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"),
        method = "makeFallingLeavesParticles"
    )
    private void eg_particle_interactions$wrapFallingLeaves(FallingParticlesLeavesBlock instance, Level level, BlockPos pos, RandomSource random, Operation<Void> original) {
        if(!(level instanceof ClientLevel clientLevel)) {
            original.call(instance, level, pos, random);
            return;
        }
        boolean spawnOriginal = SpawnParticles.spawnFallingLeavesParticles(clientLevel, pos, random);
        if(spawnOriginal) {
            original.call(instance, level, pos, random);
        }
    }
}
//? }