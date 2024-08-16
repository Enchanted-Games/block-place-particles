package games.enchanted.blockplaceparticles;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import net.fabricmc.api.ClientModInitializer;

public class FabricClientEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleInteractionsMod.startOfModLoading();

        // register particles
        ModParticleTypes.registerParticles();

        ParticleInteractionsMod.endOfModLoading();
    }
}
