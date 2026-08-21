//? if minecraft: < 26.2 {
/*package games.enchanted.eg_particle_interactions.common.mixin.mc26_1;

import games.enchanted.eg_particle_interactions.common.duck.CustomSubmits;
import games.enchanted.eg_particle_interactions.common.duck.mc26_1.CustomSubmitsAccess;
import games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_1.CustomParticleGroupRenderer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_1.CustomParticleGeometryRenderState;
import net.minecraft.client.renderer.SubmitNodeCollection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements CustomSubmits, CustomSubmitsAccess {
    @Shadow
    private boolean wasUsed;

    @Unique
    private final List<CustomParticleGroupRenderer> eg_particle_interactions$particleGroupRenderers = new ArrayList<>();

    @Override
    public void eg_particle_interactions$submitCustomGeometryParticles(CustomParticleGeometryRenderState customParticleGeometryRenderState) {
        this.wasUsed = true;
        this.eg_particle_interactions$particleGroupRenderers.add(customParticleGeometryRenderState);
    }

    @Override
    public List<CustomParticleGroupRenderer> eg_particle_interactions$getCustomGeometryParticleSubmits() {
        return this.eg_particle_interactions$particleGroupRenderers;
    }

    @Inject(
        at = @At("TAIL"),
        method = "clear"
    )
    private void eg_particle_interactions$onClear(CallbackInfo ci) {
        this.eg_particle_interactions$particleGroupRenderers.clear();
    }
}
*///? }