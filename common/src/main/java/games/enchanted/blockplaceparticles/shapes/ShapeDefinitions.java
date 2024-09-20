package games.enchanted.blockplaceparticles.shapes;

import org.joml.Vector3d;

public abstract class ShapeDefinitions {
    /**
     * A 1x1 pixel cube
     */
    public static final VectorShape CUBE = new VectorShape(new Vector3d[]{
        new Vector3d( 0.5d,  0.5d, -0.5d), new Vector3d( 0.5d,  0.5d,  0.5d), new Vector3d(-0.5d,  0.5d,  0.5d), new Vector3d(-0.5d,  0.5d, -0.5d),
        new Vector3d(-0.5d, -0.5d, -0.5d), new Vector3d(-0.5d, -0.5d,  0.5d), new Vector3d( 0.5d, -0.5d,  0.5d), new Vector3d( 0.5d, -0.5d, -0.5d),
        new Vector3d(-0.5d, -0.5d,  0.5d), new Vector3d(-0.5d,  0.5d,  0.5d), new Vector3d( 0.5d,  0.5d,  0.5d), new Vector3d( 0.5d, -0.5d,  0.5d),
        new Vector3d( 0.5d, -0.5d, -0.5d), new Vector3d( 0.5d,  0.5d, -0.5d), new Vector3d(-0.5d,  0.5d, -0.5d), new Vector3d(-0.5d, -0.5d, -0.5d),
        new Vector3d(-0.5d, -0.5d, -0.5d), new Vector3d(-0.5d,  0.5d, -0.5d), new Vector3d(-0.5d,  0.5d,  0.5d), new Vector3d(-0.5d, -0.5d,  0.5d),
        new Vector3d( 0.5d, -0.5d,  0.5d), new Vector3d( 0.5d,  0.5d,  0.5d), new Vector3d( 0.5d,  0.5d, -0.5d), new Vector3d( 0.5d, -0.5d, -0.5d)
    });
}
