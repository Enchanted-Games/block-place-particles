package games.enchanted.eg_particle_interactions.common.predicates.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class EntityPredicates {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends EntityPredicate>> PREDICATE_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<EntityPredicate> CODEC = PREDICATE_TYPES.codec(ModCodecs.IDENTIFIER).dispatch("type", EntityPredicate::codec, mapCodec -> mapCodec);

    static {
        PREDICATE_TYPES.put(ParticleInteractionsMod.id("list"), EntityListPredicate.CODEC);
        PREDICATE_TYPES.put(ParticleInteractionsMod.id("single_entity"), SingleEntityPredicate.CODEC);
    }
}
