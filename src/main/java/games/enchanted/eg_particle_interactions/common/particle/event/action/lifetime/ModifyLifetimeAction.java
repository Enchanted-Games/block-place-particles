package games.enchanted.eg_particle_interactions.common.particle.event.action.lifetime;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.modifier.IntMathModifier;

public class ModifyLifetimeAction extends EventAction {
    public static final MapCodec<ModifyLifetimeAction> CODEC = IntMathModifier.CODEC.xmap(
        ModifyLifetimeAction::new,
        ModifyLifetimeAction::getModifier
    );

    final IntMathModifier modifier;

    ModifyLifetimeAction(IntMathModifier modifier) {
        this.modifier = modifier;
    }

    protected IntMathModifier getModifier() {
        return this.modifier;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyLifetime(this.modifier);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
