package games.enchanted.eg_particle_interactions.common.util.math.range;

import com.mojang.serialization.Codec;
import net.minecraft.util.Util;

import java.util.List;

public record IntRange(int min, int max) {
    public static final Codec<IntRange> CODEC = Codec.INT.listOf().comapFlatMap(
        input -> Util.fixedSize(input, 2).map(
            list -> {
                final int min = list.get(0);
                final int max = list.get(1);
                if(min > max) {
                    throw new IllegalArgumentException("Integer range min '" + min + "' cannot be larger than max '" + max + "'");
                }
                return new IntRange(list.get(0), list.get(1));
            }
        ),
        range -> List.of(range.min(), range.max())
    );

    public boolean inRange(int value) {
        return !this.outsideRange(value);
    }

    public boolean outsideRange(int value) {
        return value <= this.min() || value >= this.max();
    }

    public int clampWithin(int value) {
        return Math.clamp(value, this.min(), this.max());
    }

    /**
     * Returns a percentage in a 0-1 range of where the value lies between min and max
     */
    public float remapValueToPercentageAlongRange(int value) {
        int testValue = Math.clamp(value, this.min(), this.max());
        return (float) (testValue - this.min()) / (this.max() - this.min());
    }
}
