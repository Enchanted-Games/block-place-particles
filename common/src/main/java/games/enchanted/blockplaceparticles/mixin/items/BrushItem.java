package games.enchanted.blockplaceparticles.mixin.items;

import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import games.enchanted.blockplaceparticles.particle_spawning.override.BlockParticleOverride;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.item.BrushItem.class)
public abstract class BrushItem {
    @Inject(
        method = "spawnDustParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/BlockHitResult;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/HumanoidArm;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/BlockHitResult;getLocation()Lnet/minecraft/world/phys/Vec3;", shift = At.Shift.AFTER),
        cancellable = true
    )
    private void spawnDustParticles(Level level, BlockHitResult hitResult, BlockState blockState, Vec3 pos, HumanoidArm arm, CallbackInfo ci, @Local(ordinal = 0) net.minecraft.world.item.BrushItem.DustParticlesDelta particlesDelta, @Local(ordinal = 0) int armDirection, @Local(ordinal = 1) int amountOfParticles) {
        if(!level.isClientSide) return;

        Vec3 particlePos = hitResult.getLocation();
        ParticleInteractionsLogging.debugInfo("Blockstate brushed {} at {}", blockState, particlePos);

        BlockParticleOverride override = BlockParticleOverride.getOverrideForBlockState(blockState);
        if(override == BlockParticleOverride.BLOCK || override == BlockParticleOverride.NONE) {
            return;
        }

        Direction brushDirection = hitResult.getDirection();
        SpawnParticles.spawnBrushingParticles((ClientLevel) level, override, blockState, brushDirection, particlePos, armDirection, amountOfParticles, particlesDelta.xd(), particlesDelta.yd(), particlesDelta.zd());
        ci.cancel();
    }
}
