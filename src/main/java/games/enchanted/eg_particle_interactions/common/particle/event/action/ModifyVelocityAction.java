package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.Vector3dMathModifier;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public class ModifyVelocityAction extends EventAction {
    public static final MapCodec<ModifyVelocityAction> CODEC = Vector3dMathModifier.CODEC.xmap(
        ModifyVelocityAction::new,
        ModifyVelocityAction::getModifier
    );

    final Vector3dMathModifier modifier;

    ModifyVelocityAction(Vector3dMathModifier modifier) {
        this.modifier = modifier;
    }

    protected Vector3dMathModifier getModifier() {
        return this.modifier;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyVelocity(this.modifier);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
