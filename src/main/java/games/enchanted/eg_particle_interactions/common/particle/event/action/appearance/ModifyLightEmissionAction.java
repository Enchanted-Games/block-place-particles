package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.FloatMathModifier;
import games.enchanted.eg_particle_interactions.common.util.math.IntMathModifier;

public class ModifyLightEmissionAction extends EventAction {
    public static final MapCodec<ModifyLightEmissionAction> CODEC = IntMathModifier.CODEC.xmap(
        ModifyLightEmissionAction::new,
        ModifyLightEmissionAction::getModifier
    );

    final IntMathModifier modifier;

    ModifyLightEmissionAction(IntMathModifier modifier) {
        this.modifier = modifier;
    }

    protected IntMathModifier getModifier() {
        return this.modifier;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyLightEmission(this.modifier);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
