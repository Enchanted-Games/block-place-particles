package games.enchanted.eg_particle_interactions.common.resource.texture.palette.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class PaletteTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends PaletteType>> PALETTE_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<PaletteType> CODEC = PALETTE_TYPES.codec(ModCodecs.IDENTIFIER).dispatch("type", PaletteType::codec, mapCodec -> mapCodec);

    static {
        PALETTE_TYPES.put(ParticleInteractionsMod.id("static"), StaticPaletteType.CODEC);
        PALETTE_TYPES.put(ParticleInteractionsMod.id("texture"), TexturePaletteType.CODEC);
    }
}
