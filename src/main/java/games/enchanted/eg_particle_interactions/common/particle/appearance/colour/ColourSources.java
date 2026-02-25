package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class ColourSources {
    public static final ColourSource WHITE = new StaticColourSource(0xffffffff);

    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ColourSource>> SOURCES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<ColourSource> CODEC = SOURCES.codec(Identifier.CODEC).dispatch("type", ColourSource::codec, mapCodec -> mapCodec);

    static {
        SOURCES.put(ParticleInteractionsMod.id("constant"), StaticColourSource.CODEC);
        SOURCES.put(ParticleInteractionsMod.id("block_texture"), BlockTextureColourSource.CODEC);
        SOURCES.put(ParticleInteractionsMod.id("block_tint"), BlockTintColourSource.CODEC);
    }
}
