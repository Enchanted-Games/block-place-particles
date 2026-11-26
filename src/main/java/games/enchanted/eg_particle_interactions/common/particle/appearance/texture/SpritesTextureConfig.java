package games.enchanted.eg_particle_interactions.common.particle.appearance.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.SpriteCycleMode;
import games.enchanted.eg_particle_interactions.common.resource.texture.AtlasIdAndTexture;
import games.enchanted.eg_particle_interactions.common.resource.texture.TextureHelpers;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public class SpritesTextureConfig implements TextureConfig {
    public static final SpriteCycleMode DEFAULT_CYCLE_MODE = SpriteCycleMode.RANDOM_ON_SPAWN;
    public static final AtlasIdAndTexture DEFAULT_ATLAS = new AtlasIdAndTexture(AtlasIds.PARTICLES, TextureAtlas.LOCATION_PARTICLES);
    public static final int MAX_LIST_LENGTH = 1024;

    public static final MapCodec<? extends TextureConfig> MAP_CODEC = RecordCodecBuilder.<SpritesTextureConfig>mapCodec(
        instance -> instance.group(
            Codec.list(Identifier.CODEC, 1, MAX_LIST_LENGTH).fieldOf("sprites").forGetter(o -> o.sprites),
            ModCodecs.ATLAS.optionalFieldOf("atlas", DEFAULT_ATLAS).forGetter(o -> o.atlas),
            StringRepresentable.fromEnum(SpriteCycleMode::values).optionalFieldOf("sprite_cycle_mode", DEFAULT_CYCLE_MODE).forGetter(o -> o.spriteCycleMode)
        ).apply(
            instance,
            SpritesTextureConfig::new
        )
    );

    final List<Identifier> sprites;
    final AtlasIdAndTexture atlas;
    final SpriteCycleMode spriteCycleMode;

    public SpritesTextureConfig(List<Identifier> sprites, AtlasIdAndTexture atlas, SpriteCycleMode spriteCycleMode) {
        this.sprites = sprites;
        this.atlas = atlas;
        this.spriteCycleMode = spriteCycleMode;
    }

    private TextureAtlasSprite lookupSprite(Identifier id) {
        return TextureHelpers.getSpriteFromAtlas(id, this.atlas.id());
    }

    @Override
    public TextureAtlasSprite getAt(ParticleContext context, float agePercentage) {
        int i = (int) (agePercentage * (this.sprites.size()));
        return this.lookupSprite(this.sprites.get(Math.clamp(i, 0, this.sprites.size() - 1)));
    }

    @Override
    public TextureAtlasSprite getRandom(ParticleContext context, float randomValue) {
        int i = (int) (randomValue * (this.sprites.size()));
        return this.lookupSprite(this.sprites.get(Math.clamp(i, 0, this.sprites.size() - 1)));
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
    public SpriteCycleMode getSpriteCycleMode(ParticleContext context) {
        return this.spriteCycleMode;
    }

    @Override
    public List<Identifier> getSpriteIds(ParticleContext context) {
        return this.sprites;
    }

    @Override
    public AtlasIdAndTexture getAtlas(ParticleContext context) {
        return this.atlas;
    }

    @Override
    public boolean containsValidMaskSprites() {
        return !(this.sprites.size() == 1 && this.sprites.getFirst().equals(MissingTextureAtlasSprite.getLocation()));
    }
}
