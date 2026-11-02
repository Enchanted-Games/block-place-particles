package games.enchanted.eg_particle_interactions.optimization;

import org.joml.Quaternionf;
import org.joml.Vector3d;

/** Keeps the original copy and double-precision transform; the caller owns the rotation. */
public final class GeometryMath {
    private GeometryMath() {}

    public static Vector3d rotateWithQuaternion(Vector3d point, Quaternionf rotation) {
        return rotation.transform(new Vector3d(point));
    }
}
