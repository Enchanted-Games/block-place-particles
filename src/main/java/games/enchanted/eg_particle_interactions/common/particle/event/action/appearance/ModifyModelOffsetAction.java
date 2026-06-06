package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.Vector3fMathModifier;

public class ModifyModelOffsetAction extends EventAction {
    public static final MapCodec<ModifyModelOffsetAction> CODEC = Vector3fMathModifier.CODEC.xmap(
        ModifyModelOffsetAction::new,
        ModifyModelOffsetAction::getModifier
    );

    final Vector3fMathModifier modifier;

    ModifyModelOffsetAction(Vector3fMathModifier modifier) {
        this.modifier = modifier;
    }

    protected Vector3fMathModifier getModifier() {
        return this.modifier;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyModelOffset(this.modifier);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
