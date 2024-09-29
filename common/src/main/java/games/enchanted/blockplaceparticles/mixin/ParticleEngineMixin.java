package games.enchanted.blockplaceparticles.mixin;

import games.enchanted.blockplaceparticles.particle_spawning.BlockParticleOverride;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin implements PreparableReloadListener {
    @Shadow
    protected ClientLevel level;

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"),
        method = "destroy"
    )
    public void useParticleInteractionsDestroyParticleLogic(BlockPos brokenBlockPos, BlockState brokenBlockState, CallbackInfo ci) {
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(brokenBlockState, false);
        if (particleOverride == BlockParticleOverride.NONE) {
            return;
        }
        SpawnParticles.spawnBlockBreakParticle(this.level, brokenBlockState, brokenBlockPos, particleOverride);
    }

    @Redirect(
        method = "destroy",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/VoxelShape;forAllBoxes(Lnet/minecraft/world/phys/shapes/Shapes$DoubleLineConsumer;)V")
    )
    public void skipSpawningVanillaDestroyParticles(VoxelShape instance, Shapes.DoubleLineConsumer action) {
    }
}