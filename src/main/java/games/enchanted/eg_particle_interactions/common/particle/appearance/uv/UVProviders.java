package games.enchanted.eg_particle_interactions.common.particle.appearance.uv;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class UVProviders {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends UVProvider>> PROVIDERS = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<UVProvider> CODEC = Codec.withAlternative(
        PROVIDERS.codec(ModCodecs.IDENTIFIER).dispatch("type", UVProvider::codec, mapCodec -> mapCodec),
        SimpleUV.CODEC
    );

    static {
        PROVIDERS.put(ParticleInteractionsMod.id("simple"), SimpleUV.MAP_CODEC);
    }
}
