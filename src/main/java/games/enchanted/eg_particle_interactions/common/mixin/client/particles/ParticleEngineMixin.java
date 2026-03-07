package games.enchanted.eg_particle_interactions.common.mixin.client.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.particle.render.ModParticleRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if minecraft: > 1.21.8 {
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.culling.Frustum;
import games.enchanted.eg_particle_interactions.common.particle.render.group.CustomGeometryParticleGroup;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.state.ParticlesRenderState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?} else {
/*import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
*///?}

import java.util.Map;

@Mixin(value = ParticleEngine.class, priority = 3000)
public abstract class ParticleEngineMixin implements PreparableReloadListener {
    @Shadow
    protected ClientLevel level;

    // block cracking and breaking particles (moved to ClientLevel in 1.21.9)
    //? if minecraft: <= 1.21.8 {
    /*@Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"),
        method = "destroy"
    )
    public void useParticleInteractionsDestroyParticleLogic(BlockPos brokenBlockPos, BlockState brokenBlockState, CallbackInfo ci) {
        BlockParticleOverride particleOverride = BlockParticleOverride.getOverrideForBlockState(brokenBlockState, ParticleOrigin.BLOCK_BROKEN);
        SpawnParticles.spawnBlockBreakParticle(this.level, brokenBlockState, brokenBlockPos, particleOverride);
    }

    @WrapOperation(
        method = "destroy",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/VoxelShape;forAllBoxes(Lnet/minecraft/world/phys/shapes/Shapes$DoubleLineConsumer;)V")
    )
    public void skipSpawningVanillaDestroyParticles(VoxelShape instance, Shapes.DoubleLineConsumer doublelist, Operation<Void> original) {
    }

    @Inject(
        method = "crack(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"),
        locals = LocalCapture.CAPTURE_FAILSOFT,
        cancellable = true
    )
    public void replaceCrackingParticlesConditionally(BlockPos blockPos, Direction side, CallbackInfo ci, @Local(ordinal = 0) double xPos, @Local(ordinal = 1) double yPos, @Local(ordinal = 2) double zPos) {
        BlockState blockstate = this.level.getBlockState(blockPos);

        int overrideOrigin = ParticleOrigin.BLOCK_CRACK;
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
    *///?}

    // override item and block particles if they have a particle override
    @WrapOperation(
        method = "createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;makeParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;")
    )
    private <T extends ParticleOptions> Particle overrideParticleTypeConditionally(ParticleEngine instance, T originalParticleOption, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Particle> original) {
        // Override item particles if the item is a BlockItem
        if(
            originalParticleOption.getType() == ParticleTypes.ITEM
        ) {
            // TODO: implement item override
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
//            if(!(originalParticleOption instanceof ItemParticleOption)) {
//            }
//
//            ItemStackTemplate originalParticleItem = ((ItemParticleOption) originalParticleOption).getItem();
//
//            ParticleOrigin origin = ParticleOrigin.ITEM_PARTICLE_OVERRIDDEN;
//            OverridePreset overridePreset = BlockOverrideManager.getForBlock(blockState);
//            ParticleOverride override = overridePreset.getRandom();
//            Identifier id = ParticleOverrides.getIdFromOverride(override);
//
//            if(id == ParticleOverrides.VANILLA_OVERRIDE_ID) {
//                return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
//            }
//
//             override.spawnParticle(
//                origin,
//                new ParticleContext(
//                    level,
//                    null,
//                    originalParticleItem
//                ),
//                x,
//                y,
//                z,
//                xSpeed * (Math.random() * 0.75 + 0.6) * particleOverride.getParticleVelocityMultiplier(),
//                (ySpeed + 0.6) * (Math.random() * 0.75 + 0.6) * particleOverride.getParticleVelocityMultiplier(),
//                zSpeed * (Math.random() * 0.75 + 0.6) * particleOverride.getParticleVelocityMultiplier()
//            );
//
//            return null;
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

        BlockState blockState = ((BlockParticleOption) originalParticleOption).getState();

        ParticleOrigin origin = ParticleOrigin.BLOCK_PARTICLE_OVERRIDDEN;
        OverridePreset overridePreset = BlockOverrideManager.getForBlock(blockState, origin);
        ParticleOverride override = overridePreset.getRandom();
        Identifier id = ParticleOverrides.getIdFromOverride(override);

        if(id == ParticleOverrides.VANILLA_OVERRIDE_ID) {
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        ParticleContext context = new ParticleContext(
            level,
            new ParticleContext.BlockContext(blockState, BlockPos.containing(x, y, z)),
            null
        );
        boolean isDustPillarParticle = originalParticleOption.getType() == ParticleTypes.DUST_PILLAR;
        double newYSpeed = (ySpeed * 0.5) + (ySpeed < 0.02 ? 0.08 : 0.);

        override.spawnParticle(
            origin,
            context,
            x,
            y,
            z,
            xSpeed * (Math.random() * 0.75 + 0.6),
            isDustPillarParticle ? (ySpeed * 2) + 0.45 : newYSpeed,
            zSpeed * (Math.random() * 0.75 + 0.6)
        );

        return null;
    }

    //? if minecraft: > 1.21.8 {
    @Shadow @Final private Map<ParticleRenderType, ParticleGroup<?>> particles = Maps.newIdentityHashMap();;

    @Inject(
        at = @At("HEAD"),
        method = "createParticleGroup",
        cancellable = true
    )
    private void block_place_particle$createCustomParticleGroup(ParticleRenderType particleRenderType, CallbackInfoReturnable<ParticleGroup<?>> cir) {
        if(particleRenderType == ModParticleRenderTypes.PARTICLE_INTERACTIONS) {
            cir.setReturnValue(new CustomGeometryParticleGroup((ParticleEngine) (Object) this));
        }
    }

    @Inject(
        at = @At("TAIL"),
        method = "extract"
    )
    private void block_place_particle$extractCustomParticles(ParticlesRenderState state, Frustum frustum, Camera camera, float f, CallbackInfo ci) {
        ParticleGroup<?> group = this.particles.get(ModParticleRenderTypes.PARTICLE_INTERACTIONS);
        if (group != null && !group.isEmpty()) {
            state.add(group.extractRenderState(frustum, camera, f));
        }
    }
    //?}

    //? if minecraft: <= 1.21.8 && fabric {
    /*@Shadow @Final private Map<ParticleRenderType, Queue<Particle>> particles;
    @Shadow private static void renderParticleType(Camera p_382847_, float p_383032_, MultiBufferSource.BufferSource p_383105_, ParticleRenderType p_383179_, Queue<Particle> p_383046_) {}

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V"),
        method = "render(Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"
    )
    public void render(Camera camera, float f, MultiBufferSource.BufferSource bufferSource, CallbackInfo ci) {
        Queue<Particle> queue = this.particles.get(ModParticleRenderTypes.BACKFACE_TERRAIN_PARTICLE);
        if (queue != null && !queue.isEmpty()) {
            renderParticleType(camera, f, bufferSource, ModParticleRenderTypes.BACKFACE_TERRAIN_PARTICLE, queue);
        }
    }
    *///?}
}