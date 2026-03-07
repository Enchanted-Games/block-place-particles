package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.ColourSource;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.ColourSources;
import games.enchanted.eg_particle_interactions.common.particle.render.ModRenderPipelines;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
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
        new ParticleAppearance.TextureConfig(
            List.of(MissingTextureAtlasSprite.getLocation()),
            TextureConfig.DEFAULT_ATLAS,
            TextureConfig.DEFAULT_CYCLE_MODE,
            TextureConfig.DEFAULT_LAYER_DEFINITION
        ),
        ColourSources.WHITE,
        DEFAULT_LIGHT_EMISSION
    );

    public static final Codec<ParticleAppearance> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            TextureConfig.CODEC.optionalFieldOf("texture_config").forGetter(appearance -> Optional.ofNullable(appearance.textureConfig)),
            ColourSources.CODEC.optionalFieldOf("colour").forGetter(appearance -> Optional.of(appearance.colourSource())),
            Codec.intRange(0, 15).optionalFieldOf("light_emission").forGetter(appearance -> Optional.of(appearance.lightEmission()))
        ).apply(
            instance,
            (
                sprites,
                colourSource,
                lightEmission
            ) -> new ParticleAppearance(
                sprites.orElse(null),
                colourSource.orElse(ColourSources.WHITE),
                lightEmission.orElse(DEFAULT_LIGHT_EMISSION)
            )
        )
    );

    // TODO: add dedicated mode for auto item and block textures, instead of that being inferred from the lack of a TextureConfig
    public record TextureConfig(List<Identifier> sprites, TextureHelpers.AtlasIdAndTexture atlas, SpriteCycleMode spriteCycleMode, LayerDefinition layer) {
        public static final SpriteCycleMode DEFAULT_CYCLE_MODE = SpriteCycleMode.RANDOM_ON_SPAWN;
        public static final LayerDefinition DEFAULT_LAYER_DEFINITION = LayerDefinition.CUTOUT;
        public static final TextureHelpers.AtlasIdAndTexture DEFAULT_ATLAS = new TextureHelpers.AtlasIdAndTexture(AtlasIds.PARTICLES, TextureAtlas.LOCATION_PARTICLES);

        public static final Codec<TextureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.list(Identifier.CODEC).fieldOf("sprites").forGetter(o -> null),
                ModCodecs.ATLAS.optionalFieldOf("atlas", DEFAULT_ATLAS).forGetter(TextureConfig::atlas),
                StringRepresentable.fromEnum(SpriteCycleMode::values).optionalFieldOf("sprite_cycle_mode", DEFAULT_CYCLE_MODE).forGetter(TextureConfig::spriteCycleMode),
                StringRepresentable.fromEnum(LayerDefinition::values).optionalFieldOf("layer", DEFAULT_LAYER_DEFINITION).forGetter(TextureConfig::layer)
            ).apply(
                instance,
                TextureConfig::new
            )
        );

        public TextureAtlasSprite lookupSprite(Identifier id) {
            return TextureHelpers.getSpriteFromAtlas(id, this.atlas.id());
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

    public enum LayerDefinition implements StringRepresentable {
        CUTOUT("cutout", false, RenderPipelines.OPAQUE_PARTICLE),
        TRANSLUCENT("translucent", true, RenderPipelines.TRANSLUCENT_PARTICLE),
        CUTOUT_BACKFACE("cutout_backface", false, ModRenderPipelines.BACKFACE_CUTOUT_PARTICLE),
        TRANSLUCENT_BACKFACE("translucent_backface", true, ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE);

        final String name;
        final boolean translucent;
        final RenderPipeline pipeline;

        LayerDefinition(String name, boolean translucent, RenderPipeline pipeline) {
            this.name = name;
            this.translucent = translucent;
            this.pipeline = pipeline;
        }

        public boolean isTranslucent() {
            return translucent;
        }

        public RenderPipeline pipeline() {
            return pipeline;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
