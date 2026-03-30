package games.enchanted.eg_particle_interactions.common.mixin.client.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FallingBlock.class, priority = 1010)
public class FallingBlockMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FallingBlock;isFree(Lnet/minecraft/world/level/block/state/BlockState;)Z"),
        method = "animateTick"
    )
    private boolean eg_particle_interactions$modifyIsFree(BlockState state, Operation<Boolean> original, BlockState stateArg, Level level, BlockPos pos) {
        return original.call(stateArg) || !FallingBlock.canSupportRigidBlock(level, pos.below());
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ParticleUtils;spawnParticleBelow(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/particles/ParticleOptions;)V"),
        method = "animateTick"
    )
    private void eg_particle_interactions$overrideParticles(Level level, BlockPos pos, RandomSource random, ParticleOptions particle, Operation<Void> original, BlockState state) {
        if(!(level instanceof ClientLevel clientLevel)) {
            original.call(level, pos, random, particle);
            return;
        }

        double x = (double) pos.getX() + random.nextDouble();
        double y = (double) pos.getY() - 0.05f;
        double z = (double) pos.getZ() + random.nextDouble();

        ParticleOrigin origin = ParticleOrigin.FALLING_BLOCK_UNSTABLE;
        OverridePreset override = BlockOverrideManager.getForBlock(state, origin);

        override.getRandom().spawnParticle(
            origin,
            ParticleContext.block(clientLevel, state, pos),
            x,
            y,
            z,
            0.0f,
            -0.1f,
            0.0f
        );
    }
}
