package games.enchanted.eg_particle_interactions.common.particle.appearance.uv;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import games.enchanted.eg_particle_interactions.common.resource.texture.UVCoordinates;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.Nullable;

public class RandomScaleUV extends UVProvider {
    public static MapCodec<RandomScaleUV> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Codec.FLOAT.optionalFieldOf("scale", 1f).forGetter(RandomScaleUV::getScale)
        )
        .apply(
            i,
            RandomScaleUV::new
        )
    );

    final float scale;

    RandomScaleUV(float scale) {
        this.scale = scale;
    }

    @Override
    public UVCoordinates getUv(@Nullable UVCoordinates oldUV, TextureAtlasSprite sprite, float particleScale) {
        int spriteWidth = sprite.contents().width();
        int spriteHeight = sprite.contents().height();

        float pixelsForScale = MathHelper.ceilWithResolution(particleScale * this.getScale() * Math.max(spriteWidth, spriteHeight), Math.max(spriteWidth, spriteHeight));

        float u0 = (float) MathHelper.randomBetween(0, spriteWidth);
        if(u0 + pixelsForScale > spriteWidth) u0 = Math.max(0, u0 - pixelsForScale);

        float v0 = (float) MathHelper.randomBetween(0, spriteHeight);
        if(v0 + pixelsForScale > spriteHeight) v0 = Math.max(0, v0 - pixelsForScale);

        return new UVCoordinates(
            u0 / spriteWidth,
            v0 / spriteHeight,
            Math.min((u0 + pixelsForScale) / spriteWidth, 1),
            Math.min((v0 + pixelsForScale) / spriteHeight, 1)
        );
    }

    protected float getScale() {
        return this.scale;
    }

    @Override
    public MapCodec<? extends UVProvider> codec() {
        return MAP_CODEC;
    }
}
