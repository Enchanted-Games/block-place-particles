package games.enchanted.blockplaceparticles.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderingUtil {
    public static void addVertexToConsumer(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        Vector3f vertexPos = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(scale).add(x, y, z);
        buffer.addVertex(vertexPos.x(), vertexPos.y(), vertexPos.z()).setUv(u, v).setColor(rCol, gCol, bCol, alpha).setLight(packedLight);
    }

    public static void addVertexToConsumer(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight) {
        addVertexToConsumer(buffer, quaternion, x, y, z, xOffset, yOffset, scale, u, v, packedLight, 1, 1, 1, 1);
    }

    public static void addVertexToConsumer(VertexConsumer buffer, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        Vector3f vertexPos = (new Vector3f(xOffset, yOffset, 0.0F)).mul(scale).add(x, y, z);
        buffer.addVertex(vertexPos.x(), vertexPos.y(), vertexPos.z()).setUv(u, v).setColor(rCol, gCol, bCol, alpha).setLight(packedLight);
    }
}
