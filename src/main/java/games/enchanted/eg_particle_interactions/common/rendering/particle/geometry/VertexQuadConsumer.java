package games.enchanted.eg_particle_interactions.common.rendering.particle.geometry;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VertexQuadConsumer implements QuadConsumer {
    final VertexConsumer consumer;

    public VertexQuadConsumer(VertexConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void addVertex(Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        Vector3f vertexPos = (new Vector3f(xOffset, yOffset, 0.0F)).mul(scale).add(x, y, z);
        consumer.addVertex(vertexPos.x(), vertexPos.y(), vertexPos.z()).setUv(u, v).setColor(rCol, gCol, bCol, alpha).setLight(packedLight);
    }

    @Override
    public void startQuad() {
    }

    @Override
    public void finishQuad() {
    }
}
