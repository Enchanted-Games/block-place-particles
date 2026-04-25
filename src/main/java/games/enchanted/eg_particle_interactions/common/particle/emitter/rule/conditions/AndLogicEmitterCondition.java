package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

import java.util.List;

public class AndLogicEmitterCondition extends EmitterCondition {
    public static final MapCodec<AndLogicEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Codec.list(EmitterConditions.CODEC).fieldOf("conditions").forGetter(AndLogicEmitterCondition::getConditions)
        ).apply(
            i,
            AndLogicEmitterCondition::new
        )
    );

    final List<EmitterCondition> conditions;

    AndLogicEmitterCondition(List<EmitterCondition> conditions) {
        this.conditions = conditions;
    }

    protected List<EmitterCondition> getConditions() {
        return this.conditions;
    }

    @Override
    public boolean matches(ParticleContext context) {
        return this.conditions.stream().allMatch(c -> c.matches(context));
    }

    @Override
    public MapCodec<? extends EmitterCondition> codec() {
        return CODEC;
    }
}
