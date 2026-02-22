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
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record ParticleAppearance(@Nullable TextureConfig textureConfig, ColourSource colourSource, int lightEmission) {
    private static final int DEFAULT_LIGHT_EMISSION = 0;

    public static final ParticleAppearance FALLBACK_APPEARANCE = new ParticleAppearance(
        new ParticleAppearance.TextureConfig(List.of(MissingTextureAtlasSprite.getLocation()), AtlasIds.PARTICLES, TextureConfig.DEFAULT_CYCLE_MODE),
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

    public record TextureConfig(List<Identifier> sprites, Identifier atlasId, SpriteCycleMode spriteCycleMode) {
        public static final SpriteCycleMode DEFAULT_CYCLE_MODE = SpriteCycleMode.RANDOM_ON_SPAWN;

        public static final Codec<TextureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.list(Identifier.CODEC).fieldOf("sprites").forGetter(o -> null),
                Identifier.CODEC.optionalFieldOf("atlas", AtlasIds.PARTICLES).forGetter(TextureConfig::atlasId),
                StringRepresentable.fromEnum(SpriteCycleMode::values).optionalFieldOf("sprite_cylce_mode", DEFAULT_CYCLE_MODE).forGetter(TextureConfig::spriteCycleMode)
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

    public enum SpriteCycleMode implements StringRepresentable {
        RANDOM_ON_SPAWN("random_on_spawn"),
        RANDOM_PER_TICK("random_per_tick"),
        AGE_CYCLE("age_cycle");

        final String name;

        SpriteCycleMode(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
