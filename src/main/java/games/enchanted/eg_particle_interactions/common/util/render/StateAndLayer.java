package games.enchanted.eg_particle_interactions.common.util.render;

//? if minecraft: > 1.21.8 {
import games.enchanted.eg_particle_interactions.common.rendering.particle.state.CustomParticleGeometryRenderState;
import net.minecraft.client.particle.SingleQuadParticle;

public record StateAndLayer(CustomParticleGeometryRenderState state, SingleQuadParticle.Layer layer) {
}
//?}