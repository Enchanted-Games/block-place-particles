package games.enchanted.eg_particle_interactions.common.shapes;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import games.enchanted.eg_particle_interactions.common.util.render.RenderingUtil;
import org.joml.*;

//? if minecraft: > 1.21.8 {
import games.enchanted.eg_particle_interactions.common.util.render.StateAndLayer;
//?}

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
     * Extract geometry for this shape
     *
     * @param consumer               the vertex consumer or state
     * @param uvCoordinates          the uv coordinates for every face, this should have 2 elements defining the uv of the top left and bottom right corners
     * @param pos                    x, y, and z position
     * @param scale                  the scale that the shape will be rendered at
     * @param rotation               the pitch, yaw, and roll that this shape will be rendered at in degrees, rotation is relative to the origin of the shape
     * @param size                   the scale the shape will render at
     * @param lightColour            light colour
     * @param argb                   a colour to tint the shape with, in array of int in argb format
     */
    public void extractShape(
        //? if minecraft: <= 1.21.8 {
        /*VertexConsumer consumer,
         *///?} else {
        StateAndLayer consumer,
        //?}
        Vector2f[] uvCoordinates, Vector3f pos, Vector3f scale, Vector3f rotation, float size, int lightColour, int[] argb
    ) {
        if(uvCoordinates.length > 2) throw new IllegalArgumentException("VertexShape#renderShape requires exactly 2 elements in uvCoordinates specifying the top left and top right uv coordinates");
        float pitchRad = (float) Math.toRadians(rotation.x);
        float yawRad   = (float) Math.toRadians(rotation.y);
        float rollRad  = (float) Math.toRadians(rotation.z);

        float rCol = argb[1] / 255f;
        float gCol = argb[2] / 255f;
        float bCol = argb[3] / 255f;
        float alpha = argb[0] / 255f;

        for (int i = 0; i < this.vertices.length; i += 4) {
            //? if minecraft: > 1.21.8 {
            consumer.state().startQuad(consumer.layer());
            //?}

            Vector3d vertex1 = MathHelpers.rotate3DPoint(new Vector3d(this.vertices[i]).mul(scale), pitchRad, yawRad, rollRad).mul(size).add(pos.x, pos.y, pos.z);
//            Vector3d vertex1 = new Vector3d().mul(scale).add(pos.x, pos.y, pos.z);
            RenderingUtil.addVertex(
                consumer,
                new Quaternionf(),
                (float) vertex1.x(),
                (float) vertex1.y(),
                (float) vertex1.z(),
                0f,
                0f,
                1,
                uvCoordinates[0].x,
                uvCoordinates[1].y,
                lightColour,
                rCol,
                gCol,
                bCol,
                alpha
            );

            Vector3d vertex2 = MathHelpers.rotate3DPoint(new Vector3d(this.vertices[i + 1]).mul(scale), pitchRad, yawRad, rollRad).mul(size).add(pos.x, pos.y, pos.z);
            RenderingUtil.addVertex(
                consumer,
                new Quaternionf(),
                (float) vertex2.x(),
                (float) vertex2.y(),
                (float) vertex2.z(),
                0f,
                0f,
                1,
                uvCoordinates[0].x,
                uvCoordinates[0].y,
                lightColour,
                rCol,
                gCol,
                bCol,
                alpha
            );

            Vector3d vertex3 = MathHelpers.rotate3DPoint(new Vector3d(this.vertices[i + 2]).mul(scale), pitchRad, yawRad, rollRad).mul(size).add(pos.x, pos.y, pos.z);
            RenderingUtil.addVertex(
                consumer,
                new Quaternionf(),
                (float) vertex3.x(),
                (float) vertex3.y(),
                (float) vertex3.z(),
                0f,
                0f,
                1,
                uvCoordinates[1].x,
                uvCoordinates[0].y,
                lightColour,
                rCol,
                gCol,
                bCol,
                alpha
            );

            Vector3d vertex4 = MathHelpers.rotate3DPoint(new Vector3d(this.vertices[i + 3]).mul(scale), pitchRad, yawRad, rollRad).mul(size).add(pos.x, pos.y, pos.z);
            RenderingUtil.addVertex(
                consumer,
                new Quaternionf(),
                (float) vertex4.x(),
                (float) vertex4.y(),
                (float) vertex4.z(),
                0f,
                0f,
                1,
                uvCoordinates[1].x,
                uvCoordinates[1].y,
                lightColour,
                rCol,
                gCol,
                bCol,
                alpha
            );

            //? if minecraft: > 1.21.8 {
            consumer.state().finishQuad(consumer.layer());
            //?}
        }
    }

    public void extractShape(
        //? if minecraft: <= 1.21.8 {
        /*VertexConsumer consumer,
         *///?} else {
        StateAndLayer consumer,
        //?}
        Vector2f[] uvCoordinates, Vector3f pos, Vector3f scale, float size, int lightColour, int[] argb
    ) {
        this.extractShape(consumer, uvCoordinates, pos, scale, new Vector3f(0), size, lightColour, argb);
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
