package games.enchanted.blockplaceparticles.mixin.accessor.client;

import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextureAtlas.class)
public interface TextureAtlasAccessor {
    @Accessor("width")
    int block_place_particle$getWidth();
    @Accessor("height")
    int block_place_particle$getHeight();
}
