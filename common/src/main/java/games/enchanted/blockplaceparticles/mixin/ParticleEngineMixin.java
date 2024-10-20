package games.enchanted.blockplaceparticles.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.blockplaceparticles.particle_spawning.override.BlockParticleOverride;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin implements PreparableReloadListener {
    @Shadow
    protected ClientLevel level;

    // block breaking logic
    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"),
        method = "destroy"
    )
    public void useParticleInteractionsDestroyParticleLogic(BlockPos brokenBlockPos, BlockState brokenBlockState, CallbackInfo ci) {
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(brokenBlockState);
        SpawnParticles.spawnBlockBreakParticle(this.level, brokenBlockState, brokenBlockPos, particleOverride);
    }

    @Redirect(
        method = "destroy",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/VoxelShape;forAllBoxes(Lnet/minecraft/world/phys/shapes/Shapes$DoubleLineConsumer;)V")
    )
    public void skipSpawningVanillaDestroyParticles(VoxelShape instance, Shapes.DoubleLineConsumer action) {
    }

    // override BLOCK and DUST_PILLAR particles if they have a particle override
    @WrapOperation(
        method = "createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;makeParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;")
    )
    private <T extends ParticleOptions> Particle overrideParticleTypeConditionally(ParticleEngine instance, T originalParticleOption, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Particle> original) {
        if(originalParticleOption.getType() != ParticleTypes.BLOCK && originalParticleOption.getType() != ParticleTypes.DUST_PILLAR) {
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
        }
        if(!(originalParticleOption instanceof BlockParticleOption)) {
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        BlockState originalParticleBlockState = ((BlockParticleOption) originalParticleOption).getState();
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(originalParticleBlockState);

        if(particleOverride == BlockParticleOverride.BLOCK || particleOverride == BlockParticleOverride.NONE) {
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        ParticleOptions newParticleOption = particleOverride.getParticleOptionForState(originalParticleBlockState, level, BlockPos.containing(x, y, z));
        boolean isDustPillarParticle = originalParticleOption.getType() == ParticleTypes.DUST_PILLAR;
        double newYSpeed = (ySpeed * 0.08) + (ySpeed < 0.02 ? 0.08 : 0.);
        return (original).call(
            instance,
            newParticleOption,
            x,
            y,
            z,
            xSpeed * (Math.random() * 0.75 + 0.6) * 0.15,
            isDustPillarParticle ? (ySpeed * 2) + 0.45 : newYSpeed,
            zSpeed * (Math.random() * 0.75 + 0.6) * 0.15
        );
    }

    // block cracking particles
    @Inject(
        method = "crack(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"),
        locals = LocalCapture.CAPTURE_FAILSOFT,
        cancellable = true
    )
    public void replaceCrackingParticlesConditionally(BlockPos blockPos, Direction side, CallbackInfo ci, @Local(ordinal = 0) double xPos, @Local(ordinal = 1) double yPos, @Local(ordinal = 2) double zPos) {
        BlockState blockstate = this.level.getBlockState(blockPos);
        BlockParticleOverride override = BlockParticleOverride.getOverrideForBlockState(blockstate);
        if(override == BlockParticleOverride.BLOCK) return;
        if(override != BlockParticleOverride.NONE) {
            ParticleOptions newParticleOption = override.getParticleOptionForState(blockstate, level, blockPos);
            if (newParticleOption == null) return;
            this.level.addParticle(
                newParticleOption,
                xPos,
                yPos,
                zPos,
                0,
                0,
                0
            );
        }
        ci.cancel();
    }
}