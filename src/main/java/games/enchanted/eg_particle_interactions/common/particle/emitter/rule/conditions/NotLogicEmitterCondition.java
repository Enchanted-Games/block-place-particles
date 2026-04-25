package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

import java.util.List;

public class NotLogicEmitterCondition extends EmitterCondition {
    public static final MapCodec<NotLogicEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            EmitterConditions.CODEC.fieldOf("condition").forGetter(NotLogicEmitterCondition::getCondition)
        ).apply(
            i,
            NotLogicEmitterCondition::new
        )
    );

    final EmitterCondition condition;

    NotLogicEmitterCondition(EmitterCondition condition) {
        this.condition = condition;
    }

    protected EmitterCondition getCondition() {
        return this.condition;
    }

    @Override
    public boolean matches(ParticleContext context) {
        return !this.condition.matches(context);
    }

    @Override
    public MapCodec<? extends EmitterCondition> codec() {
        return CODEC;
    }
}
