package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class EmitterConditions {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends EmitterCondition>> EMITTER_RULE_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<EmitterCondition> CODEC = EMITTER_RULE_TYPES.codec(ModCodecs.IDENTIFIER).dispatch("type", EmitterCondition::codec, mapCodec -> mapCodec);

    static {
        EMITTER_RULE_TYPES.put(ParticleInteractionsMod.id("block"), BlockPredicateEmitterCondition.CODEC);
        EMITTER_RULE_TYPES.put(ParticleInteractionsMod.id("fluid"), FluidPredicateEmitterCondition.CODEC);
        EMITTER_RULE_TYPES.put(ParticleInteractionsMod.id("biome"), BiomePredicateEmitterCondition.CODEC);
    }
}
