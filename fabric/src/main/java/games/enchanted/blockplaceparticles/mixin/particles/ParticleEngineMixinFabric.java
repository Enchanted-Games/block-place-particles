package games.enchanted.blockplaceparticles.mixin.particles;

import games.enchanted.blockplaceparticles.particle.ModParticleRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

// this is necessary to make particles with modded render types render on fabric
@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixinFabric {
    @Shadow @Final private Map<ParticleRenderType, Queue<Particle>> particles;
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
}
