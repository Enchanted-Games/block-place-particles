package games.enchanted.eg_particle_interactions.common.serialization;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

import java.util.List;

public class ModCodecs {
    public static <T> Codec<List<T>> singleOrListCodec(Codec<T> codec) {
        return Codec.either(codec.listOf(), codec).xmap(
            either -> either.map(list -> list, List::of),
            list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list)
        );
    }
}
