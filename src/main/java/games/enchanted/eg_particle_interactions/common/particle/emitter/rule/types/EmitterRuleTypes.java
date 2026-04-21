package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class EmitterRuleTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends EmitterRuleType>> EMITTER_RULE_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<EmitterRuleType> CODEC = EMITTER_RULE_TYPES.codec(ModCodecs.IDENTIFIER).dispatch("type", EmitterRuleType::codec, mapCodec -> mapCodec);

    static {
        EMITTER_RULE_TYPES.put(ParticleInteractionsMod.id("block"), BlockPredicateEmitterRuleType.CODEC);
    }
}
