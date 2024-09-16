package games.enchanted.blockplaceparticles.util;

import net.minecraft.world.phys.Vec3;

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
}
