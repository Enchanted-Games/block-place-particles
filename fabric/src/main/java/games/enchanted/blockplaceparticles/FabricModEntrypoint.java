package games.enchanted.blockplaceparticles;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.RegParticleProvidersFabric;
import net.fabricmc.api.ModInitializer;

public class FabricModEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonEntrypoint.initBeforeRegistration();

        ModParticleTypes.registerParticles();
        RegParticleProvidersFabric.registerParticles();
    }
}
