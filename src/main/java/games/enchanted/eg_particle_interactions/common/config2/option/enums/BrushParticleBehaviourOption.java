package games.enchanted.eg_particle_interactions.common.config2.option.enums;

import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import net.minecraft.util.StringRepresentable;

public class BrushParticleBehaviourOption extends EnumOption<BrushParticleBehaviour> {
    public BrushParticleBehaviourOption(BrushParticleBehaviour initialAndDefaultValue, String jsonKey) {
        super(initialAndDefaultValue, jsonKey);
    }

    @Override
    protected StringRepresentable.EnumCodec<BrushParticleBehaviour> getCodec() {
        return StringRepresentable.fromEnum(BrushParticleBehaviour::values);
    }
}
