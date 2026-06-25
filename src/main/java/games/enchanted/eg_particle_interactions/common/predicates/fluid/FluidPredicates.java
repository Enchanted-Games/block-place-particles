package games.enchanted.eg_particle_interactions.common.predicates.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class FluidPredicates {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends FluidPredicate>> PREDICATE_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<FluidPredicate> CODEC = PREDICATE_TYPES.codec(ModCodecs.IDENTIFIER).dispatch("type", FluidPredicate::codec, mapCodec -> mapCodec);

    static {
        PREDICATE_TYPES.put(ParticleInteractionsMod.id("list"), FluidListPredicate.CODEC);
        PREDICATE_TYPES.put(ParticleInteractionsMod.id("fluid_state"), FluidStatePredicate.CODEC);
    }
}
