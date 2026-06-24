package games.enchanted.eg_particle_interactions.common.resource.texture.palette.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.resource.texture.AtlasIdAndTexture;
import games.enchanted.eg_particle_interactions.common.resource.texture.TexturePalettes;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.Palette;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

import java.util.Optional;

public class TexturePaletteType extends PaletteType {
    public static final MapCodec<TexturePaletteType> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            Identifier.CODEC.fieldOf("texture").forGetter(TexturePaletteType::textureId),
            ModCodecs.ATLAS.optionalFieldOf("atlas", AtlasIdAndTexture.BLOCKS).forGetter(TexturePaletteType::atlasId)
        ).apply(
            i,
            TexturePaletteType::new
        )
    );

    final Identifier textureId;
    final AtlasIdAndTexture atlasId;
    Palette palette = null;

    TexturePaletteType(Identifier textureId, AtlasIdAndTexture atlasId) {
        this.textureId = textureId;
        this.atlasId = atlasId;
    }

    @Override
    public Palette getOrCreatePalette() {
        if(this.palette != null) {
            return this.palette;
        }
        TextureAtlas atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(atlasId.id());
        TextureAtlasSprite sprite = atlas.getSprite(textureId);
        this.palette = TexturePalettes.generatePalette(sprite.contents());
        return this.palette;
    }

    @Override
    public MapCodec<? extends PaletteType> codec() {
        return CODEC;
    }

    protected Identifier textureId() {
        return this.textureId;
    }

    protected AtlasIdAndTexture atlasId() {
        return this.atlasId;
    }
}
