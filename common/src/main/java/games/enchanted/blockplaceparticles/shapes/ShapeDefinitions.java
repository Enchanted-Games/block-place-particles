package games.enchanted.blockplaceparticles.shapes;

import org.joml.Vector3d;

public abstract class ShapeDefinitions {
    /**
     * A 1x1 pixel cube with the origin in the center of the cube
     */
    public static final QuadFaceShape CUBE = new QuadFaceShape(new Vector3d[]{
        new Vector3d( 0.5d,  0.5d, -0.5d), new Vector3d( 0.5d,  0.5d,  0.5d), new Vector3d(-0.5d,  0.5d,  0.5d), new Vector3d(-0.5d,  0.5d, -0.5d),
        new Vector3d(-0.5d, -0.5d, -0.5d), new Vector3d(-0.5d, -0.5d,  0.5d), new Vector3d( 0.5d, -0.5d,  0.5d), new Vector3d( 0.5d, -0.5d, -0.5d),
        new Vector3d(-0.5d, -0.5d,  0.5d), new Vector3d(-0.5d,  0.5d,  0.5d), new Vector3d( 0.5d,  0.5d,  0.5d), new Vector3d( 0.5d, -0.5d,  0.5d),
        new Vector3d( 0.5d, -0.5d, -0.5d), new Vector3d( 0.5d,  0.5d, -0.5d), new Vector3d(-0.5d,  0.5d, -0.5d), new Vector3d(-0.5d, -0.5d, -0.5d),
        new Vector3d(-0.5d, -0.5d, -0.5d), new Vector3d(-0.5d,  0.5d, -0.5d), new Vector3d(-0.5d,  0.5d,  0.5d), new Vector3d(-0.5d, -0.5d,  0.5d),
        new Vector3d( 0.5d, -0.5d,  0.5d), new Vector3d( 0.5d,  0.5d,  0.5d), new Vector3d( 0.5d,  0.5d, -0.5d), new Vector3d( 0.5d, -0.5d, -0.5d)
    });

    /**
     * A 1x1 pixel cube with the origin in the center of the top face
     */
    public static final QuadFaceShape CUBE_TOP_ORIGIN = new QuadFaceShape(new Vector3d[]{
        new Vector3d( 0.5d, 0.0d, -0.5d), new Vector3d(0.5d,  0.0d,  0.5d), new Vector3d(-0.5d, 0.0d,  0.5d), new Vector3d(-0.5d, 0.0d, -0.5d),
        new Vector3d(-0.5d, 1.0d, -0.5d), new Vector3d(-0.5d, 1.0d,  0.5d), new Vector3d( 0.5d, 1.0d,  0.5d), new Vector3d( 0.5d, 1.0d, -0.5d),
        new Vector3d(-0.5d, 1.0d,  0.5d), new Vector3d(-0.5d, 0.0d,  0.5d), new Vector3d( 0.5d, 0.0d,  0.5d), new Vector3d( 0.5d, 1.0d,  0.5d),
        new Vector3d( 0.5d, 1.0d, -0.5d), new Vector3d( 0.5d, 0.0d, -0.5d), new Vector3d(-0.5d, 0.0d, -0.5d), new Vector3d(-0.5d, 1.0d, -0.5d),
        new Vector3d(-0.5d, 1.0d, -0.5d), new Vector3d(-0.5d, 0.0d, -0.5d), new Vector3d(-0.5d, 0.0d,  0.5d), new Vector3d(-0.5d, 1.0d,  0.5d),
        new Vector3d( 0.5d, 1.0d,  0.5d), new Vector3d( 0.5d, 0.0d,  0.5d), new Vector3d( 0.5d, 0.0d, -0.5d), new Vector3d( 0.5d, 1.0d, -0.5d)
    });
}
