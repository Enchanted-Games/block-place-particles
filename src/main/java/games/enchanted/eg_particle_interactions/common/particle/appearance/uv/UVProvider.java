package games.enchanted.eg_particle_interactions.common.particle.appearance.uv;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.util.texture.UVCoordinates;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.Nullable;

public abstract class UVProvider {
    public abstract UVCoordinates getUv(@Nullable UVCoordinates oldUV, TextureAtlasSprite sprite);

    public abstract MapCodec<? extends UVProvider> codec();
}
