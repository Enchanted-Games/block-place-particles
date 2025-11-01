package games.enchanted.eg_particle_interactions.common.particle.render.state;

//? if minecraft: > 1.21.8 {
import net.minecraft.client.particle.SingleQuadParticle;

public record StateAndLayer(CustomParticleGeometryRenderState state, SingleQuadParticle.Layer layer) {
}
//?}