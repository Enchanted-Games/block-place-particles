package games.enchanted.eg_particle_interactions.common.particle.event.action.lifetime;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.FloatMathModifier;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.Vector3dMathModifier;

public class ModifyGravityAction extends EventAction {
    public static final MapCodec<ModifyGravityAction> CODEC = FloatMathModifier.CODEC.xmap(
        ModifyGravityAction::new,
        ModifyGravityAction::getModifier
    );

    final FloatMathModifier modifier;

    ModifyGravityAction(FloatMathModifier modifier) {
        this.modifier = modifier;
    }

    protected FloatMathModifier getModifier() {
        return this.modifier;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyGravity(this.modifier);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
