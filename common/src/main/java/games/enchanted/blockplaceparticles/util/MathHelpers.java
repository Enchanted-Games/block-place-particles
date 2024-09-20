package games.enchanted.blockplaceparticles.util;

import com.mojang.math.Axis;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class MathHelpers {
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

    public static Vector3f axisAngleToEuler(Vector3f axis, double angle) {
        float heading;
        float attitude;
        float bank;
        double s=Math.sin(angle);
        double c=Math.cos(angle);
        double t=1-c;
        //  if axis is not already normalised then uncomment this
        // double magnitude = Math.sqrt(x*x + y*y + z*z);
        // if (magnitude==0) throw error;
        // x /= magnitude;
        // y /= magnitude;
        // z /= magnitude;
        if ((axis.x*axis.y*t + axis.z*s) > 0.998) { // north pole singularity detected
            heading = (float) (2*Math.atan2(axis.x*Math.sin(angle/2),Math.cos(angle/2)));
            attitude = (float) (Math.PI/2);
            bank = 0;
            return new Vector3f(heading, attitude, bank);
        }
        if ((axis.x*axis.y*t + axis.z*s) < -0.998) { // south pole singularity detected
            heading = (float) (-2*Math.atan2(axis.x*Math.sin(angle/2),Math.cos(angle/2)));
            attitude = (float) (-Math.PI/2);
            bank = 0;
            return new Vector3f(heading, attitude, bank);
        }
        heading = (float) Math.atan2(axis.y * s- axis.x * axis.z * t , 1 - (axis.y*axis.y+ axis.z*axis.z ) * t);
        attitude = (float) Math.asin(axis.x * axis.y * t + axis.z * s);
        bank = (float) Math.atan2(axis.x * s - axis.y * axis.z * t , 1 - (axis.x*axis.x + axis.z*axis.z) * t);
        return new Vector3f(heading, attitude, bank);
    }

    /**
     * Returns the angle from point a to point b
     *
     * @return the angle in degrees
     */
    public static double angleBetween2DPoints(Vector2f pointA, Vector2f pointB) {
        return Math.toDegrees(Math.atan2(pointA.x - pointB.x, pointA.y - pointB.y));
    }

    /**
     * Returns the position in 3D space between two points
     *
     * @return the angle in degrees
     */
    public static Vector3f getPosBetween3DPoints(Vector3f pointA, Vector3f pointB) {
        float diffX = pointA.x - pointB.x;
        float diffY = pointA.y - pointB.y;
        float diffZ = pointA.z - pointB.z;
        return new Vector3f(pointA).sub(diffX / 2, diffY / 2, diffZ / 2);
    }
}
