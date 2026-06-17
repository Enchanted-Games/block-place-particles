//? if minecraft: >= 26.2 {
package games.enchanted.eg_particle_interactions.common.particle.render.geometry.mc26_2;

import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_2.CustomParticleGeometryRenderState;
import net.minecraft.client.particle.SingleQuadParticle;
import org.joml.Quaternionf;

public class CustomParticleGeometryQuadConsumer implements QuadConsumer {
    final CustomParticleGeometryRenderState state;
    final SingleQuadParticle.Layer layer;

    public CustomParticleGeometryQuadConsumer(CustomParticleGeometryRenderState state, SingleQuadParticle.Layer layer) {
        this.state = state;
        this.layer = layer;
    }

    @Override
    public void addVertex(Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        this.state.addVertex(
            this.layer,
            quaternion,
            x,
            y,
            z,
            xOffset,
            yOffset,
            scale,
            u,
            v,
            packedLight,
            rCol,
            gCol,
            bCol,
            alpha
        );
    }

    @Override
    public void startQuad() {
        this.state.startQuad(this.layer);
    }

    @Override
    public void finishQuad() {
        this.state.finishQuad(this.layer);
    }
}
//?}