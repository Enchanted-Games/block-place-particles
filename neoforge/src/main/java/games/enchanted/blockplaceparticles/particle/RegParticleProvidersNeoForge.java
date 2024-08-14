package games.enchanted.blockplaceparticles.particle;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class RegParticleProvidersNeoForge {
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.FALLING_CHERRY_PETAL, FallingPetalProvider::new);
    }
}
