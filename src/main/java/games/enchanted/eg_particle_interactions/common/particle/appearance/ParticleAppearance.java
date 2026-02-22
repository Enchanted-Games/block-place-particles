package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.ColourSource;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.StaticColourSource;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record ParticleAppearance(@Nullable TextureConfig textureConfig, ColourSource colourSource, int lightEmission) {
    private static final int DEFAULT_LIGHT_EMISSION = 0;

    public static final ParticleAppearance FALLBACK_APPEARANCE = new ParticleAppearance(
        new ParticleAppearance.TextureConfig(List.of(MissingTextureAtlasSprite.getLocation()), AtlasIds.PARTICLES, true),
        new StaticColourSource(new int[]{255, 255, 255, 255}),
        DEFAULT_LIGHT_EMISSION
    );

    public static final Codec<ParticleAppearance> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            TextureConfig.CODEC.optionalFieldOf("texture_config").forGetter(textureSource -> Optional.ofNullable(textureSource.textureConfig)),
            Codec.intRange(0, 15).optionalFieldOf("light_emission").forGetter(appearance -> Optional.of(appearance.lightEmission()))
        ).apply(
            instance,
            (
                sprites,
                lightEmission
            ) -> new ParticleAppearance(
                sprites.orElse(null),
                new StaticColourSource(new int[]{255, 255, 255, 255}),
                lightEmission.orElse(DEFAULT_LIGHT_EMISSION)
            )
        )
    );

    public record TextureConfig(List<Identifier> sprites, Identifier atlasId, boolean chooseRandomSprite) {
        public static final Codec<TextureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.list(Identifier.CODEC).fieldOf("sprites").forGetter(o -> null),
                Identifier.CODEC.optionalFieldOf("atlas", AtlasIds.PARTICLES).forGetter(TextureConfig::atlasId),
                Codec.BOOL.optionalFieldOf("choose_random_sprite", true).forGetter(TextureConfig::chooseRandomSprite)
            ).apply(
                instance,
                TextureConfig::new
            )
        );

        public TextureAtlasSprite lookupSprite(Identifier id) {
            return TextureHelpers.getSpriteFromAtlas(id, this.atlasId());
        }

        public TextureAtlasSprite getAt(int index, int max) {
            return this.lookupSprite(this.sprites.get(index * (this.sprites.size() - 1) / max));
        }

        public TextureAtlasSprite getRandom(RandomSource random) {
            return this.lookupSprite(this.sprites.get(random.nextInt(this.sprites.size())));
        }

        public TextureAtlasSprite getFirst() {
            return this.lookupSprite(this.sprites.getFirst());
        }
    }
}
