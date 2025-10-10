package games.enchanted.eg_particle_interactions.common.rendering.particle.geometry;

import games.enchanted.eg_particle_interactions.common.rendering.particle.state.CustomParticleGeometryRenderState;
import net.minecraft.client.particle.SingleQuadParticle;
import org.joml.Quaternionf;

public class StateQuadConsumer implements QuadConsumer {
    final CustomParticleGeometryRenderState state;
    final SingleQuadParticle.Layer layer;

    public StateQuadConsumer(CustomParticleGeometryRenderState state, SingleQuadParticle.Layer layer) {
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
