package games.enchanted.eg_particle_interactions.common.mixin.client.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.override_system.OverridePreset;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.particle.render.ModParticleRenderTypes;
import games.enchanted.eg_particle_interactions.common.particle.render.group.CustomGeometryParticleGroup;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticlesRenderState;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = ParticleEngine.class, priority = 3000)
public abstract class ParticleEngineMixin implements PreparableReloadListener {
    @Shadow protected ClientLevel level;
    @Shadow @Final private Map<ParticleRenderType, ParticleGroup<?>> particles;

    // override item and block particles if they have a particle override
    @WrapOperation(
        method = "createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;makeParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;")
    )
    private <T extends ParticleOptions> Particle eg_particle_interactions$overrideParticleTypeConditionally(ParticleEngine instance, T originalParticleOption, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Particle> original) {
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
        Identifier id = ParticleOverrides.getIdOrThrow(override);

        if(id == ParticleOverrides.VANILLA_OVERRIDE_ID) {
            return (original).call(instance, originalParticleOption, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        ParticleContext context = ParticleContext.block(level, blockState, BlockPos.containing(x, y, z));
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


    @Inject(
        at = @At("HEAD"),
        method = "createParticleGroup",
        cancellable = true
    )
    private void eg_particle_interactions$createCustomParticleGroup(ParticleRenderType particleRenderType, CallbackInfoReturnable<ParticleGroup<?>> cir) {
        if(particleRenderType == ModParticleRenderTypes.PARTICLE_INTERACTIONS) {
            cir.setReturnValue(new CustomGeometryParticleGroup((ParticleEngine) (Object) this));
        }
    }

    @Inject(
        at = @At("TAIL"),
        method = "extract"
    )
    private void eg_particle_interactions$extractCustomParticles(ParticlesRenderState state, Frustum frustum, Camera camera, float f, CallbackInfo ci) {
        ParticleGroup<?> group = this.particles.get(ModParticleRenderTypes.PARTICLE_INTERACTIONS);
        if (group != null && !group.isEmpty()) {
            state.add(group.extractRenderState(frustum, camera, f));
        }
    }
}