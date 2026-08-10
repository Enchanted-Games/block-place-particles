package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.IntMathModifier;

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
