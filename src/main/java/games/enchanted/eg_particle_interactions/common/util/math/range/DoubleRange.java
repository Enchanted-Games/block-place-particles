package games.enchanted.eg_particle_interactions.common.util.math.range;

import com.mojang.serialization.Codec;
import net.minecraft.util.Util;

import java.util.List;

public record DoubleRange(double min, double max) {
    public static final Codec<DoubleRange> CODEC = Codec.DOUBLE.listOf().comapFlatMap(
        input -> Util.fixedSize(input, 2).map(
            list -> {
                final double min = list.get(0);
                final double max = list.get(1);
                if(min > max) {
                    throw new IllegalArgumentException("Double range min '" + min + "' cannot be larger than max '" + max + "'");
                }
                return new DoubleRange(list.get(0), list.get(1));
            }
        ),
        range -> List.of(range.min(), range.max())
    );

    public boolean inRange(double value) {
        return !this.outsideRange(value);
    }

    public boolean outsideRange(double value) {
        return value <= this.min() || value >= this.max();
    }

    public double clampWithin(double value) {
        return Math.clamp(value, this.min(), this.max());
    }

    /**
     * Returns a percentage in a 0-1 range of where the value lies between min and max
     */
    public double remapValueToPercentageAlongRange(double value) {
        double testValue = Math.clamp(value, this.min(), this.max());
        return (testValue - this.min()) / (this.max() - this.min());
    }
}
