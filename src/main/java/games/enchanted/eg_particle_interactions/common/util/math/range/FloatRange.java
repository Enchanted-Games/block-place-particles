package games.enchanted.eg_particle_interactions.common.util.math.range;

import com.mojang.serialization.Codec;
import net.minecraft.util.Util;

import java.util.List;

public record FloatRange(float min, float max) {
    public static final Codec<FloatRange> CODEC = Codec.FLOAT.listOf().comapFlatMap(
        input -> Util.fixedSize(input, 2).map(
            list -> {
                final float min = list.get(0);
                final float max = list.get(1);
                if(min > max) {
                    throw new IllegalArgumentException("Float range min '" + min + "' cannot be larger than max '" + max + "'");
                }
                return new FloatRange(list.get(0), list.get(1));
            }
        ),
        range -> List.of(range.min(), range.max())
    );

    public boolean inRange(float value) {
        return !this.outsideRange(value);
    }

    public boolean outsideRange(float value) {
        return value <= this.min() || value >= this.max();
    }

    public float clampWithin(float value) {
        return Math.clamp(value, this.min(), this.max());
    }

    /**
     * Returns a percentage in a 0-1 range of where the value lies between min and max
     */
    public float remapValueToPercentageAlongRange(float value) {
        float testValue = Math.clamp(value, this.min(), this.max());
        return (testValue - this.min()) / (this.max() - this.min());
    }
}
