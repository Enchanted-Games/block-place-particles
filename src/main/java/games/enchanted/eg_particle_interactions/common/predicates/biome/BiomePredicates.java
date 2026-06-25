package games.enchanted.eg_particle_interactions.common.predicates.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class BiomePredicates {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends BiomePredicate>> PREDICATE_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<BiomePredicate> CODEC = PREDICATE_TYPES.codec(ModCodecs.IDENTIFIER).dispatch("type", BiomePredicate::codec, mapCodec -> mapCodec);

    static {
        PREDICATE_TYPES.put(ParticleInteractionsMod.id("list"), BiomeListPredicate.CODEC);
        PREDICATE_TYPES.put(ParticleInteractionsMod.id("single_biome"), SingleBiomePredicate.CODEC);
    }
}
