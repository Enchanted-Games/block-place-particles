package games.enchanted.blockplaceparticles.shapes;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import games.enchanted.blockplaceparticles.util.RenderingUtil;
import org.joml.*;

import java.lang.Math;

public class QuadFaceShape {
    protected final Vector3dc[] vertices;

    public QuadFaceShape(Vector3dc[] shapeVertices) {
        if(shapeVertices.length % 4 != 0) throw new IllegalArgumentException("VertexShape must be initialised with a multiple of 4 vertices");
        this.vertices = shapeVertices;
    }

    public Vector3dc[] getVertices() {
        return this.vertices;
    }

    /**
     * Adds the geometry for this shape to a {@link VertexConsumer}
     *
     * @param vertexConsumer         the vertex consumer
     * @param uvCoordinates          the uv coordinates for every face, this should have 2 elements defining the uv of the top left and bottom right corners
     * @param pos                    x, y, and z position
     * @param scale                  the scale that the shape will be rendered at
     * @param rotation               the pitch, yaw, and roll that this shape will be rendered at in degrees, rotation is relative to the origin of the shape
     * @param size                   the scale the shape will render at
     * @param lightColour            light colour
     * @param colour                 tint colour
     */
    public void renderShapeWithRotation(VertexConsumer vertexConsumer, Vector2f[] uvCoordinates, Vector3f pos, Vector3f scale, Vector3f rotation, float size, int lightColour, Vector4f colour) {
        if(uvCoordinates.length > 2) throw new IllegalArgumentException("VertexShape#renderShape requires exactly 2 elements in uvCoordinates specifying the top left and top right uv coordinates");
        float pitchRad = (float) Math.toRadians(rotation.x);
        float yawRad   = (float) Math.toRadians(rotation.y);
        float rollRad  = (float) Math.toRadians(rotation.z);

        for (int i = 0; i < this.vertices.length; i += 4) {
            Vector3d vertex1 = MathHelpers.rotate3DPoint( new Vector3d(this.vertices[i]    ).mul(scale), pitchRad, yawRad, rollRad ).mul(size).add(pos.x, pos.y, pos.z);
            Vector3d vertex2 = MathHelpers.rotate3DPoint( new Vector3d(this.vertices[i + 1]).mul(scale), pitchRad, yawRad, rollRad ).mul(size).add(pos.x, pos.y, pos.z);
            Vector3d vertex3 = MathHelpers.rotate3DPoint( new Vector3d(this.vertices[i + 2]).mul(scale), pitchRad, yawRad, rollRad ).mul(size).add(pos.x, pos.y, pos.z);
            Vector3d vertex4 = MathHelpers.rotate3DPoint( new Vector3d(this.vertices[i + 3]).mul(scale), pitchRad, yawRad, rollRad ).mul(size).add(pos.x, pos.y, pos.z);

            this.renderVertex(vertexConsumer, vertex1, uvCoordinates[0].x, uvCoordinates[1].y, lightColour, colour);
            this.renderVertex(vertexConsumer, vertex2, uvCoordinates[0].x, uvCoordinates[0].y, lightColour, colour);
            this.renderVertex(vertexConsumer, vertex3, uvCoordinates[1].x, uvCoordinates[0].y, lightColour, colour);
            this.renderVertex(vertexConsumer, vertex4, uvCoordinates[1].x, uvCoordinates[1].y, lightColour, colour);
        }
    }
    /**
     * @see QuadFaceShape#renderShapeWithRotation(VertexConsumer, Vector2f[], Vector3f, Vector3f, Vector3f, float, int, Vector4f)
     *
     */
    public void renderShape(VertexConsumer vertexConsumer, Vector2f[] uvCoordinates, Vector3f pos, Vector3f scale, float size, int lightColour, Vector4f colour) {
        this.renderShapeWithRotation(vertexConsumer, uvCoordinates, pos, scale, new Vector3f(0), size, lightColour, colour);
    }

    protected void renderVertex(VertexConsumer vertexConsumer, Vector3d vertexPos, float u, float v, int lightColor, Vector4f colour) {
        RenderingUtil.addVertexToConsumer(vertexConsumer, (float) vertexPos.x, (float) vertexPos.y, (float) vertexPos.z, 0, 0, 1, u, v, lightColor, colour.x, colour.y, colour.z, colour.w);
    }

    /**
     * Returns an exact copy of a shape
     *
     * @param shapeToCopy the shape to copy
     */
    public static QuadFaceShape copyShape(QuadFaceShape shapeToCopy) {
        Vector3dc[] copiedVerts = new Vector3d[shapeToCopy.getVertices().length];
        for (int i = 0; i < shapeToCopy.getVertices().length; i++) {
            copiedVerts[i] = new Vector3d(shapeToCopy.getVertices()[i]);
        }
        return new QuadFaceShape(copiedVerts);
    }
}
