package games.enchanted.eg_particle_interactions.common.particle.appearance.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public class TextureConfigs {
    public static final TextureConfig MISSING_APPEARANCE = new SpritesTextureConfig(
        List.of(ParticleInteractionsMod.id("missing_appearance")),
        SpritesTextureConfig.DEFAULT_ATLAS,
        SpritesTextureConfig.DEFAULT_CYCLE_MODE,
        SpritesTextureConfig.DEFAULT_LAYER_DEFINITION
    );
    public static final TextureConfig MISSING_DEFINITION = new SpritesTextureConfig(
        List.of(ParticleInteractionsMod.id("missing_definition")),
        SpritesTextureConfig.DEFAULT_ATLAS,
        SpritesTextureConfig.DEFAULT_CYCLE_MODE,
        SpritesTextureConfig.DEFAULT_LAYER_DEFINITION
    );

    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends TextureConfig>> SOURCES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<TextureConfig> CODEC = Codec.withAlternative(
        SOURCES.codec(ModCodecs.IDENTIFIER).dispatch("type", TextureConfig::codec, mapCodec -> mapCodec),
        SpritesTextureConfig.MAP_CODEC.codec()
    );

    static {
        SOURCES.put(ParticleInteractionsMod.id("sprites"), SpritesTextureConfig.MAP_CODEC);
        SOURCES.put(ParticleInteractionsMod.id("block_texture"), BlockTextureConfig.MAP_CODEC);
        SOURCES.put(ParticleInteractionsMod.id("fluid_texture"), FluidTextureConfig.MAP_CODEC);
    }
}
