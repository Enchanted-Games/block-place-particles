package games.enchanted.blockplaceparticles.shapes;

import org.joml.Vector3d;

public class ShapeDefinitions {
    public static final VectorShape CUBE = new VectorShape(new Vector3d[]{
        new Vector3d( 1.0d,  1.0d, -1.0d), new Vector3d( 1.0d,  1.0d,  1.0d), new Vector3d(-1.0d,  1.0d,  1.0d), new Vector3d(-1.0d,  1.0d, -1.0d),
        new Vector3d(-1.0d, -1.0d, -1.0d), new Vector3d(-1.0d, -1.0d,  1.0d), new Vector3d( 1.0d, -1.0d,  1.0d), new Vector3d( 1.0d, -1.0d, -1.0d),
        new Vector3d(-1.0d, -1.0d,  1.0d), new Vector3d(-1.0d,  1.0d,  1.0d), new Vector3d( 1.0d,  1.0d,  1.0d), new Vector3d( 1.0d, -1.0d,  1.0d),
        new Vector3d( 1.0d, -1.0d, -1.0d), new Vector3d( 1.0d,  1.0d, -1.0d), new Vector3d(-1.0d,  1.0d, -1.0d), new Vector3d(-1.0d, -1.0d, -1.0d),
        new Vector3d(-1.0d, -1.0d, -1.0d), new Vector3d(-1.0d,  1.0d, -1.0d), new Vector3d(-1.0d,  1.0d,  1.0d), new Vector3d(-1.0d, -1.0d,  1.0d),
        new Vector3d( 1.0d, -1.0d,  1.0d), new Vector3d( 1.0d,  1.0d,  1.0d), new Vector3d( 1.0d,  1.0d, -1.0d), new Vector3d( 1.0d, -1.0d, -1.0d)
    });
}
