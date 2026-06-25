package games.enchanted.eg_particle_interactions.common.particle.appearance.uv;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.resource.texture.UVCoordinates;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.Nullable;

public class SimpleUV extends UVProvider {
    public static final SimpleUV UNIT = new SimpleUV(new UVCoordinates(0, 0, 1, 1));

    public static Codec<SimpleUV> CODEC = UVCoordinates.CODEC.xmap(
        SimpleUV::new,
        SimpleUV::getCoords
    );

    public static MapCodec<SimpleUV> MAP_CODEC = CODEC.fieldOf("coordinates");

    final UVCoordinates coords;

    SimpleUV(UVCoordinates coords) {
        this.coords = coords;
    }

    protected UVCoordinates getCoords() {
        return this.coords;
    }

    @Override
    public UVCoordinates getUv(@Nullable UVCoordinates oldUV, TextureAtlasSprite sprite, float particleScale) {
        return this.coords;
    }

    @Override
    public MapCodec<? extends UVProvider> codec() {
        return MAP_CODEC;
    }
}
