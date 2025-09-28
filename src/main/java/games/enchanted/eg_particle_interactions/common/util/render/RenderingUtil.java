package games.enchanted.eg_particle_interactions.common.util.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderingUtil {
    public static void addVertexToConsumer(VertexConsumer consumer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        Vector3f vertexPos = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(scale).add(x, y, z);
        addVertexToConsumer(consumer, vertexPos.x(), vertexPos.y(), vertexPos.z(), 0, 0, 0, u, v, packedLight, rCol, gCol, bCol, alpha);
    }

    public static void addVertexToConsumer(VertexConsumer consumer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight) {
        addVertexToConsumer(consumer, quaternion, x, y, z, xOffset, yOffset, scale, u, v, packedLight, 1, 1, 1, 1);
    }

    public static void addVertexToConsumer(VertexConsumer consumer, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        Vector3f vertexPos = (new Vector3f(xOffset, yOffset, 0.0F)).mul(scale).add(x, y, z);
        consumer.addVertex(vertexPos.x(), vertexPos.y(), vertexPos.z()).setUv(u, v).setColor(rCol, gCol, bCol, alpha).setLight(packedLight);
    }
}
