package games.enchanted.eg_particle_interactions.common.mixin.client.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientLevel.class)
public class BlockBreakAndCrackingParticle_ClientLevelMixin {
    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"),
        method = "addDestroyBlockEffect"
    )
    public void eg_particle_interactions$useParticleInteractionsDestroyParticleLogic(BlockPos brokenBlockPos, BlockState brokenBlockState, CallbackInfo ci) {
        SpawnParticles.spawnBlockBreakParticle((ClientLevel) (Object) this, brokenBlockState, brokenBlockPos);
    }

    @WrapOperation(
        method = "addDestroyBlockEffect",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/VoxelShape;forAllBoxes(Lnet/minecraft/world/phys/shapes/Shapes$DoubleLineConsumer;)V")
    )
    public void eg_particle_interactions$skipSpawningVanillaDestroyParticles(VoxelShape instance, Shapes.DoubleLineConsumer action, Operation<Void> original) {
    }


    // block cracking particles
    @Inject(
        method =
            //? if neoforge {
            /*"addBreakingBlockEffect(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/phys/HitResult;)V"
            *///?} else {
            "addBreakingBlockEffect"
            //?}
        ,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"),
        locals = LocalCapture.CAPTURE_FAILSOFT,
        cancellable = true
    )
    public void eg_particle_interactions$replaceCrackingParticlesConditionally(
        BlockPos blockPos,
        Direction side,
        //? if neoforge {
        /*HitResult hitResult,
        *///?}
        CallbackInfo ci,
        @Local(ordinal = 0) double xPos,
        @Local(ordinal = 1) double yPos,
        @Local(ordinal = 2) double zPos
    ) {
        ClientLevel level = (ClientLevel) (Object) this;
        BlockState blockstate = level.getBlockState(blockPos);

        ParticleOrigin origin = ParticleOrigin.BLOCK_CRACK;
        OverridePreset overridePreset = BlockOverrideManager.getForBlock(blockstate, origin);
        ParticleOverride override = overridePreset.getRandom();
        Identifier id = ParticleOverrides.getIdOrThrow(override);

        if(id == ParticleOverrides.VANILLA_OVERRIDE_ID) return;

        ci.cancel();

        override.spawnParticle(
            origin,
            new ParticleContext(
                level,
                new ParticleContext.BlockContext(blockstate, blockPos),
                null
            ),
            xPos,
            yPos,
            zPos,
            0,
            0,
            0
        );
    }
}
