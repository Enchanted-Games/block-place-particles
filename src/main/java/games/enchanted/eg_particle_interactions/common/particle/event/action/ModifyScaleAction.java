package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.FloatMathModifier;

public class ModifyScaleAction extends EventAction {
    public static final MapCodec<ModifyScaleAction> CODEC = FloatMathModifier.CODEC.xmap(
        ModifyScaleAction::new,
        ModifyScaleAction::getModifier
    );

    final FloatMathModifier modifier;

    ModifyScaleAction(FloatMathModifier modifier) {
        this.modifier = modifier;
    }

    protected FloatMathModifier getModifier() {
        return this.modifier;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyScale(this.modifier);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
