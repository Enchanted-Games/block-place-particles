package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

import java.util.List;

public class OrLogicEmitterCondition extends AndLogicEmitterCondition {
    public static final MapCodec<OrLogicEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Codec.list(EmitterConditions.CODEC).fieldOf("conditions").forGetter(OrLogicEmitterCondition::getConditions)
        ).apply(
            i,
            OrLogicEmitterCondition::new
        )
    );

    OrLogicEmitterCondition(List<EmitterCondition> conditions) {
        super(conditions);
    }

    @Override
    public boolean matches(ParticleContext context) {
        return this.conditions.stream().anyMatch(c -> c.matches(context));
    }

    @Override
    public MapCodec<? extends EmitterCondition> codec() {
        return CODEC;
    }
}
