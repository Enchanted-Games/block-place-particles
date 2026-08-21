package games.enchanted.eg_particle_interactions.common.particle.render.geometry;

import org.joml.Quaternionf;

public interface QuadConsumer {
    default void addVertexNoMask(Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        addVertex(quaternion, x, y, z, xOffset, yOffset, scale, u, v, 0, 0, packedLight, 1, 1, 1, 1);
    }
    void addVertex(Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, float maskU, float maskV, int packedLight, float rCol, float gCol, float bCol, float alpha);
    void startQuad();
    void finishQuad();
}
