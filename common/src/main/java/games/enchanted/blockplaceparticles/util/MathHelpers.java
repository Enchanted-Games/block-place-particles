package games.enchanted.blockplaceparticles.util;

public class MathHelpers {
    public static double expandWhenOutOfBound(double value, double minBound, double maxBound) {
        if (value >= maxBound) {
            return value + 0.005;
        } else if (value <= minBound) {
            return value - 0.005;
        }
        return value;
    }
}
