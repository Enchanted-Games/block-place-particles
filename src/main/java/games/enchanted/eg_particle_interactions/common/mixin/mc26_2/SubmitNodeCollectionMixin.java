//? if minecraft: >= 26.2 {
package games.enchanted.eg_particle_interactions.common.mixin.mc26_2;

import games.enchanted.eg_particle_interactions.common.duck.mc26_2.CustomSubmits;
import games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_2.CustomParticleGeometryFeatureRenderer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_2.CustomParticleGeometryRenderState;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements CustomSubmits {
    @Shadow
    @Final
    public SimpleFeatureRenderPhase solid;

    @Shadow
    @Final
    public SimpleFeatureRenderPhase afterTerrain;

    @Override
    public void eg_particle_interactions$submitCustomGeometryParticles(CustomParticleGeometryRenderState customParticleGeometryRenderState) {
        this.solid.submit(new CustomParticleGeometryFeatureRenderer.Submit(customParticleGeometryRenderState, false));
        this.afterTerrain.submit(new CustomParticleGeometryFeatureRenderer.Submit(customParticleGeometryRenderState, true));
    }
}
//? }