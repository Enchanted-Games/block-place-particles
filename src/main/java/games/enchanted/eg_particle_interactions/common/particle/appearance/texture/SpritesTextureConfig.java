package games.enchanted.eg_particle_interactions.common.particle.appearance.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.LayerDefinition;
import games.enchanted.eg_particle_interactions.common.particle.appearance.SpriteCycleMode;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public class SpritesTextureConfig implements TextureConfig {
    public static final SpriteCycleMode DEFAULT_CYCLE_MODE = SpriteCycleMode.RANDOM_ON_SPAWN;
    public static final TextureHelpers.AtlasIdAndTexture DEFAULT_ATLAS = new TextureHelpers.AtlasIdAndTexture(AtlasIds.PARTICLES, TextureAtlas.LOCATION_PARTICLES);
    public static final LayerDefinition DEFAULT_LAYER_DEFINITION = LayerDefinition.CUTOUT;

    public static final MapCodec<? extends TextureConfig> MAP_CODEC = RecordCodecBuilder.<SpritesTextureConfig>mapCodec(
        instance -> instance.group(
            Codec.list(Identifier.CODEC).fieldOf("sprites").forGetter(o -> o.sprites),
            ModCodecs.ATLAS.optionalFieldOf("atlas", DEFAULT_ATLAS).forGetter(o -> o.atlas),
            StringRepresentable.fromEnum(SpriteCycleMode::values).optionalFieldOf("sprite_cycle_mode", DEFAULT_CYCLE_MODE).forGetter(o -> o.spriteCycleMode),
            StringRepresentable.fromEnum(LayerDefinition::values).optionalFieldOf("layer", DEFAULT_LAYER_DEFINITION).forGetter(o -> o.layer)
        ).apply(
            instance,
            SpritesTextureConfig::new
        )
    );

    final List<Identifier> sprites;
    final TextureHelpers.AtlasIdAndTexture atlas;
    final SpriteCycleMode spriteCycleMode;
    final LayerDefinition layer;

    public SpritesTextureConfig(List<Identifier> sprites, TextureHelpers.AtlasIdAndTexture atlas, SpriteCycleMode spriteCycleMode, LayerDefinition layer) {
        this.sprites = sprites;
        this.atlas = atlas;
        this.spriteCycleMode = spriteCycleMode;
        this.layer = layer;
    }

    private TextureAtlasSprite lookupSprite(Identifier id) {
        return TextureHelpers.getSpriteFromAtlas(id, this.atlas.id());
    }

    @Override
    public TextureAtlasSprite getAt(ParticleContext context, int age, int max) {
        return this.lookupSprite(this.sprites.get(age * (this.sprites.size() - 1) / max));
    }

    @Override
    public TextureAtlasSprite getRandom(ParticleContext context, RandomSource random) {
        return this.lookupSprite(this.sprites.get(random.nextInt(this.sprites.size())));
    }

    @Override
    public TextureAtlasSprite getFirst(ParticleContext context) {
        return this.lookupSprite(this.sprites.getFirst());
    }

    @Override
    public MapCodec<? extends TextureConfig> codec() {
        return null;
    }

    @Override
    public LayerDefinition getLayerDefinition(ParticleContext context) {
        return this.layer;
    }

    @Override
    public SpriteCycleMode getSpriteCycleMode(ParticleContext context) {
        return this.spriteCycleMode;
    }

    @Override
    public List<Identifier> getSpriteIds(ParticleContext context) {
        return this.sprites;
    }

    @Override
    public TextureHelpers.AtlasIdAndTexture getAtlas(ParticleContext context) {
        return this.atlas;
    }
}
