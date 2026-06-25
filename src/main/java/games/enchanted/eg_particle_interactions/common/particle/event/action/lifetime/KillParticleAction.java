package games.enchanted.eg_particle_interactions.common.particle.event.action.lifetime;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public class KillParticleAction extends EventAction {
    public static final MapCodec<KillParticleAction> CODEC = MapCodec.unit(new KillParticleAction());

    KillParticleAction() {
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.remove();
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
