package games.enchanted.eg_particle_interactions.common.duck;

//? if minecraft: >= 26.2 {
import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_2.CustomParticleGeometryRenderState;
//? } else {
/*import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_1.CustomParticleGeometryRenderState;
*///? }

public interface CustomSubmits {
    void eg_particle_interactions$submitCustomGeometryParticles(CustomParticleGeometryRenderState customParticleGeometryRenderState);
}
