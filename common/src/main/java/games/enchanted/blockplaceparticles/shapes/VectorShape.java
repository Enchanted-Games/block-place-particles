package games.enchanted.blockplaceparticles.shapes;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class VectorShape {
    protected final Vector3d[] vertices;

    public VectorShape(Vector3d[] shapeVertices) {
        if(shapeVertices.length % 4 != 0) throw new IllegalArgumentException("VertexShape must be initialised with a multiple of 4 vertices");
        this.vertices = shapeVertices;
    }

    public Vector3d[] getVertices() {
        return this.vertices;
    }

    /**
     * Adds the geometry for this shape to a {@link VertexConsumer}
     *
     * @param vertexConsumer the vertex consumer
     * @param uvCoordinates  the uv coordinates for every face, this should have 2 elements defining the uv of the top left and bottom right corners
     * @param xPos           x position
     * @param yPos           y position
     * @param zPos           z position
     * @param size           the scale the shape will render at
     * @param rotation       the pitch, yaw, and roll that this shape will be rendered at in degrees
     * @param lightColour    light colour
     * @param colour         tint colour
     */
    public void renderShapeWithRotation(VertexConsumer vertexConsumer, Vector2f[] uvCoordinates, double xPos, double yPos, double zPos, float size, Vector3f rotation, int lightColour, Vector4f colour) {
        if(uvCoordinates.length > 2) throw new IllegalArgumentException("VertexShape#renderShape requires exactly 2 elements in uvCoordinates specifying the top left and top right uv coordinates");
        float pitchRad = (float) Math.toRadians(rotation.x);
        float yawRad = (float) Math.toRadians(rotation.y);
        float rollRad = (float) Math.toRadians(rotation.z);

        for (int i = 0; i < this.vertices.length; i += 4) {
            Vector3d vertex1 = MathHelpers.rotate3DPoint(this.vertices[i]    , pitchRad, yawRad, rollRad).mul(size).add(xPos, yPos, zPos);
            Vector3d vertex2 = MathHelpers.rotate3DPoint(this.vertices[i + 1], pitchRad, yawRad, rollRad).mul(size).add(xPos, yPos, zPos);
            Vector3d vertex3 = MathHelpers.rotate3DPoint(this.vertices[i + 2], pitchRad, yawRad, rollRad).mul(size).add(xPos, yPos, zPos);
            Vector3d vertex4 = MathHelpers.rotate3DPoint(this.vertices[i + 3], pitchRad, yawRad, rollRad).mul(size).add(xPos, yPos, zPos);

            this.addVertex(vertexConsumer, vertex1, uvCoordinates[0].x, uvCoordinates[1].y, lightColour, colour);
            this.addVertex(vertexConsumer, vertex2, uvCoordinates[0].x, uvCoordinates[0].y, lightColour, colour);
            this.addVertex(vertexConsumer, vertex3, uvCoordinates[1].x, uvCoordinates[0].y, lightColour, colour);
            this.addVertex(vertexConsumer, vertex4, uvCoordinates[1].x, uvCoordinates[1].y, lightColour, colour);
        }
    }
    /**
     * @see VectorShape#renderShapeWithRotation(VertexConsumer, Vector2f[], double, double, double, float, Vector3f, int, Vector4f)
     *
     */
    public void renderShape(VertexConsumer vertexConsumer, Vector2f[] uvCoordinates, double xPos, double yPos, double zPos, float size, int lightColour, Vector4f colour) {
        this.renderShapeWithRotation(vertexConsumer, uvCoordinates, xPos, yPos, zPos, size, new Vector3f(0), lightColour, colour);
    }

    protected void addVertex(VertexConsumer vertexConsumer, Vector3d vertexPos, float u, float v, int lightColor, Vector4f colour) {
        vertexConsumer.addVertex((float) vertexPos.x, (float) vertexPos.y, (float) vertexPos.z).setUv(u, v).setColor(colour.x, colour.y, colour.z, colour.w).setLight(lightColor);
    }

    /**
     * Returns an exact copy of a shape
     *
     * @param shapeToCopy the shape to copy
     */
    public static VectorShape copyShape(VectorShape shapeToCopy) {
        Vector3d[] copiedVerts = new Vector3d[shapeToCopy.getVertices().length];
        for (int i = 0; i < shapeToCopy.getVertices().length; i++) {
            copiedVerts[i] = new Vector3d(shapeToCopy.getVertices()[i]);
        }
        return new VectorShape(copiedVerts);
    }

    /**
     * Scales a shape in the given directions, this will change the vertex positions of the shape
     *
     * @param scale a vector the shape will be scaled by
     *
     * @return this shape
     */
    public VectorShape scale(Vector3f scale) {
        for (Vector3d vertex : this.vertices) {
            vertex.mul(scale);
        }
        return this;
    }
}
