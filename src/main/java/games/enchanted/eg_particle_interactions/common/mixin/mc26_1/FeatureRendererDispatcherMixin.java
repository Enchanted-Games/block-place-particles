//? if minecraft: < 26.2 {
/*package games.enchanted.eg_particle_interactions.common.mixin.mc26_1;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_1.CustomParticleGeometryFeatureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRendererDispatcherMixin {
    @Unique
    private final CustomParticleGeometryFeatureRenderer eg_particle_interactions$customFeatureRenderer = new CustomParticleGeometryFeatureRenderer();

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/ParticleFeatureRenderer;renderSolid(Lnet/minecraft/client/renderer/SubmitNodeCollection;)V"),
        method = "renderSolidFeatures"
    )
    private void eg_particle_interactions$injectSolid(ParticleFeatureRenderer instance, SubmitNodeCollection nodeCollection, Operation<Void> original) {
        original.call(instance, nodeCollection);
        this.eg_particle_interactions$customFeatureRenderer.renderSolids(nodeCollection);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/ParticleFeatureRenderer;renderTranslucent(Lnet/minecraft/client/renderer/SubmitNodeCollection;)V"),
        method = "renderTranslucentParticles"
    )
    private void eg_particle_interactions$injectTranslucentParticles(ParticleFeatureRenderer instance, SubmitNodeCollection nodeCollection, Operation<Void> original) {
        original.call(instance, nodeCollection);
        this.eg_particle_interactions$customFeatureRenderer.renderTranslucents(nodeCollection);
    }

    @Inject(
        at = @At("TAIL"),
        method = "endFrame"
    )
    private void eg_particle_interactions$onEndFrame(CallbackInfo ci) {
        this.eg_particle_interactions$customFeatureRenderer.endFrame();
    }

    @Inject(
        at = @At("TAIL"),
        method = "close"
    )
    private void eg_particle_interactions$onClose(CallbackInfo ci) {
        this.eg_particle_interactions$customFeatureRenderer.close();
    }
}
*///? }