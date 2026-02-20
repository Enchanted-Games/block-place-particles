package games.enchanted.eg_particle_interactions.common.resource.texture_source;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.resource.texture_source.colour.ColourSource;
import games.enchanted.eg_particle_interactions.common.resource.texture_source.colour.StaticColourSource;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record TextureSource(@Nullable Sprites sprites, ColourSource colourSource) {
    public static final Codec<TextureSource> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Sprites.CODEC.optionalFieldOf("texture_config").forGetter(textureSource -> Optional.ofNullable(textureSource.sprites))
        ).apply(
            instance,
            sprites -> new TextureSource(sprites.orElse(null), new StaticColourSource(new int[]{255,255,255,255}))
        )
    );

    public record Sprites(List<Identifier> sprites, Identifier atlasId) {
        public static final Codec<Sprites> CODEC = RecordCodecBuilder.create(
            spritesInstance -> spritesInstance.group(
                Codec.list(Identifier.CODEC).fieldOf("sprites").forGetter(o -> null),
                Identifier.CODEC.optionalFieldOf("atlas", AtlasIds.PARTICLES).forGetter(Sprites::atlasId)
            ).apply(
                spritesInstance,
                Sprites::new
            )
        );
    }
}
