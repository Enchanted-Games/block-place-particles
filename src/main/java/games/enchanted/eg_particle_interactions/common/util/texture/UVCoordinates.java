package games.enchanted.eg_particle_interactions.common.util.texture;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;

import java.util.List;

public record UVCoordinates(float u0, float v0, float u1, float v1) {
    public static UVCoordinates UNIT = new UVCoordinates(0, 0, 1, 1);

    public static Codec<UVCoordinates> CODEC = Codec.FLOAT.listOf(4, 4).xmap(
        list -> {
            return new UVCoordinates(list.get(0), list.get(1), list.get(2), list.get(3));
        },
        UVCoordinates -> {
            return List.of(UVCoordinates.u0(), UVCoordinates.v0(), UVCoordinates.u1(), UVCoordinates.v1());
        }
    );

    public UVCoordinates remapInUV(float u0, float v0, float u1, float v1) {
        return new UVCoordinates(
            Mth.lerp(this.u0, u0, u1),
            Mth.lerp(this.v0, v0, v1),
            Mth.lerp(this.u1, u0, u1),
            Mth.lerp(this.v1, v0, v1)
        );
    }
}
