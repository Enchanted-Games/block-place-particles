package games.enchanted.eg_particle_interactions.common.particle.render.state;

import games.enchanted.eg_particle_interactions.common.Logging;
import net.minecraft.util.ARGB;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Arrays;

public class QuadStorage {
    private static final int INITIAL_CAPACITY = 512;
    private static final int ADDITION_CAPACITY = 512;
    private static final int VERTS_PER_QUAD = 4;
    private static final int INTS_PER_QUAD = 2;
    private static final int FLOATS_PER_QUAD = 5;

    private int capacity = INITIAL_CAPACITY * VERTS_PER_QUAD;
    private float[] floats = new float[capacity * FLOATS_PER_QUAD];
    private int[] ints = new int[capacity * INTS_PER_QUAD];

    private int currentVertexIndex = 0;
    private int currentQuadVertCount = -1;

    public void startQuad() {
        if (this.currentQuadVertCount != -1) {
            throw new IllegalStateException("Cannot start new quad before previous quad has 4 vertices");
        }
        this.currentQuadVertCount = 0;
    }

    public void finishQuad() {
        if (this.currentQuadVertCount != 4) {
            throw new IllegalStateException("Cannot finish quad without 4 vertices");
        }
        this.currentQuadVertCount = -1;
    }

    public boolean validStateForAddingVertex() {
        return this.currentQuadVertCount >= 0 && this.currentQuadVertCount < 4;
    }

    public void addVertex(Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        if (!validStateForAddingVertex()) {
            throw new IllegalStateException("Cannot add vertex, make sure to finish the previous quad or start a new quad");
        }
        Vector3f vertexPos = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(scale).add(x, y, z);
        this.putData(vertexPos.x(), vertexPos.y(), vertexPos.z(), u, v, packedLight, ARGB.colorFromFloat(alpha, rCol, gCol, bCol));
    }

    private void putData(float x, float y, float z, float u, float v, int packedLight, int argb) {
        if (this.currentVertexIndex + 1 > this.capacity) {
            expandCapacity();
        }
        this.currentQuadVertCount++;
        int i = this.currentVertexIndex * FLOATS_PER_QUAD;
        this.floats[i++] = x;
        this.floats[i++] = y;
        this.floats[i++] = z;
        this.floats[i++] = u;
        this.floats[i] = v;
        i = this.currentVertexIndex * INTS_PER_QUAD;
        this.ints[i++] = packedLight;
        this.ints[i] = argb;
        this.currentVertexIndex++;
    }

    public void forEachVertex(VertexConsumer consumer) {
        for (int i = 0; i < this.currentVertexIndex; i++) {
            int floatsIndex = i * FLOATS_PER_QUAD;
            int intsIndex = i * INTS_PER_QUAD;
            consumer.consume(
                this.floats[floatsIndex++],
                this.floats[floatsIndex++],
                this.floats[floatsIndex++],
                this.floats[floatsIndex++],
                this.floats[floatsIndex],
                this.ints[intsIndex++],
                this.ints[intsIndex]
            );
        }
    }

    public int vertexAmount() {
        return this.currentVertexIndex;
    }

    public void clear() {
        this.currentQuadVertCount = -1;
        this.currentVertexIndex = 0;
    }

    private void expandCapacity() {
        int oldCapacity = this.capacity;
        this.capacity += ADDITION_CAPACITY * VERTS_PER_QUAD;
        this.floats = Arrays.copyOf(this.floats, capacity * FLOATS_PER_QUAD);
        this.ints = Arrays.copyOf(this.ints, capacity * INTS_PER_QUAD);
        Logging.info("Expanding QuadStorage: old capacity [{} quads], new capacity: [{} quads]", oldCapacity / VERTS_PER_QUAD, this.capacity / VERTS_PER_QUAD);
    }

    public interface VertexConsumer {
        void consume(float x, float y, float z, float u, float v, int packedLight, int argb);
    }
}
