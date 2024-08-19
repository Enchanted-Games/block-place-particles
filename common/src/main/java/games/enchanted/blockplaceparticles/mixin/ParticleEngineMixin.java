package games.enchanted.blockplaceparticles.mixin;

import games.enchanted.blockplaceparticles.particle_spawning.BlockParticleOverride;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin implements PreparableReloadListener {
    @Shadow protected ClientLevel level;

    @Inject(
        at = @At("HEAD"),
        cancellable = true,
        method = "destroy(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
    )
    public void useParticleInteractionsDestroyParticleLogic(BlockPos brokenBlockPos, BlockState brokenBlockState, CallbackInfo ci) {
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(brokenBlockState, false);
        if(particleOverride == BlockParticleOverride.NONE) {
            ci.cancel();
        };
        SpawnParticles.spawnBlockBreakParticle(this.level, brokenBlockState, brokenBlockPos, particleOverride);
        ci.cancel();
    }
}
