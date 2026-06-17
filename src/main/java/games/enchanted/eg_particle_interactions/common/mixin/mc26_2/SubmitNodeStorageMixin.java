//? if minecraft: >= 26.2 {
package games.enchanted.eg_particle_interactions.common.mixin.mc26_2;

import games.enchanted.eg_particle_interactions.common.duck.mc26_2.CustomSubmits;
import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_2.CustomParticleGeometryRenderState;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SubmitNodeStorage.class)
public abstract class SubmitNodeStorageMixin implements CustomSubmits {
    @Shadow
    public abstract OrderedSubmitNodeCollector order(int par1);

    @Override
    public void eg_particle_interactions$submitCustomGeometryParticles(CustomParticleGeometryRenderState customParticleGeometryRenderState) {
        ((CustomSubmits) this.order(0)).eg_particle_interactions$submitCustomGeometryParticles(customParticleGeometryRenderState);
    }
}
//? }