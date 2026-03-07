package games.enchanted.eg_particle_interactions.common.override_system.predicate.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class BlockPredicates {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends BlockPredicate>> PREDICATE_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<BlockPredicate> CODEC = PREDICATE_TYPES.codec(ModCodecs.IDENTIFIER).dispatch("type", BlockPredicate::codec, mapCodec -> mapCodec);

    static {
        PREDICATE_TYPES.put(ParticleInteractionsMod.id("list"), BlockListPredicate.CODEC);
        PREDICATE_TYPES.put(ParticleInteractionsMod.id("block_state"), BlockStatePredicate.CODEC);
    }
}
