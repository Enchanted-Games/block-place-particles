package games.enchanted.eg_particle_interactions.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;

/**
 * A type that stores a resource location for a {@link net.minecraft.world.level.block.Block} or the resource location of a block tag
 */
public record BlockOrTagLocation(Identifier location, boolean isTag) {
    public static Codec<BlockOrTagLocation> CODEC = Codec.STRING.comapFlatMap(
        string -> {
            Identifier parsedLocation = Identifier.parse(string.replace("#", ""));
            return DataResult.success(new BlockOrTagLocation(parsedLocation, string.startsWith("#")));
        },
        BlockOrTagLocation::toString
    );

    public BlockOrTagLocation(Identifier location) {
        this(location, false);
    }

    @Override
    public String toString() {
        return (this.isTag() ? "#" : "") + location.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BlockOrTagLocation castedObj)) return false;
        return this.location.equals(castedObj.location);
    }
}
