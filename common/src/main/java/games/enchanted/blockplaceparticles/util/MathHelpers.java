package games.enchanted.blockplaceparticles.util;

import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class MathHelpers {
    public static int clampInt(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static double expandWhenOutOfBound(double value, double minBound, double maxBound) {
        if (value >= maxBound) {
            return value + 0.005;
        } else if (value <= minBound) {
            return value - 0.005;
        }
        return value;
    }

    /**
     * Returns the largest value from a {@link Vector3f}
     *
     * @param abs converts all values to positive before finding the maximum value
     */
    public static double maxVec3(Vector3f vector, boolean abs) {
        if(abs) vector = new Vector3f(Math.abs(vector.x), Math.abs(vector.y), Math.abs(vector.z));
        return Math.max(Math.max(vector.x, vector.y), vector.z);
    }

    /**
     * Rotates a vector in 3D space around the origin
     *
     * @param point       the point to rotate
     * @param roll  the roll in radians
     * @param pitch the pitch in radians
     * @param yaw   the yaw in radians
     * @return the rotated point
     */
    public static Vector3d rotate3DPoint(Vector3d point, float pitch, float yaw, float roll) {
        Quaternionf quaternionf = eulerAnglesToQuaternion(pitch, yaw, roll);
        return quaternionf.transform(new Vector3d(point));
    }

    /**
     * Converts euler angles to a quaternion
     *
     * @param roll  the roll in radians
     * @param pitch the pitch in radians
     * @param yaw   the yaw in radians
     */
    public static Quaternionf eulerAnglesToQuaternion(float roll, float pitch, float yaw) {
        float cr = (float) Math.cos(roll * 0.5);
        float sr = (float) Math.sin(roll * 0.5);
        float cp = (float) Math.cos(pitch * 0.5);
        float sp = (float) Math.sin(pitch * 0.5);
        float cy = (float) Math.cos(yaw * 0.5);
        float sy = (float) Math.sin(yaw * 0.5);

        Quaternionf q = new Quaternionf();
        q.w = cr * cp * cy + sr * sp * sy;
        q.x = sr * cp * cy - cr * sp * sy;
        q.y = cr * sp * cy + sr * cp * sy;
        q.z = cr * cp * sy - sr * sp * cy;

        return q;
    }

    /**
     * Returns the distance in a straight line between two vectors
     */
    public static float getDistanceBetweenVectors(Vector3f pointA, Vector3f pointB) {
        return (float) new Vector3d(pointA).distance(pointB.x, pointB.y, pointB.z);
    }

    /**
     * Returns the position in 3D space between two points
     */
    public static Vector3f getPosBetween3DPoints(Vector3f pointA, Vector3f pointB) {
        float diffX = pointA.x - pointB.x;
        float diffY = pointA.y - pointB.y;
        float diffZ = pointA.z - pointB.z;
        return new Vector3f(pointA).sub(diffX / 2, diffY / 2, diffZ / 2);
    }

    /**
     * Tests if the value is within the min and max bounds, if it is the value will be clamped to the closest value
     * If 10, 5, and 8 are passed, it will return 10
     * If 10, 5, and 15 are passed, it will return 10
     */
    public static double clampOutside(double value, double min, double max) {
        if (value > min && value < max) {
            double mid = (max - min) / 2 + min;
            return value < mid ? min : max;
        }
        return value;
    }
}
