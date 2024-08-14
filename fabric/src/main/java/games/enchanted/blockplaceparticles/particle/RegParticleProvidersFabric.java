package games.enchanted.blockplaceparticles.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class RegParticleProvidersFabric {
    public static void registerParticles() {
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.FALLING_CHERRY_PETAL, FallingPetalProvider::new);
    }
}
