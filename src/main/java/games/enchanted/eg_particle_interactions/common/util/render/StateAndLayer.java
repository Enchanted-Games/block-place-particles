package games.enchanted.eg_particle_interactions.common.util.render;

//? if minecraft: > 1.21.8 {
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.QuadParticleRenderState;

public record StateAndLayer(QuadParticleRenderState state, SingleQuadParticle.Layer layer) {
}
//?}