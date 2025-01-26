package games.enchanted.blockplaceparticles.mixin.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import games.enchanted.blockplaceparticles.particle_override.BlockParticleOverride;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(brokenBlockState, BlockParticleOverride.ORIGIN_BLOCK_BROKEN);
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
        // Override item particles if the item is a BlockItem
        if(
            originalParticleOption.getType() == ParticleTypes.ITEM
        ) {
            if(!(originalParticleOption instanceof ItemParticleOption)) {
                return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
            }

            Item originalParticleItem = ((ItemParticleOption) originalParticleOption).getItem().getItem();

            if(!(originalParticleItem instanceof BlockItem)) {
                return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
            }

            int overrideOrigin = BlockParticleOverride.ORIGIN_ITEM_PARTICLE_OVERRIDDEN;

            BlockState originalParticleBlockState = ((BlockItem) originalParticleItem).getBlock().defaultBlockState();
            BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(originalParticleBlockState, overrideOrigin);

            if(particleOverride == BlockParticleOverride.VANILLA || particleOverride == BlockParticleOverride.NONE) {
                return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
            }

            ParticleOptions newParticleOption = particleOverride.getParticleOptionForState(originalParticleBlockState, level, BlockPos.containing(x, y, z), overrideOrigin);
            return (original).call(
                instance,
                newParticleOption,
                x,
                y,
                z,
                xSpeed * (Math.random() * 0.75 + 0.6) * particleOverride.getParticleVelocityMultiplier(),
                (ySpeed + 0.6) * (Math.random() * 0.75 + 0.6) * particleOverride.getParticleVelocityMultiplier(),
                zSpeed * (Math.random() * 0.75 + 0.6) * particleOverride.getParticleVelocityMultiplier()
            );
        }

        // Override block particles
        if(
            originalParticleOption.getType() != ParticleTypes.BLOCK &&
            originalParticleOption.getType() != ParticleTypes.DUST_PILLAR &&
            originalParticleOption.getType() != ParticleTypes.BLOCK_CRUMBLE
        ) {
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        if(!(originalParticleOption instanceof BlockParticleOption)) {
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        int overrideOrigin = BlockParticleOverride.ORIGIN_BLOCK_PARTICLE_OVERRIDDEN;

        BlockState originalParticleBlockState = ((BlockParticleOption) originalParticleOption).getState();
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(originalParticleBlockState, overrideOrigin);

        if(particleOverride == BlockParticleOverride.VANILLA || particleOverride == BlockParticleOverride.NONE) {
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        ParticleOptions newParticleOption = particleOverride.getParticleOptionForState(originalParticleBlockState, level, BlockPos.containing(x, y, z), overrideOrigin);
        boolean isDustPillarParticle = originalParticleOption.getType() == ParticleTypes.DUST_PILLAR;
        double newYSpeed = (ySpeed * particleOverride.getParticleVelocityMultiplier() * 0.5) + (ySpeed < 0.02 ? 0.08 : 0.);
        return (original).call(
            instance,
            newParticleOption,
            x,
            y,
            z,
            xSpeed * (Math.random() * 0.75 + 0.6) * particleOverride.getParticleVelocityMultiplier(),
            isDustPillarParticle ? (ySpeed * 2) + 0.45 : newYSpeed,
            zSpeed * (Math.random() * 0.75 + 0.6) * particleOverride.getParticleVelocityMultiplier()
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

        int overrideOrigin = BlockParticleOverride.ORIGIN_BLOCK_CRACK;
        BlockParticleOverride override = BlockParticleOverride.getOverrideForBlockState(blockstate, overrideOrigin);

        if(override == BlockParticleOverride.VANILLA) return;

        if(override != BlockParticleOverride.NONE) {
            ParticleOptions newParticleOption = override.getParticleOptionForState(blockstate, level, blockPos, overrideOrigin);
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