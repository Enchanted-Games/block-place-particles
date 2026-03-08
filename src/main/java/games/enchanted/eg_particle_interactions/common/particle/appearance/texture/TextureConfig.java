package games.enchanted.eg_particle_interactions.common.particle.appearance.texture;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.LayerDefinition;
import games.enchanted.eg_particle_interactions.common.particle.appearance.SpriteCycleMode;
import games.enchanted.eg_particle_interactions.common.util.TextureHelpers;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import java.util.List;

public interface TextureConfig {
    TextureAtlasSprite getAt(ParticleContext context, int age, int max);

    TextureAtlasSprite getRandom(ParticleContext context, RandomSource random);

    TextureAtlasSprite getFirst(ParticleContext context);

    LayerDefinition getLayerDefinition(ParticleContext context);

    SpriteCycleMode getSpriteCycleMode(ParticleContext context);

    List<Identifier> getSpriteIds(ParticleContext context);

    TextureHelpers.AtlasIdAndTexture getAtlas(ParticleContext context);

    MapCodec<? extends TextureConfig> codec();
}
