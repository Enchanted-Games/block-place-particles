package games.enchanted.blockplaceparticles.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class MathHelpers {
    public static double expandWhenOutOfBound(double value, double minBound, double maxBound) {
        if (value >= maxBound) {
            return value + 0.005;
        } else if (value <= minBound) {
            return value - 0.005;
        }
        return value;
    }

    public static double maxVec3(Vec3 vector, boolean abs) {
        if(abs) vector = new Vec3(Math.abs(vector.x), Math.abs(vector.y), Math.abs(vector.z));
        return Math.max(Math.max(vector.x, vector.y), vector.z);
    }

    /**
     * Rotates a vector in 3D space around the origin
     *
     * @param point       the point to rotate
     * @param xRotRadians x rotation in radians
     * @param yRotRadians y rotation in radians
     * @param zRotRadians z rotation in radians
     * @return the rotated point
     */
    public static Vector3d rotate3DPoint(Vector3d point, float xRotRadians, float yRotRadians, float zRotRadians) {
        Vector3d sin = new Vector3d(Mth.sin(xRotRadians), Mth.sin(yRotRadians), Mth.sin(zRotRadians));
        Vector3d cos = new Vector3d(Mth.cos(xRotRadians), Mth.cos(yRotRadians), Mth.cos(zRotRadians));

        point = new Vector3d(point.x, point.y * cos.x - point.z * sin.x, point.y * sin.x + point.z * cos.x);
        point = new Vector3d(point.x * cos.z - point.y * sin.z, point.x * sin.z + point.y * cos.z, point.z);
        point = new Vector3d(point.x * cos.y + point.z * sin.y, point.y, point.x * sin.y - point.z * cos.y);

        return point;
    }
}
