//? if minecraft: >= 26.2 {
package games.enchanted.eg_particle_interactions.common.mixin.mc26_2;

import games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_2.CustomParticleGeometryFeatureRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRendererMap;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRendererDispatcherMixin {
    @Shadow
    @Final
    private FeatureRendererMap featureRenderers;

    @Inject(
        at = @At("TAIL"),
        method = "<init>"
    )
    private void eg_particle_interactions$injectCustomFeatureRenderers(
        RenderBuffers renderBuffers,
        ModelManager modelManager,
        AtlasManager atlasManager,
        Font font,
        GameRenderState gameRenderState,
        CallbackInfo ci
    ) {
        this.featureRenderers.put(CustomParticleGeometryFeatureRenderer.TYPE, new CustomParticleGeometryFeatureRenderer());
    }
}
//? }