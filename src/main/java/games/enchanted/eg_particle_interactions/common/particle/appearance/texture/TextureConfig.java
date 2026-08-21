package games.enchanted.eg_particle_interactions.common.particle.appearance.texture;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.LayerDefinition;
import games.enchanted.eg_particle_interactions.common.particle.appearance.SpriteCycleMode;
import games.enchanted.eg_particle_interactions.common.resource.texture.AtlasIdAndTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import java.util.List;

public interface TextureConfig {
    TextureAtlasSprite getAt(ParticleContext context, float agePercentage);

    default TextureAtlasSprite getRandom(ParticleContext context, RandomSource random) {
        return getRandom(context, random.nextFloat());
    }

    TextureAtlasSprite getRandom(ParticleContext context, float randomValue);

    TextureAtlasSprite getFirst(ParticleContext context);

    LayerDefinition getLayerDefinition(ParticleContext context);

    SpriteCycleMode getSpriteCycleMode(ParticleContext context);

    List<Identifier> getSpriteIds(ParticleContext context);

    AtlasIdAndTexture getAtlas(ParticleContext context);

    MapCodec<? extends TextureConfig> codec();
}
